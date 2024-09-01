package com.example.loans.repo;

import com.example.loans.entity.Loans;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LoansRepo extends JpaRepository<Loans, Integer> {
    boolean existsByLoanNumber(String loanNumber);
    List<Loans> findByPhone(String phone);
    @Transactional
    @Modifying
    void deleteByLoanNumber(String loanNumber);
}
