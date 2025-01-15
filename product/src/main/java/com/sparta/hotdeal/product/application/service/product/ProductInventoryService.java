package com.sparta.hotdeal.product.application.service.product;

import com.sparta.hotdeal.product.application.dtos.req.product.ReqPutProductQuantityDto;
import com.sparta.hotdeal.product.application.dtos.req.promotion.ReqPromotionQuantityDto;
import com.sparta.hotdeal.product.application.dtos.res.product.ResPatchReduceProductQuantityDto;
import com.sparta.hotdeal.product.application.dtos.res.product.ResPatchRestoreProductQuantityDto;
import com.sparta.hotdeal.product.application.exception.ApplicationException;
import com.sparta.hotdeal.product.application.exception.ErrorCode;
import com.sparta.hotdeal.product.application.service.ProductPromotionHelperService;
import com.sparta.hotdeal.product.domain.entity.product.Product;
import com.sparta.hotdeal.product.domain.repository.product.ProductRepository;
import com.sparta.hotdeal.product.infrastructure.kafka.ProductKafkaProducer;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class ProductInventoryService {

    private final ProductRepository productRepository;
    private final ProductPromotionHelperService productPromotionHelperService;
    private final ProductKafkaProducer productKafkaProducer;

    @Value("${spring.kafka.topics.rollback-reduce-quantity}")
    private String rollbackReduceQuantityTopic;

    public List<ResPatchReduceProductQuantityDto> reduceQuantity(
            List<ReqPutProductQuantityDto> reqPutProductQuantityDto) {
        try {
            // 재고 차감 처리 로직
            return processProductQuantity(reqPutProductQuantityDto, false);
        } catch (Exception e) {
            log.error("재고 차감 처리 중 오류 발생: {}", e.getMessage());
            // 오류 발생 시 롤백 요청
            sendRollbackRequest(rollbackReduceQuantityTopic, "재고 차감 처리 중 오류 발생");
            throw new ApplicationException(ErrorCode.PRODUCT_INVENTORY_UPDATE_FAILED_EXCEPTION);
        }
    }

    public List<ResPatchRestoreProductQuantityDto> restoreQuantity(
            List<ReqPutProductQuantityDto> reqPatchProductQuantityDto) {
        return processProductQuantity(reqPatchProductQuantityDto, true); // 재고 복구
    }

    // 공통된 상품 수량 처리 메서드
    private <T> List<T> processProductQuantity(
            List<ReqPutProductQuantityDto> reqPatchProductQuantityDto,
            boolean isRestore) {

        // 상품 ID 목록 추출
        List<UUID> productIds = reqPatchProductQuantityDto.stream()
                .map(ReqPutProductQuantityDto::getProductId)
                .collect(Collectors.toList());

        // 상품들 조회
        List<Product> products = productRepository.findAllByIdIn(productIds);

        // 상품이 없으면 예외 처리
        if (products.size() != reqPatchProductQuantityDto.size()) {
            throw new ApplicationException(ErrorCode.PRODUCT_NOT_FOUND_EXCEPTION);
        }

        // 할인 중인 상품을 분리
        List<ReqPromotionQuantityDto> reqPromotionQuantityDtos = products.stream()
                .filter(product -> product.getDiscountPrice() != null) // 할인 중인 상품만 필터
                .map(product -> {
                    // 해당 상품의 ID와 수량을 ReqPromotionQuantityDto로 매핑
                    int quantity = reqPatchProductQuantityDto.stream()
                            .filter(dto -> dto.getProductId().equals(product.getId()))
                            .map(ReqPutProductQuantityDto::getQuantity)
                            .findFirst()
                            .orElse(0); // 수량을 찾고, 없으면 0으로 기본 설정
                    return new ReqPromotionQuantityDto(product.getId(), quantity);
                })
                .collect(Collectors.toList());

        log.info("ProductService reqPromotionQuantityDtos : {}", reqPromotionQuantityDtos.size());

        // 할인 중인 상품에 대한 별도 요청 처리
        processDiscountedProducts(reqPromotionQuantityDtos, isRestore);

        List<T> resPatchProductQuantityDtos = new ArrayList<>();

        // 상품 수량 처리
        for (ReqPutProductQuantityDto dto : reqPatchProductQuantityDto) {
            Product product = findProductById(dto.getProductId(), products);

            if (isRestore) {
                product.increaseQuantity(dto.getQuantity());
            } else {
                product.decreaseQuantity(dto.getQuantity());
            }

            // 결과 DTO 생성 후 추가
            if (isRestore) {
                resPatchProductQuantityDtos.add((T) ResPatchRestoreProductQuantityDto.of(product.getId()));
            } else {
                resPatchProductQuantityDtos.add((T) ResPatchReduceProductQuantityDto.of(product.getId()));
            }
        }

        return resPatchProductQuantityDtos;
    }

    private Product findProductById(UUID productId, List<Product> products) {
        return products.stream()
                .filter(product -> product.getId().equals(productId))
                .findFirst()
                .orElseThrow(() -> new ApplicationException(ErrorCode.PRODUCT_NOT_FOUND_EXCEPTION));
    }

    private void processDiscountedProducts(List<ReqPromotionQuantityDto> reqPromotionQuantityDtos, Boolean isRestore) {
        productPromotionHelperService.processPromotionQuantity(reqPromotionQuantityDtos, isRestore);
    }

    public void sendRollbackRequest(String topic, String message) {
        try {
            productKafkaProducer.sendRollbackMessage(topic, message);
            log.info("롤백 요청 메시지 전송 완료: {}", message);
        } catch (Exception e) {
            log.error("롤백 요청 메시지 생성 실패: {}", e.getMessage());
            throw new ApplicationException(ErrorCode.INTERNAL_SERVER_EXCEPTION);
        }
    }
}
