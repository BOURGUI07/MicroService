package com.example.accounts.repo;

import com.example.accounts.entity.Accounts;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AccountsRepo extends JpaRepository<Accounts, Integer> {
    Optional<Accounts> findByCustomerId(Integer customerId);
    Optional<Accounts> findByCustomerPhone(String phone);
    @Transactional
    @Modifying
    void deleteByCustomerId(Integer customerId);
    @Transactional
    @Modifying
    void deleteByCustomerPhone(String phone);
}
