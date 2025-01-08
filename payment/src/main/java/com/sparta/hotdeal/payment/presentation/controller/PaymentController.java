package com.sparta.hotdeal.payment.presentation.controller;

import com.sparta.hotdeal.payment.application.dtos.ResponseDto;
import com.sparta.hotdeal.payment.application.dtos.payment.req.ReqPostPaymentConfirmDto;
import com.sparta.hotdeal.payment.application.dtos.payment.req.ReqPostPaymentDto;
import com.sparta.hotdeal.payment.application.dtos.payment.res.ResGetPaymentByIdDto;
import com.sparta.hotdeal.payment.application.dtos.payment.res.ResGetPaymentForListDto;
import com.sparta.hotdeal.payment.application.dtos.payment.res.ResPostPaymentConfirmDto;
import com.sparta.hotdeal.payment.application.dtos.payment.res.ResPostPaymentsDto;
import com.sparta.hotdeal.payment.application.service.PaymentService;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/payments")
public class PaymentController {
    private final PaymentService paymentService;

    @PostMapping
    public ResponseDto<ResPostPaymentsDto> readyPayment(@RequestBody ReqPostPaymentDto req) {
        return ResponseDto.of("결제 요청이 처리되었습니다.", paymentService.readyPayment(UUID.randomUUID(), req));
    }

    @PostMapping("/confirm")
    public ResponseDto<ResPostPaymentConfirmDto> confirmPayment(@RequestBody ReqPostPaymentConfirmDto req) {
        return ResponseDto.of("결제 승인 처리되었습니다.", paymentService.approvePayment(UUID.randomUUID(), req));
    }

    @GetMapping
    public ResponseDto<Page<ResGetPaymentForListDto>> getPayments(Pageable pageable) {
        return ResponseDto.of("결제 내역 조회 성공", paymentService.getPaymentList(UUID.randomUUID(), pageable));
    }

    @GetMapping("/{paymentId}")
    public ResponseDto<ResGetPaymentByIdDto> getPaymentById(@PathVariable UUID paymentId) {
        return ResponseDto.of("결제 단건 조회 성공", paymentService.getPaymentById(UUID.randomUUID(), paymentId));
    }

    @DeleteMapping("/{paymentId}")
    public ResponseDto<Void> deletePayment(@PathVariable UUID paymentId) {// 추후 구현
        return ResponseDto.of("결제 환불 처리되었습니다.", null);
    }
}
