package com.sparta.hotdeal.payment.presentation.controller;

import com.sparta.hotdeal.payment.application.dtos.ResponseDto;
import com.sparta.hotdeal.payment.application.dtos.payment.req.ReqPostPaymentConfirmDto;
import com.sparta.hotdeal.payment.application.dtos.payment.req.ReqPostPaymentDto;
import com.sparta.hotdeal.payment.application.dtos.payment.res.ResGetPaymentByIdDto;
import com.sparta.hotdeal.payment.application.dtos.payment.res.ResGetPaymentForListDto;
import com.sparta.hotdeal.payment.application.dtos.payment.res.ResPostPaymentCancelDto;
import com.sparta.hotdeal.payment.application.dtos.payment.res.ResPostPaymentConfirmDto;
import com.sparta.hotdeal.payment.application.dtos.payment.res.ResPostPaymentRefundDto;
import com.sparta.hotdeal.payment.application.dtos.payment.res.ResPostPaymentsDto;
import com.sparta.hotdeal.payment.application.service.PaymentService;
import com.sparta.hotdeal.payment.infrastructure.custom.RequestUserDetails;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/payments")
public class PaymentController {
    private final PaymentService paymentService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "결제 요청 API", description = "결제를 요청합니다.")
    public ResponseDto<ResPostPaymentsDto> readyPayment(@AuthenticationPrincipal RequestUserDetails userDetails,
                                                        @RequestBody @Valid ReqPostPaymentDto req) {
        return ResponseDto.of("결제 요청이 처리되었습니다.", paymentService.readyPayment(userDetails.getUserId(), req));
    }
    @PostMapping("/confirm")
    @Operation(summary = "결제 승인 API", description = "결제를 승인합니다.")
    public ResponseDto<ResPostPaymentConfirmDto> confirmPayment(@AuthenticationPrincipal RequestUserDetails userDetails,
                                                                @RequestBody @Valid ReqPostPaymentConfirmDto req) {
        return ResponseDto.of("결제 승인 처리되었습니다.", paymentService.approvePayment(userDetails.getUserId(), req));
    }

    @GetMapping
    @Operation(summary = "결제 목록 조회 API", description = "결제 목록을 조회합니다.")
    public ResponseDto<Page<ResGetPaymentForListDto>> getPayments(
            @AuthenticationPrincipal RequestUserDetails userDetails,
            Pageable pageable
    ) {
        return ResponseDto.of("결제 내역 조회 성공", paymentService.getPaymentList(userDetails.getUserId(), pageable));
    }

    @GetMapping("/{paymentId}")
    @Operation(summary = "결제 내역 조회 API", description = "결제 내역을 조회합니다.")
    public ResponseDto<ResGetPaymentByIdDto> getPaymentById(@AuthenticationPrincipal RequestUserDetails userDetails,
                                                            @PathVariable UUID paymentId) {
        return ResponseDto.of("결제 단건 조회 성공", paymentService.getPaymentById(userDetails.getUserId(), paymentId));
    }

    @PostMapping("/refund")
    @Operation(summary = "결제 환불 API", description = "결제를 환불합니다.")
    public ResponseDto<ResPostPaymentRefundDto> refundPayment(@AuthenticationPrincipal RequestUserDetails userDetails,
                                                              @RequestParam UUID orderId) {
        return ResponseDto.of("결제 환불 처리되었습니다.", paymentService.refundPayment(userDetails.getUserId(), orderId));
    }

    @PostMapping("/cancel")
    @Operation(summary = "결제 취소 API", description = "결제를 취소합니다.")
    public ResponseDto<ResPostPaymentCancelDto> cancelPayment(@AuthenticationPrincipal RequestUserDetails userDetails,
                                                              @RequestParam UUID orderId) {
        return ResponseDto.of("결제 취소 처리되었습니다.", paymentService.cancelPayment(userDetails.getUserId(), orderId));
    }
}
