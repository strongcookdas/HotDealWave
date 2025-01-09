package com.sparta.hotdeal.order.presentation.controller;

import com.sparta.hotdeal.order.application.dtos.ResponseDto;
import com.sparta.hotdeal.order.application.dtos.order.req.ReqPatchOrderDto;
import com.sparta.hotdeal.order.application.dtos.order.req.ReqPostOrderDto;
import com.sparta.hotdeal.order.application.dtos.order.res.OrderResponseMessage;
import com.sparta.hotdeal.order.application.dtos.order.res.ResGetOrderByIdDto;
import com.sparta.hotdeal.order.application.dtos.order.res.ResGetOrderListDto;
import com.sparta.hotdeal.order.application.service.order.OrderService;
import com.sparta.hotdeal.order.infrastructure.custom.RequestUserDetails;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/orders")
public class OrderController {

    private final OrderService orderService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseDto<Void> createOrder(@AuthenticationPrincipal RequestUserDetails userDetails,
                                         @RequestBody ReqPostOrderDto req) {

        log.info("userId : {}", userDetails.getUserId());
        log.info("userEmail : {}", userDetails.getEmail());
        log.info("userRole : {}", userDetails.getRole());
        orderService.createOrder(userDetails.getUserId(), req);
        return ResponseDto.of(OrderResponseMessage.CREATE_ORDER.getMessage(), null);
    }

    @GetMapping
    public ResponseDto<Page<ResGetOrderListDto>> getOrders(@AuthenticationPrincipal RequestUserDetails userDetails,
                                                           Pageable pageable) {
        log.info("userId : {}", userDetails.getUserId());
        log.info("userEmail : {}", userDetails.getEmail());
        log.info("userRole : {}", userDetails.getRole());
        return ResponseDto.of("주문 목록 조회 성공", orderService.getOrderList(userDetails.getUserId(), pageable));
    }

    @GetMapping("/{orderId}")
    public ResponseDto<ResGetOrderByIdDto> getOrderById(@AuthenticationPrincipal RequestUserDetails userDetails,
                                                        @PathVariable UUID orderId) {
        log.info("userId : {}", userDetails.getUserId());
        log.info("userEmail : {}", userDetails.getEmail());
        log.info("userRole : {}", userDetails.getRole());
        return ResponseDto.of("주문 상세 조회 성공", orderService.getOrderDetail(userDetails.getUserId(), orderId));
    }

    @PatchMapping("/{orderId}/status")
    public ResponseDto<Void> updateOrderStatus(@PathVariable UUID orderId, @RequestBody ReqPatchOrderDto req) {
        log.info("orderId : {}, ReqPatchOrderDto : {}", orderId, req);
        return ResponseDto.of("주문 상태가 수정되었습니다.", null);
    }

    @DeleteMapping("/{orderId}")
    public ResponseDto<Void> deleteOrder(@PathVariable UUID orderId) {
        log.info("orderId : {}", orderId);
        return ResponseDto.of("주문 취소가 처리 되었습니다.", null);
    }
}
