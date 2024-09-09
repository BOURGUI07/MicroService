package com.example.message.dto;

public record AccountsMsgDTO(
        Integer accountNumber,
        String name,
        String email,
        String phone
) {
}
