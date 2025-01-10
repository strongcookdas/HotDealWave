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
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
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
    @Operation(summary = "장바구니 생성 API", description = "장바구니를 생성합니다.")
    public ResponseDto<ResPostBasketDto> createBasket(@AuthenticationPrincipal RequestUserDetails userDetails,
                                                      @RequestBody @Valid ReqPostBasketDto req) {
        return ResponseDto.of("장바구니가 추가되었습니다.", basketService.createBasket(userDetails.getUserId(), req));
    }

    @GetMapping("/{basketId}")
    @Operation(summary = "장바구니 단건 조회 API", description = "장바구니 단건을 조회합니다.")
    public ResponseDto<ResGetBasketByIdDto> getBasketDetail(@AuthenticationPrincipal RequestUserDetails userDetails,
                                                            @PathVariable UUID basketId) {
        return ResponseDto.of("장바구니 단건 조회 성공",
                basketService.getBasketDetail(userDetails.getUserId(), basketId));
    }

    @GetMapping
    @Operation(summary = "장바구니 목록 조회 API", description = "장바구니 목록을 조회합니다.")
    public ResponseDto<Page<ResGetBasketListDto>> getBasketList(@AuthenticationPrincipal RequestUserDetails userDetails,
                                                                @PageableDefault(size = 10) Pageable pageable) { //pageable 에 대한 설정 논의 필요
        return ResponseDto.of("장바구니 조회 성공", basketService.getBasketList(userDetails.getUserId(), pageable));
    }

    @PatchMapping("/{basketId}")
    @Operation(summary = "장바구니 수량 수정 API", description = "장바구니 수량을 수정합니다.")
    public ResponseDto<ResPatchBasketDto> updateBasket(@AuthenticationPrincipal RequestUserDetails userDetails,
                                                       @PathVariable UUID basketId,
                                                       @RequestBody @Valid ReqPatchBasketDto req) {
        return ResponseDto.of("장바구니 상품 수량이 수정되었습니다.",
                basketService.updateBasket(userDetails.getUserId(), basketId, req));
    }

    @DeleteMapping("/{basketId}")
    @Operation(summary = "장바구니 삭제 API", description = "장바구니를 삭제합니다.")
    public ResponseDto<ResDeleteBasketDto> deleteBasket(@AuthenticationPrincipal RequestUserDetails userDetails,
                                                        @PathVariable UUID basketId) {

        return ResponseDto.of("해당 장바구니가 삭제되었습니다.",
                basketService.deleteBasket(userDetails.getUserId(), basketId));
    }
}
