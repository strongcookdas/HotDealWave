package com.sparta.hotdeal.user.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
@Table(name = "p_email")
public class Email {
    @Id
    private String email;

    @Column(nullable = false, name = "is_verified")
    private boolean verified;

    public void updateVerified() {
        this.verified = true;
    }
}
