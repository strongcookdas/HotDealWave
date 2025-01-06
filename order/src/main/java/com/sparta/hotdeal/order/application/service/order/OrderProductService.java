package com.sparta.hotdeal.order.application.service.order;

import com.sparta.hotdeal.order.application.dtos.product.req.ReqProductReduceQuantityDto;
import com.sparta.hotdeal.order.application.dtos.product.res.ResGetProductListForOrderDto;
import com.sparta.hotdeal.order.application.service.client.ProductClientService;
import com.sparta.hotdeal.order.domain.entity.basket.Basket;
import com.sparta.hotdeal.order.domain.entity.order.Order;
import com.sparta.hotdeal.order.domain.entity.order.OrderProduct;
import com.sparta.hotdeal.order.domain.repository.OrderProductRepository;
import com.sparta.hotdeal.order.application.exception.ApplicationException;
import com.sparta.hotdeal.order.application.exception.ErrorCode;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j(topic = "[Order-Product]")
public class OrderProductService {

    private final OrderProductRepository orderProductRepository;
    private final ProductClientService productClientService;

    public void createOrderProductList(Order order, List<Basket> basketList,
                                       Map<UUID, ResGetProductListForOrderDto> productMap) {

        List<OrderProduct> orderProductList = basketList.stream()
                .map(basket -> {
                    ResGetProductListForOrderDto product = getProductOrThrow(productMap, basket.getProductId());
                    return OrderProduct.create(
                            order.getId(),
                            product.getProductId(),
                            basket.getQuantity(),
                            (product.getDiscountPrice() == null) ? product.getPrice() : product.getDiscountPrice()
                    );
                }).toList();

        orderProductRepository.saveAllOrderProduct(orderProductList);

    }

    //장바구니에 따른 상품 조회 후 Map으로 반환
    public Map<UUID, ResGetProductListForOrderDto> getProductMap(List<Basket> basketList) {
        List<UUID> productIds = basketList.stream()
                .map(Basket::getProductId)
                .toList();

        List<ResGetProductListForOrderDto> productList = productClientService.getProductListForOrder(productIds);
        return productList.stream()
                .collect(Collectors.toMap(ResGetProductListForOrderDto::getProductId, product -> product));
    }

    //회사 id에 따른 구매 상품 가격 계산 (쿠폰 유효성 검사를 위해)
    public Map<UUID, Integer> calculateTotalAmountByCompany(List<Basket> basketList,
                                                            Map<UUID, ResGetProductListForOrderDto> productMap) {

        Map<UUID, Integer> totalAmountByCompany = new HashMap<>();

        for (Basket basket : basketList) {
            ResGetProductListForOrderDto product = getProductOrThrow(productMap, basket.getProductId());
            validateProductForPurchase(product, basket.getQuantity());

            int productPrice = calculateProductPrice(product, basket.getQuantity());
            totalAmountByCompany.merge(product.getCompanyId(), productPrice, Integer::sum);
        }

        return totalAmountByCompany;
    }

    //Map을 활용한 총 금액 계산
    public long calculateTotalAmountFromCompanyMap(Map<UUID, Integer> totalAmountByCompany) {
        // Map의 값들(Integer)을 합산하여 총 금액 계산
        return totalAmountByCompany.values().stream()
                .mapToLong(Integer::longValue) // Integer 값을 long으로 변환
                .sum();
    }

    private ResGetProductListForOrderDto getProductOrThrow(
            Map<UUID, ResGetProductListForOrderDto> productMap,
            UUID productId) {
        return Optional.ofNullable(productMap.get(productId))
                .orElseThrow(() -> new ApplicationException(ErrorCode.PRODUCT_NOT_FOUND_EXCEPTION));
    }

    private void validateProductForPurchase(ResGetProductListForOrderDto product, int quantity) {
        if (!"ON_SALE".equals(product.getStatus())) {
            throw new ApplicationException(ErrorCode.PRODUCT_NOT_ON_SALE_EXCEPTION);
        } else if (product.getQuantity() < quantity) {
            throw new ApplicationException(ErrorCode.PRODUCT_INVALID_QUANTITY_EXCEPTION);
        }
    }

    private int calculateProductPrice(ResGetProductListForOrderDto product, int quantity) {
        int unitPrice = product.getDiscountPrice() != null ? product.getDiscountPrice() : product.getPrice();
        return unitPrice * quantity;
    }

    public void reduceProductQuantity(List<Basket> basketList,
                                      Map<UUID, ResGetProductListForOrderDto> productMap) {

        List<ReqProductReduceQuantityDto> reqProductReduceQuantityDtos = basketList.stream()
                .map(basket -> {
                    ResGetProductListForOrderDto product = getProductOrThrow(productMap, basket.getProductId());
                    return ReqProductReduceQuantityDto.of(product.getProductId(), basket.getQuantity());
                })
                .toList();

        productClientService.reduceProductQuantity(reqProductReduceQuantityDtos);
    }
}
