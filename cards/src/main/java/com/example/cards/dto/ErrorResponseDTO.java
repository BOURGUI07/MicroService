package com.example.cards.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.http.HttpStatus;

@Schema(name = "ErrorResponseDTO", description = "schema to hold error response fields")
public record ErrorResponseDTO(
        String apiPath,
        HttpStatus httpStatus,
        Integer statusCode,
        String errorMessage,
        long timeStamp
) {
}
