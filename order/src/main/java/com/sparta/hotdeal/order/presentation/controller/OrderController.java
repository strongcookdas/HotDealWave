package com.sparta.hotdeal.order.presentation.controller;

import com.sparta.hotdeal.order.application.dtos.ResponseDto;
import com.sparta.hotdeal.order.application.dtos.order.req.ReqPatchOrderDto;
import com.sparta.hotdeal.order.application.dtos.order.req.ReqPostOrderDto;
import com.sparta.hotdeal.order.application.dtos.order.res.OrderResponseMessage;
import com.sparta.hotdeal.order.application.dtos.order.res.ResGetOrderByIdDto;
import com.sparta.hotdeal.order.application.dtos.order.res.ResGetOrderListDto;
import com.sparta.hotdeal.order.application.dtos.order.res.ResPostOrderDto;
import com.sparta.hotdeal.order.application.service.order.OrderService;
import com.sparta.hotdeal.order.infrastructure.custom.RequestUserDetails;
import io.swagger.v3.oas.annotations.Operation;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.annotation.Secured;
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
    @Operation(summary = "주문 생성 API", description = "주문을 생성합니다.")
    public ResponseDto<ResPostOrderDto> createOrder(@AuthenticationPrincipal RequestUserDetails userDetails,
                                                    @RequestBody ReqPostOrderDto req) {

        return ResponseDto.of(OrderResponseMessage.CREATE_ORDER.getMessage(),
                orderService.createOrder(userDetails.getUserId(), userDetails.getEmail(), userDetails.getRole(), req));
    }

    @GetMapping
    @Operation(summary = "주문 내역 조회 API", description = "주문 내역을 조회합니다.")
    public ResponseDto<Page<ResGetOrderListDto>> getOrders(@AuthenticationPrincipal RequestUserDetails userDetails,
                                                           @PageableDefault(size = 10) Pageable pageable) {
        return ResponseDto.of("주문 목록 조회 성공", orderService.getOrderList(userDetails.getUserId(), pageable));
    }

    @GetMapping("/{orderId}")
    @Operation(summary = "주문 상세 조회 API", description = "주문 상세 내역을 조회합니다.")
    public ResponseDto<ResGetOrderByIdDto> getOrderById(@AuthenticationPrincipal RequestUserDetails userDetails,
                                                        @PathVariable UUID orderId) {
        return ResponseDto.of("주문 상세 조회 성공",
                orderService.getOrderDetail(userDetails.getUserId(), userDetails.getEmail(), userDetails.getRole(),
                        orderId));
    }

    @PatchMapping("/{orderId}/status")
    @Secured({"ROLE_MASTER"})
    @Operation(summary = "주문 상태 수정 API", description = "주문 상태를 수정합니다. (추후 구현 예정)")
    public ResponseDto<Void> updateOrderStatus(@PathVariable UUID orderId, @RequestBody ReqPatchOrderDto req) {
        return ResponseDto.of("주문 상태가 수정되었습니다.", null);
    }

    @DeleteMapping("/{orderId}")
    @Operation(summary = "주문 취소 API", description = "주문을 취소합니다. (추후 구현 예정)")
    public ResponseDto<Void> deleteOrder(@PathVariable UUID orderId) {
        return ResponseDto.of("주문 취소가 처리 되었습니다.", null);
    }
}
