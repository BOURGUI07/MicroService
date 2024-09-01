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
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
@FieldDefaults(makeFinal = true,level = AccessLevel.PRIVATE)
public class CustomerService {
    CustomerRepo repo;
    CustomerMapper mapper;
    AccountsRepo accountsRepo;

    @Transactional
    public CustomerResponse create(CustomerRequest x) {
        if(repo.existsByNameOrEmailOrPhone(x.name(),x.email(),x.phone())){
            throw new EntityAlreadyExistsException("Customer already exists");
        }
        if(accountsRepo.existsByAccountTypeOrBranchAddress(x.accountType(),x.branchAddress())){
            throw new EntityAlreadyExistsException("Account already exists");
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

    public CustomerResponse findByPhone(String phone) {
        var account = accountsRepo.findByCustomerPhone(phone)
                .orElseThrow(() -> new EntityNotFoundException("Account not found"));
        return mapper.toDto(account.getCustomer());
    }

    public void deleteByPhone(String phone) {
        if(phone.isBlank()){
            throw new IllegalArgumentException("phone should not be blank");
        }
        accountsRepo.deleteByCustomerPhone(phone);
    }


}
