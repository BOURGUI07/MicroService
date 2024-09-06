package com.example.accounts.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Schema(name="Customer Details Response",description = "Schema to hold Accounts, Loans, Cards details for customer")
public record CustomerDetailsResponse(

        CustomerResponse customerResponse,
        List<CardResponse> cardResponse,
        List<LoanResponse> loanResponse
) {

}
