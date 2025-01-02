package com.sparta.hotdeal.order.presentation.controller;

import com.sparta.hotdeal.order.presentation.dtos.ResponseDto;
import com.sparta.hotdeal.order.presentation.dtos.basket.req.ReqPatchBasketDto;
import com.sparta.hotdeal.order.presentation.dtos.basket.req.ReqPostBasketDto;
import com.sparta.hotdeal.order.presentation.dtos.basket.res.ResDeleteBasketDto;
import com.sparta.hotdeal.order.presentation.dtos.basket.res.ResGetBasketByIdDto;
import com.sparta.hotdeal.order.presentation.dtos.basket.res.ResGetBasketsDto;
import com.sparta.hotdeal.order.presentation.dtos.basket.res.ResPatchBasketDto;
import com.sparta.hotdeal.order.presentation.dtos.basket.res.ResPostBasketDto;
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
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseDto<ResPostBasketDto> createBasket(@RequestBody ReqPostBasketDto req) {
        log.info("ReqPostBasket : {}", req);
        return ResponseDto.of("장바구니가 추가되었습니다.", ResPostBasketDto.createDummyData());
    }

    @GetMapping("/{basketId}")
    public ResponseDto<ResGetBasketByIdDto> getBasketDetail(@PathVariable UUID basketId) {
        return ResponseDto.of("장바구니 단건 조회 성공", ResGetBasketByIdDto.createDummyData(basketId));
    }

    @GetMapping
    public ResponseDto<List<ResGetBasketsDto>> getBaskets() {
        return ResponseDto.of("장바구니 조회 성공", ResGetBasketsDto.createDummyList());
    }

    @PatchMapping("/{basketId}")
    public ResponseDto<ResPatchBasketDto> updateBasket(@PathVariable UUID basketId,
                                                       @RequestBody ReqPatchBasketDto req) {
        log.info("ReqPatchBasketDto : {}", req);
        return ResponseDto.of("장바구니 상품 수량이 수정되었습니다.", ResPatchBasketDto.createDummyData());
    }

    @DeleteMapping("/{basketId}")
    public ResponseDto<ResDeleteBasketDto> deleteBasket(@PathVariable UUID basketId) {
        return ResponseDto.of("해당 장바구니가 삭제되었습니다.", ResDeleteBasketDto.createDummyData());
    }
}
