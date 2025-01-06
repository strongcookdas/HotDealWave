package com.sparta.hotdeal.order.application.service;

import com.sparta.hotdeal.order.application.dtos.basket.req.ReqPatchBasketDto;
import com.sparta.hotdeal.order.application.dtos.basket.req.ReqPostBasketDto;
import com.sparta.hotdeal.order.application.dtos.basket.res.ResDeleteBasketDto;
import com.sparta.hotdeal.order.application.dtos.basket.res.ResGetBasketByIdDto;
import com.sparta.hotdeal.order.application.dtos.basket.res.ResGetBasketListDto;
import com.sparta.hotdeal.order.application.dtos.basket.res.ResPatchBasketDto;
import com.sparta.hotdeal.order.application.dtos.basket.res.ResPostBasketDto;
import com.sparta.hotdeal.order.application.dtos.product.res.ResGetProductByIdForBasketDto;
import com.sparta.hotdeal.order.application.dtos.product.res.ResGetProductListForBasketDto;
import com.sparta.hotdeal.order.application.service.client.ProductClientService;
import com.sparta.hotdeal.order.domain.entity.basket.Basket;
import com.sparta.hotdeal.order.domain.repository.BasketRepository;
import com.sparta.hotdeal.order.application.exception.ApplicationException;
import com.sparta.hotdeal.order.application.exception.ErrorCode;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
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
    private final ProductClientService productClientService;

    public ResPostBasketDto createBasket(UUID userId, ReqPostBasketDto req) {
        //product 유효성
        ResGetProductByIdForBasketDto resGetProductByIdForBasketDto = productClientService.getProduct(req.getProductId());
        Basket basket = Basket.create(resGetProductByIdForBasketDto.getProductId(), userId, req.getQuantity());
        basket = basketRepository.save(basket);
        return ResPostBasketDto.of(basket);
    }

    @Transactional(readOnly = true)
    public Page<ResGetBasketListDto> getBasketList(UUID userId, Pageable pageable) {
        Page<Basket> basketPage = basketRepository.findAllByUserId(userId, pageable);

        //ProductListDto를 Map<UUID, ProductListDto>로 변환
        Map<UUID, ResGetProductListForBasketDto> productMap = getProductMap(basketPage.getContent());

        return basketPage.map(basket -> toResGetBasketListDto(basket, productMap));
    }

    @Transactional(readOnly = true)
    public ResGetBasketByIdDto getBasketDetail(UUID userId, UUID basketId) {
        Basket basket = basketRepository.findByIdAndUserId(basketId, userId)
                .orElseThrow(() -> new ApplicationException(ErrorCode.NOT_FOUND_EXCEPTION));

        ResGetProductByIdForBasketDto resGetProductByIdForBasketDto = productClientService.getProduct(basket.getProductId());

        return ResGetBasketByIdDto.of(basket, resGetProductByIdForBasketDto);
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

    private Map<UUID, ResGetProductListForBasketDto> getProductMap(List<Basket> basketList) {
        List<UUID> productIds = basketList.stream()
                .map(Basket::getProductId)
                .toList();

        List<ResGetProductListForBasketDto> productList = productClientService.getProductList(productIds);

        return productList.stream()
                .collect(Collectors.toMap(ResGetProductListForBasketDto::getProductId, product -> product));
    }

    private ResGetBasketListDto toResGetBasketListDto(Basket basket, Map<UUID, ResGetProductListForBasketDto> productMap) {
        ResGetProductListForBasketDto product = Optional.ofNullable(productMap.get(basket.getProductId()))
                .orElseThrow(() -> new ApplicationException(ErrorCode.NOT_FOUND_EXCEPTION));

        return ResGetBasketListDto.of(basket, product);
    }
}
