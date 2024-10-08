package com.example.accounts.service;

import com.example.accounts.dto.*;
import com.example.accounts.entity.Accounts;
import com.example.accounts.entity.Customer;
import com.example.accounts.exception.EntityAlreadyExistsException;
import com.example.accounts.exception.EntityNotFoundException;
import com.example.accounts.mapper.CustomerMapper;
import com.example.accounts.repo.AccountsRepo;
import com.example.accounts.repo.CustomerRepo;
import com.example.accounts.service.client.CardsFeignClient;
import com.example.accounts.service.client.LoansFeignClient;
import jakarta.transaction.Transactional;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.constraints.Pattern;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@RequiredArgsConstructor
@Service
@FieldDefaults(makeFinal = true,level = AccessLevel.PRIVATE)
@Slf4j
public class CustomerService {
    CustomerRepo repo;
    CustomerMapper mapper;
    AccountsRepo accountsRepo;
    Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
    CardsFeignClient cardsFeignClient;
    LoansFeignClient loansFeignClient;
    StreamBridge streamBridge;

    @Transactional
    public CustomerResponse create(CustomerRequest x) {
        var violations = validator.validate(x);
        if (!violations.isEmpty()) {
            throw new ConstraintViolationException(violations);
        }
        if(repo.existsByNameOrEmailOrPhone(x.name(),x.email(),x.phone())){
            throw new EntityAlreadyExistsException("Customer already exists");
        }
        var customer = mapper.toCustomerEntity(x);
        var savedCustomer = repo.save(customer);
        var accountRequest = new AccountsRequest(savedCustomer.getId(), x.accountType(), x.branchAddress());
        var account = mapper.toAccountEntity(accountRequest);
        var savedAccount = accountsRepo.save(account);
        sendCommunication(savedAccount,savedCustomer);
        return mapper.toDto(savedCustomer);
    }

    private void sendCommunication(Accounts savedAccount, Customer savedCustomer) {
        var accountMsgDTO = new AccountsMsgDTO(savedAccount.getAccountNumber(),
                savedCustomer.getName(), savedCustomer.getEmail(), savedCustomer.getPhone());
        log.info("Sending communication request for details {}", accountMsgDTO);
        var result = streamBridge.send("sendCommunication-out-0", accountMsgDTO);
        log.info("Is communication sent successfully? {}", result);
    }

    @Transactional
    public CustomerResponse update(Integer id, CustomerRequest x) {
        var violations = validator.validate(x);
        if (!violations.isEmpty()) {
            throw new ConstraintViolationException(violations);
        }
        if(id==null || id<1){
            throw new IllegalArgumentException("id should be positive");
        }
        var customer = repo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Customer not found"))
                .setEmail(x.email())
                .setPhone(x.phone())
                .setName(x.name());
        var savedCustomer = repo.save(customer);
        var account = accountsRepo.findByCustomerId(id)
                .orElseThrow(() -> new EntityNotFoundException("Account not found"))
                .setAccountType(x.accountType())
                .setBranchAddress(x.branchAddress());
        accountsRepo.save(account);
        return mapper.toDto(savedCustomer);

    }

    public CustomerResponse findByPhone(@Pattern(regexp="(^$|[0-9]{10})",message = "Mobile number must be 10 digits") String phone) {
        var violations = validator.validate(phone);
        if (!violations.isEmpty()) {
            throw new ConstraintViolationException(violations);
        }
        var account = accountsRepo.findByCustomerPhone(phone)
                .orElseThrow(() -> new EntityNotFoundException("Account not found"));
        return mapper.toDto(account.getCustomer());
    }

    @Transactional
    public void deleteByPhone(@Pattern(regexp="(^$|[0-9]{10})",message = "Mobile number must be 10 digits") String phone) {
        var violations = validator.validate(phone);
        if (!violations.isEmpty()) {
            throw new ConstraintViolationException(violations);
        }
        accountsRepo.deleteByCustomerPhone(phone);
    }

    public CustomerDetailsResponse findAllDetailsByPhone(@Pattern(regexp="(^$|[0-9]{10})",message = "Mobile number must be 10 digits") String phone){
        var violations = validator.validate(phone);
        if (!violations.isEmpty()) {
            throw new ConstraintViolationException(violations);
        }
        var customerResponse = repo.findByPhone(phone).map(mapper::toDto).orElseThrow(() -> new EntityNotFoundException("Customer not found"));
        var consumedCard = cardsFeignClient.findByPhone(phone);
        List<CardResponse> cardResponse = null;
        if(consumedCard!=null){
            cardResponse = consumedCard.getBody();
        }
        var consumedLoan = loansFeignClient.findByPhone(phone);
        List<LoanResponse> loanResponse = null;
        if(consumedLoan!=null){
            loanResponse = consumedLoan.getBody();
        }
        return new CustomerDetailsResponse(customerResponse, Optional.ofNullable(cardResponse),Optional.ofNullable(loanResponse));
    }


    public boolean updateCommunicationStatus(Integer accountNumber){
        var account = accountsRepo.findById(accountNumber)
                .orElseThrow(() -> new EntityNotFoundException("Account not found"))
                .setCommunicationStatus(true);
        return accountsRepo.save(account).getCommunicationStatus();
    }

}
