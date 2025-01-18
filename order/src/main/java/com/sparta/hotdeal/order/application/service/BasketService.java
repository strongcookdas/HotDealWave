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
import com.sparta.hotdeal.order.common.exception.ApplicationException;
import com.sparta.hotdeal.order.common.exception.ErrorCode;
import com.sparta.hotdeal.order.application.port.ProductClientPort;
import com.sparta.hotdeal.order.domain.entity.basket.Basket;
import com.sparta.hotdeal.order.domain.repository.BasketRepository;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class BasketService {

    private final BasketRepository basketRepository;
    private final ProductClientPort productClientPort;

    public ResPostBasketDto createBasket(UUID userId, ReqPostBasketDto req) {
        CompletableFuture<ProductByIdtDto> productFuture = getProductAsync(req.getProductId());
        Basket basket = Basket.create(req.getProductId(), userId, req.getQuantity());
        basket = basketRepository.save(basket);
        productFuture.join(); // 비동기 결과 대기
        return ResPostBasketDto.of(basket);
    }

    @Async
    public CompletableFuture<ProductByIdtDto> getProductAsync(UUID productId) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                return productClientPort.getProduct(productId);
            } catch (Exception e) {
                log.error("상품 단건 조회 API 호출 실패 productId {}: {}", productId, e.getMessage());
                throw new ApplicationException(ErrorCode.PRODUCT_NOT_FOUND_EXCEPTION);
            }
        });
    }

    @Transactional(readOnly = true)
    public Page<ResGetBasketListDto> getBasketList(UUID userId, Pageable pageable) {
        Page<Basket> basketPage = basketRepository.findAllByUserId(userId, pageable);
        Map<UUID, ProductDto> productMap = getProductMap(basketPage);

        return basketPage.map(basket -> toResGetBasketListDto(basket, productMap));
    }

    private Map<UUID, ProductDto> getProductMap(Page<Basket> basketPage) {
        List<UUID> productIds = basketPage.stream()
                .map(Basket::getProductId)
                .toList();

        return productClientPort.getProductAll(productIds);
    }

    private ResGetBasketListDto toResGetBasketListDto(Basket basket, Map<UUID, ProductDto> productMap) {
        ProductDto product = Optional.ofNullable(productMap.get(basket.getProductId()))
                .orElseThrow(() -> new ApplicationException(ErrorCode.PRODUCT_NOT_FOUND_EXCEPTION));

        return ResGetBasketListDto.of(basket, product);
    }

    @Transactional(readOnly = true)
    public ResGetBasketByIdDto getBasketDetail(UUID userId, UUID basketId) {
        Basket basket = getBasket(userId, basketId);
        ProductByIdtDto productByIdtDto = productClientPort.getProduct(basket.getProductId());
        return ResGetBasketByIdDto.of(basket, productByIdtDto);
    }

    public ResPatchBasketDto updateBasket(UUID userId, UUID basketId, ReqPatchBasketDto req) {
        Basket basket = getBasket(userId, basketId);
        basket.updateQuantity(req.getQuantity());
        return ResPatchBasketDto.of(basket);
    }

    public ResDeleteBasketDto deleteBasket(UUID userId, String email, UUID basketId) {
        Basket basket = getBasket(userId, basketId);
        basket.delete(email);
        return ResDeleteBasketDto.of(basket);
    }

    private Basket getBasket(UUID basketId, UUID userId) {
        return basketRepository.findByIdAndUserId(basketId, userId)
                .orElseThrow(() -> new ApplicationException(ErrorCode.BASKET_NOT_FOUND_EXCEPTION));
    }
}
