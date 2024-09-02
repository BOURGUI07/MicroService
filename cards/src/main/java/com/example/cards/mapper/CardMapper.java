package com.example.cards.mapper;

import com.example.cards.dto.CardRequest;
import com.example.cards.dto.CardResponse;
import com.example.cards.entity.Cards;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CardMapper {
    public Cards toEntity(CardRequest x) {
        return new Cards()
                .setCardNumber(x.cardNumber())
                .setAmountUsed(x.amountUsed())
                .setTotalLimit(x.totalLimit())
                .setCardType(x.cardType())
                .setPhone(x.phone())
                .setAvailableAmount(x.availableAmount());
    }

    public CardResponse toDto(Cards x) {
        return new CardResponse(x.getId(), x.getPhone(),x.getCardNumber(), x.getCardType(), x.getTotalLimit(), x.getAmountUsed(), x.getAvailableAmount(),x.getVersion());
    }
}
