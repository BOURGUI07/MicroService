package com.example.accounts.dto;

public record AccountsMsgDTO(
        Integer accountNumber,
        String name,
        String email,
        String phone
) {
}
