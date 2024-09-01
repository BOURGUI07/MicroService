package com.example.loans.entity;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@MappedSuperclass
@Data
@EntityListeners(AuditingEntityListener.class)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BaseEntity {
    @CreatedDate
    @Column(updatable = false)
    LocalDateTime createdAt;
    @CreatedBy
    @Column(updatable = false)
    String createdBy;
    @LastModifiedDate
    @Column(insertable = false)
    LocalDateTime updatedAt;
    @LastModifiedBy
    @Column(insertable = false)
    String updatedBy;


}
