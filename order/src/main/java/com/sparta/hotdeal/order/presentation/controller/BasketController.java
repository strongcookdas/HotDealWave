package com.sparta.hotdeal.order.presentation.controller;

import com.sparta.hotdeal.order.application.dtos.ResponseDto;
import com.sparta.hotdeal.order.application.dtos.basket.req.ReqPatchBasketDto;
import com.sparta.hotdeal.order.application.dtos.basket.req.ReqPostBasketDto;
import com.sparta.hotdeal.order.application.dtos.basket.res.ResDeleteBasketDto;
import com.sparta.hotdeal.order.application.dtos.basket.res.ResGetBasketByIdDto;
import com.sparta.hotdeal.order.application.dtos.basket.res.ResGetBasketListDto;
import com.sparta.hotdeal.order.application.dtos.basket.res.ResPatchBasketDto;
import com.sparta.hotdeal.order.application.dtos.basket.res.ResPostBasketDto;
import com.sparta.hotdeal.order.application.service.BasketService;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
@RequestMapping("/api/v1/baskets")
public class BasketController {

    private final BasketService basketService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseDto<ResPostBasketDto> createBasket(@RequestBody ReqPostBasketDto req) {
        return ResponseDto.of("장바구니가 추가되었습니다.", basketService.createBasket(req.toDto()));
    }

    @GetMapping("/{basketId}")
    public ResponseDto<ResGetBasketByIdDto> getBasketDetail(@PathVariable UUID basketId) {
        return ResponseDto.of("장바구니 단건 조회 성공",
                basketService.getBasketDetail(UUID.fromString("8fbd655f-dc52-4bf9-ab23-ef89e923db44"), basketId));
    }

    @GetMapping
    public ResponseDto<List<ResGetBasketListDto>> getBasketList() {
        return ResponseDto.of("장바구니 조회 성공", basketService.getBasketList());
    }

    @PatchMapping("/{basketId}")
    public ResponseDto<ResPatchBasketDto> updateBasket(@PathVariable UUID basketId,
                                                       @RequestBody ReqPatchBasketDto req) {
        return ResponseDto.of("장바구니 상품 수량이 수정되었습니다.",
                basketService.updateBasket(
                        UUID.fromString("8fbd655f-dc52-4bf9-ab23-ef89e923db44"),
                        basketId,
                        req.toDto()));
    }

    @DeleteMapping("/{basketId}")
    public ResponseDto<ResDeleteBasketDto> deleteBasket(@PathVariable UUID basketId) {
        return ResponseDto.of("해당 장바구니가 삭제되었습니다.", ResDeleteBasketDto.createDummyData());
    }
}
