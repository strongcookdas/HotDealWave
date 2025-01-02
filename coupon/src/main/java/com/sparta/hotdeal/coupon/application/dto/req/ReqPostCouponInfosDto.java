package com.sparta.hotdeal.coupon.application.dto.req;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.UUID;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ReqPostCouponInfosDto {
    private int quantity;
    private String name;
    private String couponType;
    private int discountAmount;
    private int minOrderAmount;
    private LocalDate expirationDate;
    private UUID companyId;
}
