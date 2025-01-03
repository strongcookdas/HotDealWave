package com.sparta.hotdeal.order.application.service;

import com.sparta.hotdeal.order.application.dtos.basket.CreateBasketDto;
import com.sparta.hotdeal.order.application.dtos.basket.res.ResPostBasketDto;
import com.sparta.hotdeal.order.application.dtos.product.ProductDto;
import com.sparta.hotdeal.order.application.service.client.ProductClientService;
import com.sparta.hotdeal.order.domain.entity.basket.Basket;
import com.sparta.hotdeal.order.domain.repository.BasketRepository;
import com.sparta.hotdeal.order.infrastructure.dtos.product.ResGetProductByIdDto;
import java.util.UUID;
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
        Basket basket = Basket.create(productDto.getProductId(), UUID.randomUUID(), basketDto.getQuantity());
        basket = basketRepository.save(basket);
        return ResPostBasketDto.of(basket.getId());
    }
}
