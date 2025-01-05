package com.sparta.hotdeal.order.presentation.controller;

import com.sparta.hotdeal.order.application.dtos.ResponseDto;
import com.sparta.hotdeal.order.application.dtos.order.req.ReqPatchOrderDto;
import com.sparta.hotdeal.order.application.dtos.order.req.ReqPostOrderDto;
import com.sparta.hotdeal.order.application.dtos.order.res.OrderResponseMessage;
import com.sparta.hotdeal.order.application.dtos.order.res.ResGetOrderByIdDto;
import com.sparta.hotdeal.order.application.dtos.order.res.ResGetOrdersDto;
import com.sparta.hotdeal.order.application.service.order.OrderService;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
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
    public ResponseDto<Void> createOrder(@RequestBody ReqPostOrderDto req) {
        return ResponseDto.of(OrderResponseMessage.CREATE_ORDER.getMessage(), null);
    }

    @GetMapping
    public ResponseDto<Page<ResGetOrdersDto>> getOrders(Pageable pageable) {
        List<ResGetOrdersDto> dummyList = ResGetOrdersDto.createDummyList();

        // 페이징 처리
        int start = (int) pageable.getOffset();
        int end = Math.min(start + pageable.getPageSize(), dummyList.size());
        Page<ResGetOrdersDto> page = new PageImpl<>(dummyList.subList(start, end), pageable, dummyList.size());

        return ResponseDto.of("주문 목록 조회 성공", page);
    }

    @GetMapping("/{orderId}")
    public ResponseDto<ResGetOrderByIdDto> getOrderById(@PathVariable UUID orderId) {
        return ResponseDto.of("주문 상세 조회 성공", ResGetOrderByIdDto.createDummy(orderId));
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
