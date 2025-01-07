package com.sparta.hotdeal.payment.application.dtos.payment.res;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ResGetPaymentsDto {
    private UUID paymentId;
    private UUID orderId;
    private String paymentStatus;
    private Integer paymentAmount;

    // 더미 데이터 생성 메서드
    public static List<ResGetPaymentsDto> createDummyList() {
        List<ResGetPaymentsDto> dummyPayments = new ArrayList<>();

        for (int i = 1; i <= 3; i++) {
            ResGetPaymentsDto payment = ResGetPaymentsDto.builder()
                    .paymentId(UUID.randomUUID())
                    .orderId(UUID.randomUUID())
                    .paymentStatus("COMPLETED")
                    .paymentAmount(20000 * i)
                    .build();
            dummyPayments.add(payment);
        }

        return dummyPayments;
    }
}