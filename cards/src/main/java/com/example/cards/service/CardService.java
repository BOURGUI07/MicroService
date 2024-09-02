package com.example.cards.service;

import com.example.cards.dto.CardRequest;
import com.example.cards.dto.CardResponse;
import com.example.cards.exception.EntityNotFoundException;
import com.example.cards.exception.OptimisticLockException;
import com.example.cards.mapper.CardMapper;
import com.example.cards.repo.CardsRepo;
import jakarta.persistence.EntityExistsException;
import jakarta.transaction.Transactional;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validator;
import jakarta.validation.constraints.Pattern;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Setter
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true,level= AccessLevel.PRIVATE)
public class CardService {
    CardMapper mapper;
    CardsRepo repo;
    Validator validator;

    public List<CardResponse> findByPhone(
            @Pattern(regexp = "(^$|[0-9]{10})",message = "Mobile Number must be 10 digits")
            String phone) {
        var violations = validator.validate(phone);
        if (!violations.isEmpty()) {
            throw new ConstraintViolationException(violations);
        }
        return repo.findByPhone(phone).stream().map(mapper::toDto).toList();
    }

    @Transactional
    public CardResponse create(CardRequest request) {
        var violations = validator.validate(request);
        if (!violations.isEmpty()) {
            throw new ConstraintViolationException(violations);
        }
        if(repo.existsByCardNumber(request.cardNumber())){
            throw new EntityExistsException("card with cardNumber:  " + request.cardNumber() + "already exists");
        }
        var card = mapper.toEntity(request);
        var savedCard = repo.save(card);
        return mapper.toDto(savedCard);
    }

    @Transactional
    public CardResponse update(
        Integer id, CardRequest x) {
        var violations = validator.validate(x);
        if (!violations.isEmpty()) {
            throw new ConstraintViolationException(violations);
        }
        if(id==null|| id<1){
            throw new IllegalArgumentException("id should be greater than 0");
        }
        var card = repo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("loan with id:  " + id + " doesn't exists"))
                .setCardNumber(x.cardNumber())
                .setAmountUsed(x.amountUsed())
                .setTotalLimit(x.totalLimit())
                .setCardType(x.cardType())
                .setPhone(x.phone())
                .setAvailableAmount(x.availableAmount());
        try{
            var savedCard = repo.save(card);
            return mapper.toDto(savedCard);
        }catch(OptimisticLockingFailureException ex){
            throw new OptimisticLockException("this card was already updated by another user");
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
