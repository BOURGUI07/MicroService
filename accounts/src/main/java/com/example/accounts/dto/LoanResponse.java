package com.example.accounts.dto;


public record LoanResponse(
        Integer loanId,

        String phone,

        String loanNumber,

        String loanType,

        Integer loanTotal,

        Integer amountPaid,

        Integer outstandingAmount,

        Integer version
) {
}
