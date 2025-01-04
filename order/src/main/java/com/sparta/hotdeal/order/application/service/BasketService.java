package com.sparta.hotdeal.order.application.service;

import com.sparta.hotdeal.order.application.dtos.basket.CreateBasketDto;
import com.sparta.hotdeal.order.application.dtos.basket.UpdateBasketDto;
import com.sparta.hotdeal.order.application.dtos.basket.res.ResDeleteBasketDto;
import com.sparta.hotdeal.order.application.dtos.basket.res.ResGetBasketByIdDto;
import com.sparta.hotdeal.order.application.dtos.basket.res.ResGetBasketListDto;
import com.sparta.hotdeal.order.application.dtos.basket.res.ResPatchBasketDto;
import com.sparta.hotdeal.order.application.dtos.basket.res.ResPostBasketDto;
import com.sparta.hotdeal.order.application.dtos.product.ProductDto;
import com.sparta.hotdeal.order.application.dtos.product.ProductListDto;
import com.sparta.hotdeal.order.application.service.client.ProductClientService;
import com.sparta.hotdeal.order.domain.entity.basket.Basket;
import com.sparta.hotdeal.order.domain.repository.BasketRepository;
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
    private static final UUID TEST_USER_ID = UUID.fromString("8fbd655f-dc52-4bf9-ab23-ef89e923db44");

    private final BasketRepository basketRepository;
    private final ProductClientService productClientService;

    public ResPostBasketDto createBasket(CreateBasketDto basketDto) {
        //product 유효성
        ProductDto productDto = productClientService.getProduct(basketDto.getProductId());
        Basket basket = Basket.create(productDto.getProductId(), TEST_USER_ID, basketDto.getQuantity());
        basket = basketRepository.save(basket);
        return ResPostBasketDto.of(basket);
    }

    @Transactional(readOnly = true)
    public Page<ResGetBasketListDto> getBasketList(Pageable pageable) {
        Page<Basket> basketPage = basketRepository.findAllByUserId(TEST_USER_ID, pageable);

        //ProductListDto를 Map<UUID, ProductListDto>로 변환
        Map<UUID, ProductListDto> productMap = getProductMap(basketPage.getContent());

        return basketPage.map(basket -> toResGetBasketListDto(basket, productMap));
    }

    @Transactional(readOnly = true)
    public ResGetBasketByIdDto getBasketDetail(UUID userId, UUID basketId) {
        Basket basket = basketRepository.findByIdAndUserId(basketId, userId)
                .orElseThrow(() -> new IllegalArgumentException("해당하는 장바구니가 없습니다."));

        ProductDto productDto = productClientService.getProduct(basket.getProductId());

        return ResGetBasketByIdDto.of(basket, productDto);
    }

    public ResPatchBasketDto updateBasket(UUID userId, UUID basketId, UpdateBasketDto basketDto) {
        Basket basket = basketRepository.findByIdAndUserId(basketId, userId)
                .orElseThrow(() -> new IllegalArgumentException("해당하는 장바구니가 없습니다."));

        if (!basket.getUserId().equals(userId)) {
            throw new IllegalArgumentException("접근 권한이 없습니다.");
        }
        basket.updateQuantity(basketDto.getQuantity());

        return ResPatchBasketDto.of(basket);
    }

    public ResDeleteBasketDto deleteBasket(UUID userId, UUID basketId) {
        Basket basket = basketRepository.findByIdAndUserId(basketId, userId)
                .orElseThrow(() -> new IllegalArgumentException("해당하는 장바구니가 없습니다."));

        basketRepository.delete(basket);
        return ResDeleteBasketDto.of(basket);
    }

    private Map<UUID, ProductListDto> getProductMap(List<Basket> basketList) {
        List<UUID> productIds = basketList.stream()
                .map(Basket::getProductId)
                .toList();

        List<ProductListDto> productList = productClientService.getProductList(productIds);

        return productList.stream()
                .collect(Collectors.toMap(ProductListDto::getProductId, product -> product));
    }

    private ResGetBasketListDto toResGetBasketListDto(Basket basket, Map<UUID, ProductListDto> productMap) {
        ProductListDto product = Optional.ofNullable(productMap.get(basket.getProductId()))
                .orElseThrow(() -> new IllegalArgumentException(
                        "Product not found for productId: " + basket.getProductId()
                ));

        return ResGetBasketListDto.of(basket, product);
    }
}
