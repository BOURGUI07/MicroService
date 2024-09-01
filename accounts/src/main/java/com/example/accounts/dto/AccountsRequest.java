package com.example.accounts.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record AccountsRequest(
        @NotNull(message="customer id is mandatory") @Positive(message="id should be positive")
        Integer customerId,
        @NotBlank(message="account type is mandatory")
        String accountType,
        @NotBlank(message="account type is mandatory")
        String branchAddress

) {
}
