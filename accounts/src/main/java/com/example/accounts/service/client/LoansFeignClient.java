package com.example.accounts.service.client;

import com.example.accounts.dto.LoanResponse;
import jakarta.validation.Valid;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
@FeignClient("loans")
public interface LoansFeignClient {
    @GetMapping("/api/loans")
    public ResponseEntity<List<LoanResponse>> findByPhone(@RequestParam String phone);
}
