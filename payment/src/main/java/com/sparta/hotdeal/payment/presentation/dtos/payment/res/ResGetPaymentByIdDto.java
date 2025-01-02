package com.sparta.hotdeal.payment.presentation.dtos.payment.res;

import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ResGetPaymentByIdDto {
    private UUID paymentId;
    private UUID orderId;
    private String paymentStatus;
    private Integer paymentAmount;

    public static ResGetPaymentByIdDto createDummyData(UUID paymentId) {
        return ResGetPaymentByIdDto.builder()
                .paymentId(paymentId)
                .orderId(UUID.randomUUID())
                .paymentStatus("COMPLETE")
                .paymentAmount(10000)
                .build();
    }
}
