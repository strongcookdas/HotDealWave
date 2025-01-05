package com.sparta.hotdeal.company.domain.entity.company;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder(access = AccessLevel.PRIVATE)
@Table(name = "p_companies")
public class Company {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private Long businessRegistrationNumber;

    @Column(nullable = false)
    private Long companyPhoneNumber;

    @Column(nullable = false)
    private UUID managerId;

    @Column(nullable = false)
    private String companyEmail;

    @Column(nullable = false)
    private String brandName;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private CompanyStatusEnum status;

    public static Company create(
            Long businessRegistrationNumber,
            Long companyPhoneNumber,
            UUID managerId,
            String companyEmail,
            String brandName,
            CompanyStatusEnum status
    ) {
        return Company.builder()
                .businessRegistrationNumber(businessRegistrationNumber)
                .companyPhoneNumber(companyPhoneNumber)
                .managerId(managerId)
                .companyEmail(companyEmail)
                .brandName(brandName)
                .status(status)
                .build();
    }

    public void update(Long companyPhoneNumber, UUID managerId, String companyEmail, String brandName) {
        this.companyPhoneNumber = companyPhoneNumber;
        this.managerId = managerId;
        this.companyEmail = companyEmail;
        this.brandName = brandName;
    }

    public void updateStatus(CompanyStatusEnum status) {
        this.status = status;
    }
}
