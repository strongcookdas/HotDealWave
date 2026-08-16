package com.sparta.hotdeal.order.application.service.order;

import com.sparta.hotdeal.order.common.exception.ApplicationException;
import com.sparta.hotdeal.order.common.exception.ErrorCode;
import com.sparta.hotdeal.order.domain.entity.basket.Basket;
import com.sparta.hotdeal.order.domain.repository.BasketRepository;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class OrderBasketService {

    private final BasketRepository basketRepository;

    @Transactional(readOnly = true)
    public List<Basket> getBasketList(UUID userId, List<UUID> basketIds) {
        List<Basket> baskets = basketRepository.findByIdInAndUserId(basketIds, userId);
        if (baskets.isEmpty()) {
            throw new ApplicationException(ErrorCode.BASKET_NOT_FOUND_EXCEPTION);
        }
        return baskets;
    }

    @Transactional
    public void deleteBasketList(List<Basket> basketList) {
        // createOrder 흐름에서는 getBasketList가 이미 커밋된 별도 트랜잭션에서 조회한 detached 엔티티를
        // 넘겨받으므로, dirty checking에 의존하지 않고 명시적으로 save 해야 반영된다.
        basketList.forEach(basket -> {
            basket.remove("email@email.com");
            basketRepository.save(basket);
        });
    }
}
