package com.example.accounts.service.client;

import com.example.accounts.dto.CardResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CardsFallBack implements CardsFeignClient{
    @Override
    public ResponseEntity<List<CardResponse>> findByPhone(String phone) {
        return null;
    }
}
