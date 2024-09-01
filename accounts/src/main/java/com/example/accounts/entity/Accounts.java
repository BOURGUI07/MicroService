package com.example.accounts.entity;

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
@Table(name="ACCOUNTS")
@NoArgsConstructor
@DynamicInsert
@DynamicUpdate
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@EqualsAndHashCode(callSuper=false)
@Accessors(chain=true)
public class Accounts extends BaseEntity {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name="account_number")
    Integer accountNumber;

    @OneToOne(cascade=CascadeType.ALL)
    @JoinColumn(name = "customer_id",nullable = false,unique = true)
    Customer customer;


    @Column(name="account_type")
    String accountType;
    @Column(name="branch_address")
    String branchAddress;

}
