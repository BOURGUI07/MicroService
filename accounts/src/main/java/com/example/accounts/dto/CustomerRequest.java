package com.example.accounts.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CustomerRequest(
        @NotBlank(message = "Customer name is mandatory")
        String name,
        @Email(message = "Customer email isn't valid")
        @NotNull
        String email,
        @NotBlank(message = "Customer phone is mandatory")
        String phone,
        @NotBlank(message="account type is mandatory")
        String accountType,
        @NotBlank(message="account type is mandatory")
        String branchAddress

) {
}
