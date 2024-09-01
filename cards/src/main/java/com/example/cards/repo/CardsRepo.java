package com.example.cards.repo;

import com.example.cards.entity.Cards;
import com.example.loans.entity.Loans;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CardsRepo extends JpaRepository<Cards, Integer> {
    boolean existsByCardNumber(String loanNumber);
    List<Cards> findByPhone(String phone);
    @Transactional
    @Modifying
    void deleteByCardNumber(String loanNumber);
}
