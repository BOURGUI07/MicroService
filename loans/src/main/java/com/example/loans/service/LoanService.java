package com.example.loans.service;

import com.example.loans.dto.LoanRequest;
import com.example.loans.dto.LoanResponse;
import com.example.loans.exception.EntityNotFoundException;
import com.example.loans.exception.OptimisticLockException;
import com.example.loans.mapper.LoanMapper;
import com.example.loans.repo.LoansRepo;
import jakarta.persistence.EntityExistsException;
import jakarta.transaction.Transactional;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validator;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Setter
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true,level= AccessLevel.PRIVATE)
public class LoanService {
    LoanMapper mapper;
    LoansRepo repo;
    @NonFinal
    Validator validator;

    public List<LoanResponse> findByPhone(
            @Pattern(regexp = "(^$|[0-9]{10})",message = "Mobile Number must be 10 digits")
            String phone) {
        var violations = validator.validate(phone);
        if (!violations.isEmpty()) {
            throw new ConstraintViolationException(violations);
        }
        return repo.findByPhone(phone).stream().map(mapper::toDto).toList();
    }

    @Transactional
    public LoanResponse create(LoanRequest request) {
        var violations = validator.validate(request);
        if (!violations.isEmpty()) {
            throw new ConstraintViolationException(violations);
        }
        if(repo.existsByLoanNumber(request.loanNumber())){
            throw new EntityExistsException("loan with loanNumber:  " + request.loanNumber() + "already exists");
        }
        var loan = mapper.toEntity(request);
        var savedLoan = repo.save(loan);
        return mapper.toDto(savedLoan);
    }

    @Transactional
    public LoanResponse update(
        Integer id, LoanRequest request) {
        var violations = validator.validate(request);
        if (!violations.isEmpty()) {
            throw new ConstraintViolationException(violations);
        }
        if(id==null|| id<1){
            throw new IllegalArgumentException("id should be greater than 0");
        }
        var loan = repo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("loan with id:  " + id + " doesn't exists"))
                .setLoanNumber(request.loanNumber())
                .setLoanTotal(request.loanTotal())
                .setLoanType(request.loanType())
                .setPhone(request.phone())
                .setAmountPaid(request.amountPaid())
                .setOutstandingAmount(request.outstandingAmount());
        try{
            var savedLoan = repo.save(loan);
            return mapper.toDto(savedLoan);
        }catch(OptimisticLockingFailureException ex){
            throw new OptimisticLockException("this loan was already updated by another user");
        }
    }

    @Transactional
    public void deleteById(Integer id) {
        if(id==null|| id<1){
            throw new IllegalArgumentException("id should be greater than 0");
        }
        repo.findById(id).ifPresent(repo::delete);
    }

}
