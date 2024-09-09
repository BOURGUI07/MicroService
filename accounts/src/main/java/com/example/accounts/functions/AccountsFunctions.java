package com.example.accounts.functions;

import com.example.accounts.service.CustomerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.function.Consumer;

@Configuration
@Slf4j
public class AccountsFunctions {

    @Bean
    public Consumer<Integer> updateCommunication(CustomerService customerService) {
        return accountNumber -> {
          log.info("updateCommunication status for account number {}", accountNumber.toString());
          customerService.updateCommunicationStatus(accountNumber);
        };
    }
}
