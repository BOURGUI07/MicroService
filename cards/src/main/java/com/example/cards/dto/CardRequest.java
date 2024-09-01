package com.example.cards.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;

@Schema(name="LoanRequest",description = "parameters to create/update a Loan entity")
@JsonIgnoreProperties(ignoreUnknown = true)
public record CardRequest(
        @Pattern(regexp = "(^$|[0-9]{10})",message = "Mobile Number must be 10 digits")
        String phone,

        @NotBlank(message = "card number is mandatory")
        String cardNumber,

        @NotBlank(message = "card type is mandatory")
        String cardType,

        @NotNull
        @Positive(message = "total limit should be positive")
        Integer totalLimit,

        @NotNull
        @PositiveOrZero(message = "amount used should be greater or equals than zero")
        Integer amountUsed,

        @NotNull
        @PositiveOrZero(message = "available amount should be greater or equals than zero")
        Integer availableAmount
) {
}
