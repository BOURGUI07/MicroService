package com.example.loans.entity;

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
@Table(name="LOANS")
@NoArgsConstructor
@DynamicInsert
@DynamicUpdate
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@EqualsAndHashCode(callSuper=false)
@Accessors(chain=true)
public class Loans extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="loan_id")
    Integer id;


    @Column(name="mobile_number",nullable=false,length=15)
    String phone;

    @Column(name="loan_number",nullable=false,length=100,unique=true)
    String loanNumber;

    @Column(name="loan_type",nullable=false,length=100)
    String loanType;

    @Column(name="total_loan",nullable=false)
    Integer loanTotal;

    @Column(name="amount_paid",nullable=false)
    Integer amountPaid;

    @Column(name="outstanding_amount",nullable = false)
    Integer outstandingAmount;

    @Version
    Integer version;

}
