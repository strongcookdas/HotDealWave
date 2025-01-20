package com.sparta.hotdeal.order.presentation.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sparta.hotdeal.order.application.dtos.order.req.ReqPostOrderDto;
import com.sparta.hotdeal.order.application.dtos.order.req.ReqPutOrderDto;
import com.sparta.hotdeal.order.application.dtos.order.res.ResGetOrderByIdDto;
import com.sparta.hotdeal.order.application.dtos.order.res.ResGetOrderListDto;
import com.sparta.hotdeal.order.application.dtos.order.res.ResPostOrderDto;
import com.sparta.hotdeal.order.application.service.order.OrderService;
import com.sparta.hotdeal.order.domain.entity.order.OrderStatus;
import com.sparta.hotdeal.order.infrastructure.custom.RequestUserDetails;
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

@WebMvcTest(OrderController.class)
class OrderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private OrderService orderService;

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
    @DisplayName("주문 생성 API 성공 테스트")
    void createOrder() throws Exception {
        ReqPostOrderDto req = ReqPostOrderDto.builder()
                .basketList(List.of(UUID.randomUUID()))
                .addressId(UUID.randomUUID())
                .couponId(UUID.randomUUID())
                .build();

        ResPostOrderDto res = ResPostOrderDto.builder()
                .orderId(UUID.randomUUID())
                .build();

        when(orderService.createOrder(any(), any(), any(), any())).thenReturn(res);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isCreated());
    }

    @Test
    @DisplayName("주문 내역 조회 API 성공 테스트")
    void getOrders() throws Exception {
        ResGetOrderListDto order = ResGetOrderListDto.builder()
                .orderId(UUID.randomUUID())
                .status(OrderStatus.CREATE)
                .build();

        Page<ResGetOrderListDto> page = new PageImpl<>(Collections.singletonList(order), PageRequest.of(0, 10), 1);

        when(orderService.getOrderList(any(), any())).thenReturn(page);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/orders")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("주문 상세 조회 API 성공 테스트")
    void getOrderById() throws Exception {
        UUID orderId = UUID.randomUUID();

        ResGetOrderByIdDto res = ResGetOrderByIdDto.builder()
                .orderId(orderId)
                .build();

        when(orderService.getOrderDetail(any(), any(), any(), any())).thenReturn(res);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/orders/{orderId}", orderId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("주문 상태 수정 API 성공 테스트")
    void updateOrderStatus() throws Exception {
        ReqPutOrderDto req = ReqPutOrderDto.builder()
                .orderStatus(OrderStatus.COMPLETE)
                .build();

        mockMvc.perform(MockMvcRequestBuilders.put("/api/v1/orders/{orderId}/status", UUID.randomUUID())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("주문 취소 API 성공 테스트")
    void cancelOrder() throws Exception {
        UUID orderId = UUID.randomUUID();

        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/orders/{orderId}/cancel", orderId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("주문 환불 API 성공 테스트")
    void refundOrder() throws Exception {
        UUID orderId = UUID.randomUUID();

        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/orders/{orderId}/refund", orderId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
}