package com.example.accounts.service;

import com.example.accounts.dto.AccountsRequest;
import com.example.accounts.dto.CustomerRequest;
import com.example.accounts.dto.CustomerResponse;
import com.example.accounts.exception.EntityAlreadyExistsException;
import com.example.accounts.exception.EntityNotFoundException;
import com.example.accounts.mapper.CustomerMapper;
import com.example.accounts.repo.AccountsRepo;
import com.example.accounts.repo.CustomerRepo;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Set;

@RequiredArgsConstructor
@Service
@FieldDefaults(makeFinal = true,level = AccessLevel.PRIVATE)
public class CustomerService {
    CustomerRepo repo;
    CustomerMapper mapper;
    AccountsRepo accountsRepo;
    Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

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
        var SavedAccount = accountsRepo.save(account);
        return mapper.toDto(savedCustomer);
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


}
