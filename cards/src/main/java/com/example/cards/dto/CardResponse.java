package com.example.cards.dto;


public record CardResponse(
        Integer cardId,

        String phone,

        String cardNumber,

        String cardType,

        Integer limitAmount,

        Integer amountUsed,

        Integer availableAmount,

        Integer version
) {
}
