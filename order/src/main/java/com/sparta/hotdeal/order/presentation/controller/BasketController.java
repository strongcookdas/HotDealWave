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
import com.sparta.hotdeal.order.infrastructure.custom.RequestUserDetails;
import jakarta.validation.Valid;
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
@RequestMapping("/api/v1/baskets")
public class BasketController {

    private final BasketService basketService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseDto<ResPostBasketDto> createBasket(@AuthenticationPrincipal RequestUserDetails userDetails,
                                                      @RequestBody @Valid ReqPostBasketDto req) {
        log.info("userId : {}", userDetails.getUserId());
        log.info("userEmail : {}", userDetails.getEmail());
        log.info("userRole : {}", userDetails.getRole());
        return ResponseDto.of("장바구니가 추가되었습니다.", basketService.createBasket(userDetails.getUserId(), req));
    }

    @GetMapping("/{basketId}")
    public ResponseDto<ResGetBasketByIdDto> getBasketDetail(@AuthenticationPrincipal RequestUserDetails userDetails,
                                                            @PathVariable UUID basketId) {
        log.info("userId : {}", userDetails.getUserId());
        log.info("userEmail : {}", userDetails.getEmail());
        log.info("userRole : {}", userDetails.getRole());
        return ResponseDto.of("장바구니 단건 조회 성공",
                basketService.getBasketDetail(userDetails.getUserId(), basketId));
    }

    @GetMapping
    public ResponseDto<Page<ResGetBasketListDto>> getBasketList(@AuthenticationPrincipal RequestUserDetails userDetails,
                                                                Pageable pageable) { //pageable 에 대한 설정 논의 필요
        log.info("userId : {}", userDetails.getUserId());
        log.info("userEmail : {}", userDetails.getEmail());
        log.info("userRole : {}", userDetails.getRole());
        return ResponseDto.of("장바구니 조회 성공", basketService.getBasketList(userDetails.getUserId(), pageable));
    }

    @PatchMapping("/{basketId}")
    public ResponseDto<ResPatchBasketDto> updateBasket(@AuthenticationPrincipal RequestUserDetails userDetails,
                                                       @PathVariable UUID basketId,
                                                       @RequestBody @Valid ReqPatchBasketDto req) {
        log.info("userId : {}", userDetails.getUserId());
        log.info("userEmail : {}", userDetails.getEmail());
        log.info("userRole : {}", userDetails.getRole());
        return ResponseDto.of("장바구니 상품 수량이 수정되었습니다.",
                basketService.updateBasket(
                        userDetails.getUserId(),
                        basketId,
                        req));
    }

    @DeleteMapping("/{basketId}")
    public ResponseDto<ResDeleteBasketDto> deleteBasket(@AuthenticationPrincipal RequestUserDetails userDetails,
                                                        @PathVariable UUID basketId) {

        log.info("userId : {}", userDetails.getUserId());
        log.info("userEmail : {}", userDetails.getEmail());
        log.info("userRole : {}", userDetails.getRole());
        return ResponseDto.of("해당 장바구니가 삭제되었습니다.",
                basketService.deleteBasket(userDetails.getUserId(), basketId));
    }
}
