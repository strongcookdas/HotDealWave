package com.sparta.hotdeal.order.domain.repository;
// 해당 패키지는 도메인 로직을 정의하는 계층
// 어떤 저장소 기술을 사용할지에 대한 구현 세부 사항과는 독립적이어야 한다.
// 즉, 도메인 계층의 책임은 데이터 저장 방식에 종속되지 않고 "비즈니스 의도와 규칙"에 초점이 맞춰져야 한다.
// 바로 JPARepository를 종속시키면 도메인 계층이 JPA 라는 특정 기술에 종속된다.
// BasketRepositoryImpl 프록시 객체를 주입받게 됨

import com.sparta.hotdeal.order.domain.entity.basket.Basket;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
public interface BasketRepository {
    Basket save(Basket basket);

    List<Basket> findByIdIn(List<UUID> ids);

    Optional<Basket> findByIdAndUserId(UUID basketId, UUID userId);

    void delete(Basket basket);

    Page<Basket> findAllByUserId(UUID userId, Pageable pageable);
}
