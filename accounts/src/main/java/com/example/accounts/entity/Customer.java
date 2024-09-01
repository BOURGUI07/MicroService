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
@Table(name="CUSTOMER")
@NoArgsConstructor
@DynamicInsert
@DynamicUpdate
@Data
@Accessors(chain=true)
@EqualsAndHashCode(callSuper=false)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Customer extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="customer_id")
    Integer id;

    @Column(name="name",nullable=false,unique=true)
    String name;
    @Column(name="email",nullable=false,unique=true)
    String email;

    @Column(name="mobile_number",nullable=false,unique=true)
    String phone;

}
