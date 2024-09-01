package com.example.loans.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.validation.constraints.*;

@Schema(name="LoanRequest",description = "parameters to create/update a Loan entity")
@JsonIgnoreProperties(ignoreUnknown = true)
public record LoanRequest(
        @Pattern(regexp = "(^$|[0-9]{10})",message = "Mobile Number must be 10 digits")
        String phone,

        @NotBlank(message = "phone number is mandatory")
        String loanNumber,

        @NotBlank(message = "phone number is mandatory")
        String loanType,

        @NotNull
        @Positive(message = "loan total should be positive")
        Integer loanTotal,

        @NotNull
        @PositiveOrZero(message = "amount paid should be greater or equals than zero")
        Integer amountPaid,

        @NotNull
        @PositiveOrZero(message = "outstanding amount should be greater or equals than zero")
        Integer outstandingAmount
) {
}
