package com.sparta.hotdeal.product.infrastructure.dtos;

import java.util.UUID;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ResGetCompanyByIdDto {
    private Long businessRegistrationNumber;
    private Long companyPhoneNumber;
    private UUID managerId;
    private String brandName;
    private String companyEmail;
    private String status;

    // 더미 데이터를 반환하는 정적 메서드
    public static ResGetCompanyByIdDto createDummy() {
        return ResGetCompanyByIdDto.builder()
                .businessRegistrationNumber(1L)
                .companyPhoneNumber(1L)
                .managerId(UUID.randomUUID())
                .brandName("Test")
                .companyEmail("Test")
                .status("Test")
                .build();
    }
}
