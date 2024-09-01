package com.example.accounts.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.http.HttpStatus;

@Schema(title = "ErrorResponseDTO", description = "schema to hold error response")
public record ErrorResponseDTO(
        String apiPath,
        HttpStatus statusCode,
        String message,
        long timeStamp
) {
}
