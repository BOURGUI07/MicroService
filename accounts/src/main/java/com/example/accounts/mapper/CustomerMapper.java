package com.example.accounts.mapper;

import com.example.accounts.dto.AccountsRequest;
import com.example.accounts.dto.CustomerRequest;
import com.example.accounts.dto.CustomerResponse;
import com.example.accounts.entity.Accounts;
import com.example.accounts.entity.Customer;
import com.example.accounts.exception.EntityNotFoundException;
import com.example.accounts.repo.AccountsRepo;
import com.example.accounts.repo.CustomerRepo;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true,level = AccessLevel.PRIVATE)
public class CustomerMapper {
    AccountsRepo repo;
    CustomerRepo customerRepo;
    public Customer toCustomerEntity(CustomerRequest x) {
        return new Customer()
                .setEmail(x.email())
                .setName(x.name())
                .setPhone(x.phone());
    }

    public Accounts toAccountEntity(AccountsRequest x) {
        var a= new Accounts().setAccountType(x.accountType()).setBranchAddress(x.branchAddress());
        customerRepo.findById(x.customerId()).ifPresent(a::setCustomer);
        return a;
    }

    public CustomerResponse toDto(Customer x) {
        var account = repo.findByCustomerId(x.getId())
                .orElseThrow(() -> new EntityNotFoundException("Account not found with customer id: " + x.getId()));
        return new CustomerResponse(x.getId(),x.getName(),x.getEmail(),x.getPhone(),account.getAccountNumber(), account.getAccountType(), account.getBranchAddress());
    }
}
