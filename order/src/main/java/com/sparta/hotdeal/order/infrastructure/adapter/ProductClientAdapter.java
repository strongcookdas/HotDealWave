package com.sparta.hotdeal.order.infrastructure.adapter;

import com.sparta.hotdeal.order.application.dtos.product.req.ReqProductReduceQuantityDto;
import com.sparta.hotdeal.order.application.dtos.product.ProductDto;
import com.sparta.hotdeal.order.application.port.ProductClientPort;
import com.sparta.hotdeal.order.domain.entity.basket.Basket;
import com.sparta.hotdeal.order.infrastructure.client.ProductClient;
import com.sparta.hotdeal.order.infrastructure.dtos.product.ResGetProductListDto;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ProductClientAdapter implements ProductClientPort {

    private final ProductClient productClient;

    @Override
    public Map<UUID, ProductDto> getProductAll(List<UUID> productIds) {
        List<ResGetProductListDto> resGetProductListDtoList = productClient.getProductListFromAPI(productIds).getData()
                .toList();
        return resGetProductListDtoList.stream()
                .map(ResGetProductListDto::toGetProductListForOrderDto)
                .collect(Collectors.toMap(
                        ProductDto::getProductId,
                        product -> product
                ));
    }

    @Override
    public void reduceProductQuantity(List<Basket> basketList, Map<UUID, ProductDto> productMap) {
        // API 호출 구현 예정
        List<ReqProductReduceQuantityDto> reqProductReduceQuantityDtos = basketList.stream()
                .map(basket -> {
                    //이부분 수정이 필요 장바구니 상품이 조회되지 않았을 경우에 대해서 체크가 필요
                    ProductDto product = productMap.get(basket.getProductId());
                    return ReqProductReduceQuantityDto.of(product.getProductId(), basket.getQuantity());
                })
                .toList();
    }
}
