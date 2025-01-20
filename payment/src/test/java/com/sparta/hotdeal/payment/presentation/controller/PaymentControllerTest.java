package com.sparta.hotdeal.payment.presentation.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
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
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;

@WebMvcTest(PaymentController.class)
class PaymentControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private PaymentService paymentService;

    @BeforeEach
    void setUp(WebApplicationContext context) {
        mockMvc = MockMvcBuilders.webAppContextSetup(context)
                .addFilter(new CharacterEncodingFilter("UTF-8", true))
                .build();

        // Spring Security 컨텍스트에 사용자 정보 설정
        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken(
                        new RequestUserDetails(
                                UUID.randomUUID().toString(), // userId
                                "test@example.com",                       // email
                                List.of(new SimpleGrantedAuthority("ROLE_MASTER")) // 권한
                        ),
                        null,
                        List.of(new SimpleGrantedAuthority("ROLE_MASTER"))
                )
        );
    }

    @Test
    @DisplayName("결제 요청 API 성공 테스트")
    void readyPayment() throws Exception {
        ReqPostPaymentDto req = ReqPostPaymentDto.builder()
                .orderId(UUID.randomUUID())
                .orderName("orderName")
                .quantity(1)
                .totalAmount(10000)
                .build();

        ResPostPaymentsDto res = ResPostPaymentsDto.builder()
                .tid("123")
                .build();

        when(paymentService.readyPayment(any(), any())).thenReturn(res);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/payments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isCreated());
    }

    @Test
    @DisplayName("결제 승인 API 성공 테스트")
    void confirmPayment() throws Exception {
        ReqPostPaymentConfirmDto req = ReqPostPaymentConfirmDto.builder()
                .tid("tid")
                .pgToken("pgToken")
                .build();

        ResPostPaymentConfirmDto res = ResPostPaymentConfirmDto.builder()
                .tid("tid")
                .build();

        when(paymentService.approvePayment(any(), any())).thenReturn(res);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/payments/confirm")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("결제 목록 조회 API 성공 테스트")
    void getPayments() throws Exception {
        ResGetPaymentForListDto payment = ResGetPaymentForListDto.builder()
                .paymentId(UUID.randomUUID())
                .build();

        Page<ResGetPaymentForListDto> page = new PageImpl<>(Collections.singletonList(payment), PageRequest.of(0, 10),
                1);

        when(paymentService.getPaymentList(any(), any())).thenReturn(page);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/payments")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("결제 단건 조회 API 성공 테스트")
    void getPaymentById() throws Exception {
        UUID paymentId = UUID.randomUUID();

        ResGetPaymentByIdDto res = ResGetPaymentByIdDto.builder()
                .paymentId(paymentId)
                .build();

        when(paymentService.getPaymentById(any(), any())).thenReturn(res);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/payments/{paymentId}", paymentId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("결제 환불 API 성공 테스트")
    void refundPayment() throws Exception {
        UUID orderId = UUID.randomUUID();

        ResPostPaymentRefundDto res = ResPostPaymentRefundDto.builder()
                .tid("tid")
                .build();

        when(paymentService.refundPayment(any(), any())).thenReturn(res);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/payments/refund")
                        .param("orderId", orderId.toString())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("결제 취소 API 성공 테스트")
    void cancelPayment() throws Exception {
        UUID orderId = UUID.randomUUID();

        ResPostPaymentCancelDto res = ResPostPaymentCancelDto.builder()
                .orderId(UUID.randomUUID())
                .build();

        when(paymentService.cancelPayment(any(), any())).thenReturn(res);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/payments/cancel")
                        .param("orderId", orderId.toString())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
}