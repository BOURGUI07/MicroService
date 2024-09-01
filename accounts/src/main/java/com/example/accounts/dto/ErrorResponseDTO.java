package com.example.accounts.dto;

import org.springframework.http.HttpStatus;

public record ErrorResponseDTO(
        String apiPath,
        HttpStatus statusCode,
        String message,
        long timeStamp
) {
}
