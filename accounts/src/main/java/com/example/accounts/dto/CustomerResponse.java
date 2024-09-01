package com.example.accounts.dto;

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
