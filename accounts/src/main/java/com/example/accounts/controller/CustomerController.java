package com.example.accounts.controller;

import com.example.accounts.dto.CustomerRequest;
import com.example.accounts.dto.CustomerResponse;
import com.example.accounts.service.CustomerService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path="/api/customers",produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true,level = AccessLevel.PRIVATE)
public class CustomerController {
    CustomerService customerService;

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CustomerResponse> createAccount(@RequestBody CustomerRequest customerRequest) {
        return ResponseEntity.status(HttpStatus.CREATED).body(customerService.create(customerRequest));
    }

    @PutMapping(value="/{customerId}",consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CustomerResponse> updateAccount(
            @PathVariable Integer customerId,
            @RequestBody CustomerRequest customerRequest) {

        return ResponseEntity.status(HttpStatus.OK).body(customerService.update(customerId,customerRequest));
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteAccount(@RequestParam String phone) {
        customerService.deleteByPhone(phone);
        return ResponseEntity.noContent().build();
    }
}
