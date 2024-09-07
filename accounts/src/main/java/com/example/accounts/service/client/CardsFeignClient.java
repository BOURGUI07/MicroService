package com.example.accounts.service.client;

import com.example.accounts.dto.CardResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(name="cards",fallback = CardsFallBack.class)
public interface CardsFeignClient {
    @GetMapping("/api/cards")
    public ResponseEntity<List<CardResponse>> findByPhone(@RequestParam String phone);
}
