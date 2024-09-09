package com.example.message.functions;

import com.example.message.dto.AccountsMsgDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.function.Function;

@Configuration
@Slf4j
public class MessageFunctions {

    @Bean
    public Function<AccountsMsgDTO,AccountsMsgDTO> email() {
        return x->{
          log.info("Sending email with details {}", x.toString());
          return x;
        };
    }

    @Bean
    public Function<AccountsMsgDTO,Integer> sms() {
        return x->{
            log.info("Sending sms with details {}", x.toString());
            return x.accountNumber();
        };
    }
}
