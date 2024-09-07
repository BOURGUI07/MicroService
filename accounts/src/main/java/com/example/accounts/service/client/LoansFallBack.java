package com.example.accounts.service.client;

import com.example.accounts.dto.LoanResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class LoansFallBack implements LoansFeignClient{
    @Override
    public ResponseEntity<List<LoanResponse>> findByPhone(String phone) {
        return null;
    }
}
