package com.example.accounts.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

@JsonIgnoreProperties(ignoreUnknown=true)
@Schema(title = "CustomerRequest", description = "Parameters required to create/update a customer alongside his account")
public record CustomerRequest(
        @NotBlank(message = "Customer name is mandatory")
        String name,
        @Email(message = "Customer email isn't valid")
        @NotNull
        String email,
        @Pattern(regexp="(^$|[0-9]{10})",message = "Mobile number must be 10 digits")
        String phone,
        @NotBlank(message="account type is mandatory")
        String accountType,
        @NotBlank(message="account type is mandatory")
        String branchAddress

) {
}
