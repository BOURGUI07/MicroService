package com.example.accounts.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(title = "CustomerResponse", description = "Parameters of customer/account response")
public record CustomerResponse(
        Integer customerId,
        String name,
        String email,
        String phone,
        Integer accountId,
        String accountType,
        String branchAddress
) {
}
