package com.example.accounts.repo;

import com.example.accounts.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CustomerRepo extends JpaRepository<Customer,Integer> {
    boolean existsByNameOrEmailOrPhone(String name,String email, String phone);
    Optional<Customer> findByPhone(String phone);
}
