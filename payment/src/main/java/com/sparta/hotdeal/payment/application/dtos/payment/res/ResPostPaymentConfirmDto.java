package com.sparta.hotdeal.payment.application.dtos.payment.res;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ResPostPaymentConfirmDto {
    private String aid;
    private String tid;
    private String cid;
    private String partnerOrderId;
    private String partnerUserId;
    private String paymentMethodType;
    private Amount amount;
    private String itemName;
    private String itemCode;
    private Integer quantity;
    private LocalDateTime createdAt;
    private LocalDateTime approvedAt;
    private String payload;

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Amount {
        private Integer total;
        private Integer taxFree;
        private Integer vat;
        private Integer point;
        private Integer discount;
    }

    // 더미 데이터 생성 메서드
    public static ResPostPaymentConfirmDto createDummy() {
        return ResPostPaymentConfirmDto.builder()
                .aid("A1234567890")
                .tid("T1234567890")
                .cid("C1234567890")
                .partnerOrderId("ORDER1234")
                .partnerUserId("USER1234")
                .paymentMethodType("CARD")
                .amount(Amount.builder()
                        .total(50000)
                        .taxFree(0)
                        .vat(5000)
                        .point(0)
                        .discount(0)
                        .build())
                .itemName("Laptop")
                .itemCode("ITEM1234")
                .quantity(1)
                .createdAt(LocalDateTime.of(2024, 11, 18, 12, 0, 0))
                .approvedAt(LocalDateTime.of(2024, 11, 18, 12, 5, 0))
                .payload("Custom payload data")
                .build();
    }
}
