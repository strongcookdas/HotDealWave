package com.sparta.hotdeal.product.application.dtos.req.promotion;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class ReqPostPromotionDto {
    @NotNull(message = "상품을 입력해주세요.")
    private UUID productId;

    @NotNull(message = "타임 세일 시작 시간을 입력해주세요.")
    private LocalDateTime start;

    @NotNull(message = "타임 세일 종료 시간을 입력해주세요.")
    private LocalDateTime end;

    @Positive(message = "가격은 1 이상으로 입력해주세요.")
    @Max(value = 2100000000, message = "가격은 2100000000 이하로 입력해주세요.")
    private Integer discountPrice;

    @Positive(message = "수량은 1 이상으로 입력해주세요.")
    @Max(value = 2100000000, message = "수량은 2100000000 이하로 입력해주세요.")
    private Integer quantity;
}
