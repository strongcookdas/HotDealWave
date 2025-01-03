package com.sparta.hotdeal.order.application.service;

import com.sparta.hotdeal.order.application.dtos.basket.CreateBasketDto;
import com.sparta.hotdeal.order.application.dtos.basket.res.ResGetBasketByIdDto;
import com.sparta.hotdeal.order.application.dtos.basket.res.ResGetBasketListDto;
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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BasketService {

    private final BasketRepository basketRepository;
    private final ProductClientService productClientService;

    @Transactional
    public ResPostBasketDto createBasket(CreateBasketDto basketDto) {
        //product 유효성
        ProductDto productDto = productClientService.getProduct(basketDto.getProductId());
        Basket basket = Basket.create(productDto.getProductId(),
                UUID.fromString("8fbd655f-dc52-4bf9-ab23-ef89e923db44"), basketDto.getQuantity());
        basket = basketRepository.save(basket);
        return ResPostBasketDto.of(basket);
    }

    public List<ResGetBasketListDto> getBasketList() {
        List<Basket> basketList = basketRepository.findAllByUserId(
                UUID.fromString("8fbd655f-dc52-4bf9-ab23-ef89e923db44"));

        //ProductListDto를 Map<UUID, ProductListDto>로 변환
        Map<UUID, ProductListDto> productMap = getProductMap(basketList);

        return basketList.stream().
                map(basket -> toResGetBasketListDto(basket, productMap))
                .collect(Collectors.toList());

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

        return ResGetBasketListDto.from(basket, product);
    }

    public ResGetBasketByIdDto getBasketDetail(UUID userId, UUID basketId) {
        Basket basket = basketRepository.findById(basketId)
                .orElseThrow(() -> new IllegalArgumentException("해당하는 장바구니가 없습니다."));

        if (!basket.getUserId().equals(userId)) {
            throw new IllegalArgumentException("접근 권한이 없습니다.");
        }

        ProductDto productDto = productClientService.getProduct(basket.getProductId());

        return ResGetBasketByIdDto.of(basket, productDto);
    }
}
