package com.sparta.hotdeal.coupon.domain.entity;

import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import java.time.LocalDateTime;
import lombok.Getter;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Getter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public class AuditingDate {

    @CreatedDate
    private LocalDateTime createdAt;

    @CreatedBy
    private String createdBy;

    @LastModifiedDate
    private LocalDateTime updatedAt;

    @LastModifiedBy
    private String updatedBy;

    private LocalDateTime deletedAt;

    private String deletedBy;

    public void delete(String email) {
        this.deletedAt = LocalDateTime.now();
        this.deletedBy = email;
    }

    public void systemDelete() {
        this.deletedAt = LocalDateTime.now();
        this.deletedBy = "SYSTEM";
    }

    public void recover() {
        this.deletedAt = null;
        this.deletedBy = null;
    }
}
