package com.sparta.hotdeal.order.application.service;

import com.sparta.hotdeal.order.application.dtos.basket.req.ReqPatchBasketDto;
import com.sparta.hotdeal.order.application.dtos.basket.req.ReqPostBasketDto;
import com.sparta.hotdeal.order.application.dtos.basket.res.ResDeleteBasketDto;
import com.sparta.hotdeal.order.application.dtos.basket.res.ResGetBasketByIdDto;
import com.sparta.hotdeal.order.application.dtos.basket.res.ResGetBasketListDto;
import com.sparta.hotdeal.order.application.dtos.basket.res.ResPatchBasketDto;
import com.sparta.hotdeal.order.application.dtos.basket.res.ResPostBasketDto;
import com.sparta.hotdeal.order.application.dtos.product.ProductDto;
import com.sparta.hotdeal.order.application.dtos.product.ProductByIdtDto;
import com.sparta.hotdeal.order.application.exception.ApplicationException;
import com.sparta.hotdeal.order.application.exception.ErrorCode;
import com.sparta.hotdeal.order.application.port.ProductClientPort;
import com.sparta.hotdeal.order.domain.entity.basket.Basket;
import com.sparta.hotdeal.order.domain.repository.BasketRepository;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class BasketService { //판매중인 상품이 아닌 경우에 대해서 고민이 필요

    private final BasketRepository basketRepository;
    private final ProductClientPort productClientPort;

    public ResPostBasketDto createBasket(UUID userId, ReqPostBasketDto req) {
        //product 유효성
        ProductByIdtDto productByIdtDto = productClientPort.getProduct(req.getProductId());
        Basket basket = Basket.create(productByIdtDto.getProductId(), userId, req.getQuantity());
        basket = basketRepository.save(basket);
        return ResPostBasketDto.of(basket);
    }

    @Transactional(readOnly = true)
    public Page<ResGetBasketListDto> getBasketList(UUID userId, Pageable pageable) {
        Page<Basket> basketPage = basketRepository.findAllByUserId(userId, pageable);
        List<UUID> productIds = basketPage.stream()
                .map(Basket::getProductId)
                .toList();

        //ProductListDto를 Map<UUID, ProductListDto>로 변환
        Map<UUID, ProductDto> productMap = productClientPort.getProductAll(productIds);

        return basketPage.map(basket -> toResGetBasketListDto(basket, productMap));
    }

    @Transactional(readOnly = true)
    public ResGetBasketByIdDto getBasketDetail(UUID userId, UUID basketId) {
        Basket basket = basketRepository.findByIdAndUserId(basketId, userId)
                .orElseThrow(() -> new ApplicationException(ErrorCode.NOT_FOUND_EXCEPTION));

        ProductByIdtDto productByIdtDto = productClientPort.getProduct(basket.getProductId());

        return ResGetBasketByIdDto.of(basket, productByIdtDto);
    }

    public ResPatchBasketDto updateBasket(UUID userId, UUID basketId, ReqPatchBasketDto req) {
        Basket basket = basketRepository.findByIdAndUserId(basketId, userId)
                .orElseThrow(() -> new ApplicationException(ErrorCode.NOT_FOUND_EXCEPTION));

        basket.updateQuantity(req.getQuantity());

        return ResPatchBasketDto.of(basket);
    }

    public ResDeleteBasketDto deleteBasket(UUID userId, UUID basketId) {
        Basket basket = basketRepository.findByIdAndUserId(basketId, userId)
                .orElseThrow(() -> new ApplicationException(ErrorCode.NOT_FOUND_EXCEPTION));

        basketRepository.delete(basket);
        return ResDeleteBasketDto.of(basket);
    }

    private ResGetBasketListDto toResGetBasketListDto(Basket basket,
                                                      Map<UUID, ProductDto> productMap) {
        ProductDto product = Optional.ofNullable(productMap.get(basket.getProductId()))
                .orElseThrow(() -> new ApplicationException(ErrorCode.NOT_FOUND_EXCEPTION));

        return ResGetBasketListDto.of(basket, product);
    }
}
