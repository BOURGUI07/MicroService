package com.example.loans.mapper;

import com.example.loans.dto.LoanRequest;
import com.example.loans.dto.LoanResponse;
import com.example.loans.entity.Loans;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LoanMapper {
    public Loans toEntity(LoanRequest x) {
        return new Loans()
                .setLoanNumber(x.loanNumber())
                .setAmountPaid(x.amountPaid())
                .setLoanTotal(x.loanTotal())
                .setLoanType(x.loanType())
                .setPhone(x.phone())
                .setOutstandingAmount(x.outstandingAmount());
    }

    public LoanResponse toDto(Loans x) {
        return new LoanResponse(x.getId(), x.getPhone(),x.getLoanNumber(), x.getLoanType(), x.getLoanTotal(), x.getAmountPaid(), x.getOutstandingAmount(),x.getVersion());
    }
}
