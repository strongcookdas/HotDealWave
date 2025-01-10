package com.sparta.hotdeal.order.infrastructure.client;

import com.sparta.hotdeal.order.application.dtos.ResponseDto;
import com.sparta.hotdeal.order.infrastructure.config.PaymentClientConfig;
import com.sparta.hotdeal.order.infrastructure.dtos.payment.req.ReqPostPaymentDto;
import com.sparta.hotdeal.order.infrastructure.dtos.payment.res.ResPostPaymentsDto;
import java.util.UUID;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "payment-service", configuration = PaymentClientConfig.class)
public interface PaymentClient {
    @PostMapping(value = "/api/v1/payments")
    ResponseDto<ResPostPaymentsDto> readyPayment(@RequestHeader("X-User-UserId") UUID userId,
                                                 @RequestHeader("X-User-Email") String email,
                                                 @RequestHeader("X-User-Role") String role,
                                                 @RequestBody ReqPostPaymentDto reqPostPaymentDto);
}
