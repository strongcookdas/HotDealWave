package com.sparta.hotdeal.order.application.service.order;

import com.sparta.hotdeal.order.domain.entity.basket.Basket;
import com.sparta.hotdeal.order.domain.repository.BasketRepository;
import com.sparta.hotdeal.order.infrastructure.exception.ApplicationException;
import com.sparta.hotdeal.order.infrastructure.exception.ErrorCode;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j(topic = "[Order-Basket]")
public class OrderBasketService {
    private final BasketRepository basketRepository;
    public List<Basket> getBasketList(List<UUID> basketIds){
        List<Basket> baskets = basketRepository.findByIdIn(basketIds);
        if (baskets.isEmpty()) {
            log.error("장바구니가 비어있습니다.");
            throw new ApplicationException(ErrorCode.NOT_FOUND_EXCEPTION);
        }
        return baskets;
    }
}
