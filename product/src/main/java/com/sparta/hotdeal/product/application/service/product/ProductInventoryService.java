package com.sparta.hotdeal.product.application.service.product;

import com.sparta.hotdeal.product.application.dtos.req.product.ReqPutProductQuantityDto;
import com.sparta.hotdeal.product.application.dtos.req.promotion.ReqPromotionQuantityDto;
import com.sparta.hotdeal.product.application.dtos.res.product.ResPutProductQuantityDto;
import com.sparta.hotdeal.product.application.exception.ApplicationException;
import com.sparta.hotdeal.product.application.exception.ErrorCode;
import com.sparta.hotdeal.product.application.service.ProductPromotionHelperService;
import com.sparta.hotdeal.product.domain.entity.product.Product;
import com.sparta.hotdeal.product.domain.repository.product.ProductRepository;
import com.sparta.hotdeal.product.infrastructure.kafka.ProductKafkaProducer;
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

    @Value("${spring.kafka.topics.request-payment}")
    private String requestPaymentTopic;

    public ResPutProductQuantityDto reduceQuantity(ReqPutProductQuantityDto reqPutProductQuantityDto) {
        try {
            // 재고 차감 처리 로직
            return processProductQuantity(reqPutProductQuantityDto, false);
        } catch (Exception e) {
            log.error("재고 차감 처리 중 오류 발생: {}", e.getMessage());
            throw new ApplicationException(ErrorCode.PRODUCT_INVENTORY_UPDATE_FAILED_EXCEPTION);
        }
    }

    public ResPutProductQuantityDto restoreQuantity(
            ReqPutProductQuantityDto reqPatchProductQuantityDto) {
        return processProductQuantity(reqPatchProductQuantityDto, true); // 재고 복구
    }

    // 공통된 상품 수량 처리 메서드
    private ResPutProductQuantityDto processProductQuantity(
            ReqPutProductQuantityDto reqPutProductQuantityDto,
            boolean isRestore) {

        // 상품 ID 목록 추출
        List<UUID> productIds = reqPutProductQuantityDto.getProductList().stream()
                .map(ReqPutProductQuantityDto.ProductQuantityDetail::getProductId)
                .collect(Collectors.toList());

        // 상품들 조회
        List<Product> products = productRepository.findAllByIdIn(productIds);

        // 상품이 없으면 예외 처리
        if (products.size() != reqPutProductQuantityDto.getProductList().size()) {
            throw new ApplicationException(ErrorCode.PRODUCT_NOT_FOUND_EXCEPTION);
        }

        // 재고 부족 여부 검증
        for (ReqPutProductQuantityDto.ProductQuantityDetail dto : reqPutProductQuantityDto.getProductList()) {
            Product product = findProductById(dto.getProductId(), products);
            if (!isRestore && product.getQuantity() < dto.getQuantity()) {
                log.warn("재고 부족 상품 ID: {}, 요청 수량: {}, 재고 수량: {}",
                        dto.getProductId(), dto.getQuantity(), product.getQuantity());
                throw new ApplicationException(ErrorCode.PRODUCT_INVENTORY_UPDATE_FAILED_EXCEPTION);
            }
        }

        // 할인 중인 상품을 분리
        List<ReqPromotionQuantityDto> reqPromotionQuantityDtos = products.stream()
                .filter(product -> product.getDiscountPrice() != null) // 할인 중인 상품만 필터
                .map(product -> {
                    // 해당 상품의 ID와 수량을 ReqPromotionQuantityDto로 매핑
                    int quantity = reqPutProductQuantityDto.getProductList().stream()
                            .filter(dto -> dto.getProductId().equals(product.getId()))
                            .map(ReqPutProductQuantityDto.ProductQuantityDetail::getQuantity)
                            .findFirst()
                            .orElse(0); // 수량을 찾고, 없으면 0으로 기본 설정
                    return new ReqPromotionQuantityDto(product.getId(), quantity);
                })
                .collect(Collectors.toList());

        log.info("할인 상품 처리 개수: {}", reqPromotionQuantityDtos.size());

        // 할인 중인 상품에 대한 별도 요청 처리
        processDiscountedProducts(reqPromotionQuantityDtos, isRestore);

        // 상품 수량 처리
        for (ReqPutProductQuantityDto.ProductQuantityDetail dto : reqPutProductQuantityDto.getProductList()) {
            Product product = findProductById(dto.getProductId(), products);
            if (isRestore) {
                product.increaseQuantity(dto.getQuantity());
            } else {
                product.decreaseQuantity(dto.getQuantity());
            }
        }

        return ResPutProductQuantityDto.of(reqPutProductQuantityDto.getOrderId());
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

    public void sendRollbackRequest(String topic, String messageKey, String message) {
        try {
            productKafkaProducer.sendRollbackMessage(topic, messageKey, message);
        } catch (Exception e) {
            log.error("롤백 요청 메시지 생성 실패: {}", e.getMessage());
            throw new ApplicationException(ErrorCode.INTERNAL_SERVER_EXCEPTION);
        }
    }

    public void sendPaymentRequest(String messageKey, String message) {
        try {
            productKafkaProducer.sendPaymentMessage(requestPaymentTopic, messageKey, message);
            log.info("결제 요청 메시지 전송 완료");
        } catch (Exception e) {
            log.error("결제 요청 메시지 생성 실패: {}", e.getMessage());
            throw new ApplicationException(ErrorCode.INTERNAL_SERVER_EXCEPTION);
        }
    }
}
