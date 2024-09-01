package com.example.cards.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

@Entity
@Table(name="CARDS")
@NoArgsConstructor
@DynamicInsert
@DynamicUpdate
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@EqualsAndHashCode(callSuper=false)
@Accessors(chain=true)
public class Cards extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="loan_id")
    Integer id;


    @Column(name="mobile_number",nullable=false,length=15)
    String phone;

    @Column(name="card_number",nullable=false,length=100,unique=true)
    String cardNumber;

    @Column(name="card_type",nullable=false,length=100)
    String cardType;

    @Column(name="total_limit",nullable=false)
    Integer totalLimit;

    @Column(name="amount_used",nullable=false)
    Integer amountUsed;

    @Column(name="available_amount",nullable = false)
    Integer availableAmount;

    @Version
    Integer version;

}
