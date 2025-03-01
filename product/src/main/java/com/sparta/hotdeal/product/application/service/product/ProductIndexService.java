//package com.sparta.hotdeal.product.application.service.product;
//
//import com.sparta.hotdeal.product.domain.entity.product.Product;
//import com.sparta.hotdeal.product.domain.entity.product.ProductDocument;
//import com.sparta.hotdeal.product.domain.entity.product.SubFile;
//import com.sparta.hotdeal.product.infrastructure.repository.product.ProductSearchRepository;
//import java.util.List;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.stereotype.Service;
//
//@Service
//@RequiredArgsConstructor
//@Slf4j
//public class ProductIndexService {
//
//    private final ProductSearchRepository productSearchRepository;
//
//    public void indexProduct(Product product) {
//        try {
//            ProductDocument document = convertToProductDocument(product);
//            productSearchRepository.save(document);
//            log.info("Successfully indexed product in ElasticSearch: {}", product.getId());
//        } catch (Exception e) {
//            log.error("Failed to index product in ElasticSearch: {}", product.getId(), e);
//        }
//    }
//
//    private ProductDocument convertToProductDocument(Product product) {
//        List<String> detailImgs = product.getDetailImgs().getSubFiles() != null
//                ? product.getDetailImgs().getSubFiles().stream()
//                .map(SubFile::getResource)
//                .toList()
//                : List.of();
//
//        String thumbImg = product.getThumbImg().getSubFiles() != null && !product.getThumbImg().getSubFiles().isEmpty()
//                ? product.getThumbImg().getSubFiles().get(0).getResource()
//                : null;
//
//        return ProductDocument.builder()
//                .id(product.getId().toString())
//                .name(product.getName())
//                .description(product.getDescription())
//                .price(product.getPrice())
//                .quantity(product.getQuantity())
//                .category(product.getCategory())
//                .companyId(product.getCompanyId().toString())
//                .status(product.getStatus())
//                .ratingSum(product.getRatingSum() != null ? product.getRatingSum() : 0.0)
//                .reviewCnt(product.getReviewCnt() != null ? product.getReviewCnt() : 0)
//                .discountPrice(product.getDiscountPrice())
//                .detailImgs(detailImgs)
//                .thumbImg(thumbImg)
//                .createdAt(product.getCreatedAt().toString())
//                .build();
//    }
//}
