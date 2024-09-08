package com.example.accounts.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;
import java.util.Optional;

@Schema(name="Customer Details Response",description = "Schema to hold Accounts, Loans, Cards details for customer")
public record CustomerDetailsResponse(

        CustomerResponse customerResponse,
        Optional<List<CardResponse>> cardResponse,
        Optional<List<LoanResponse>> loanResponse
) {

}
