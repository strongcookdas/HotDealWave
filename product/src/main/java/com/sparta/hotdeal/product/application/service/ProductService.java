package com.sparta.hotdeal.product.application.service;

import com.sparta.hotdeal.product.application.dtos.req.product.ReqPatchProductQuantityDto;
import com.sparta.hotdeal.product.application.dtos.req.product.ReqPatchProductStatusDto;
import com.sparta.hotdeal.product.application.dtos.req.product.ReqPostProductDto;
import com.sparta.hotdeal.product.application.dtos.req.product.ReqPutProductDto;
import com.sparta.hotdeal.product.application.dtos.req.promotion.ReqPromotionQuantityDto;
import com.sparta.hotdeal.product.application.dtos.res.product.ResGetProductDto;
import com.sparta.hotdeal.product.application.dtos.res.product.ResPatchProductStatusDto;
import com.sparta.hotdeal.product.application.dtos.res.product.ResPatchReduceProductQuantityDto;
import com.sparta.hotdeal.product.application.dtos.res.product.ResPatchRestoreProductQuantityDto;
import com.sparta.hotdeal.product.application.dtos.res.product.ResPostProductDto;
import com.sparta.hotdeal.product.application.dtos.res.product.ResPutProductDto;
import com.sparta.hotdeal.product.application.exception.ApplicationException;
import com.sparta.hotdeal.product.application.exception.ErrorCode;
import com.sparta.hotdeal.product.application.service.client.CompanyClientService;
import com.sparta.hotdeal.product.domain.entity.product.File;
import com.sparta.hotdeal.product.domain.entity.product.Product;
import com.sparta.hotdeal.product.domain.entity.product.ProductStatusEnum;
import com.sparta.hotdeal.product.domain.entity.product.SubFile;
import com.sparta.hotdeal.product.domain.repository.product.ProductRepository;
import com.sparta.hotdeal.product.infrastructure.dtos.ResGetCompanyByIdDto;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class ProductService {

    private final ProductRepository productRepository;
    private final CompanyClientService companyClientService;
    private final FileService fileService;
    private final SubFileService subFileService;
    private final ProductPromotionHelperService productPromotionHelperService;

    public ResPostProductDto createProduct(ReqPostProductDto productDto) {
        // company 검증
        ResGetCompanyByIdDto company = companyClientService.getCompany(productDto.getCompanyId());
        log.info("company Info : {}", company);

        // 이미지 업로드(파일 저장)
        // 상세 이미지
        File detailImgs = fileService.saveFile();
        for (MultipartFile file : productDto.getDetailImgs()) {
            subFileService.saveImg(file, detailImgs);
        }

        // 썸네일 이미지
        File thumbImg = fileService.saveFile();
        subFileService.saveImg(productDto.getThumbImg(), thumbImg);

        // 상품 저장
        Product product = Product.create(productDto.getName(), (Integer) productDto.getPrice(),
                productDto.getQuantity(),
                productDto.getCategory(), productDto.getDescription(), detailImgs, thumbImg, productDto.getCompanyId(),
                ProductStatusEnum.ON_SALE, 0D, 0);

        product = productRepository.save(product);

        return ResPostProductDto.of(product.getId());
    }

    public ResPutProductDto updateProduct(UUID productId, ReqPutProductDto reqPutUpdateProductDto, String username) {

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ApplicationException(ErrorCode.PRODUCT_NOT_FOUND_EXCEPTION));

        // 파일 객체 가져오기 (기존 파일 그대로 사용)
        File detailImgsFile = product.getDetailImgs();
        File thumbImgFile = product.getThumbImg();

        // 상세 이미지 업데이트
        if (reqPutUpdateProductDto.getDetailImgs() != null) {
            log.info("detailImgsFile : {}", reqPutUpdateProductDto.getDetailImgs());
            subFileService.updateSubFiles(reqPutUpdateProductDto.getDetailImgs(), detailImgsFile, username);
        }

        // 썸네일 이미지 업데이트
        if (reqPutUpdateProductDto.getThumbImg() != null) {
            log.info("thumbImgFile : {}", reqPutUpdateProductDto.getThumbImg());
            subFileService.updateImg(reqPutUpdateProductDto.getThumbImg(), thumbImgFile, username);
        }

        // 상품 정보 업데이트
        product.update(
                reqPutUpdateProductDto.getName(),
                reqPutUpdateProductDto.getPrice(),
                reqPutUpdateProductDto.getQuantity(),
                reqPutUpdateProductDto.getCategory(),
                reqPutUpdateProductDto.getDescription(),
                reqPutUpdateProductDto.getStatus()
        );

        return ResPutProductDto.of(product.getId());
    }

    public ResPatchProductStatusDto updateProductStatus(UUID productId,
                                                        ReqPatchProductStatusDto reqPatchUpdateProductStatusDto) {

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ApplicationException(ErrorCode.PRODUCT_NOT_FOUND_EXCEPTION));

        product.updateStatus(reqPatchUpdateProductStatusDto.getStatus());

        return ResPatchProductStatusDto.of(product.getId());
    }

    public void deleteProduct(UUID productId, String username) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ApplicationException(ErrorCode.PRODUCT_NOT_FOUND_EXCEPTION));

        // 파일 삭제 처리
        File detailImgsFile = product.getDetailImgs();
        File thumbImgFile = product.getThumbImg();

        fileService.deleteFile(detailImgsFile, username);
        fileService.deleteFile(thumbImgFile, username);

        // 서브파일 삭제 처리
        subFileService.deleteImg(detailImgsFile, username);
        subFileService.deleteImg(thumbImgFile, username);

        // 상품 삭제 처리
        product.delete(username);
    }

    public List<ResPatchReduceProductQuantityDto> reduceQuantity(
            List<ReqPatchProductQuantityDto> reqPatchProductQuantityDto) {
        return processProductQuantity(reqPatchProductQuantityDto, false); // 재고 차감
    }

    public List<ResPatchRestoreProductQuantityDto> restoreQuantity(
            List<ReqPatchProductQuantityDto> reqPatchProductQuantityDto) {
        return processProductQuantity(reqPatchProductQuantityDto, true); // 재고 복구
    }

    @Transactional(readOnly = true)
    public ResGetProductDto getProduct(UUID productId) {
        // 상품 조회
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ApplicationException(ErrorCode.PRODUCT_NOT_FOUND_EXCEPTION));

        return convertToResGetProductDto(product);
    }

    @Transactional(readOnly = true)
    public Page<ResGetProductDto> getAllProducts(int pageNumber, int pageSize, String sortBy, String direction,
                                                 String search, List<UUID> productIds) {
        Sort sort = direction.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(pageNumber - 1, pageSize, sort);

        Page<Product> products = productRepository.findAllWithSearchAndPaging(search, productIds, pageable);

        List<ResGetProductDto> productDtos = products.stream()
                .map(this::convertToResGetProductDto)
                .collect(Collectors.toList());

        return new PageImpl<>(productDtos, pageable, products.getTotalElements());
    }

    private ResGetProductDto convertToResGetProductDto(Product product) {
        // 평점 계산
        BigDecimal rating =
                product.getRatingSum() == null ? BigDecimal.valueOf(0.0) : BigDecimal.valueOf(product.getRatingSum())
                        .divide(BigDecimal.valueOf(product.getReviewCnt()), 1, RoundingMode.HALF_UP);

        // 파일 정보
        File detailImgsFile = product.getDetailImgs();
        File thumbImgFile = product.getThumbImg();
        List<String> detailImgs = detailImgsFile.getSubFiles().stream().map(SubFile::getResource).toList();
        String thumbImg = thumbImgFile.getSubFiles().get(0).getResource();

        // ResGetProductDto 생성
        return ResGetProductDto.builder()
                .productId(product.getId())
                .name(product.getName())
                .price(product.getPrice())
                .quantity(product.getQuantity())
                .category(product.getCategory())
                .companyId(product.getCompanyId())
                .description(product.getDescription())
                .detailImgs(detailImgs)
                .thumbImg(thumbImg)
                .status(product.getStatus())
                .rating(rating.doubleValue())
                .reviewCnt(product.getReviewCnt())
                .discountPrice(product.getDiscountPrice())
                .build();
    }

    public void updateProductDiscountPrice(UUID productId, Integer discountPrice) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ApplicationException(ErrorCode.PRODUCT_NOT_FOUND_EXCEPTION));

        product.updateDiscountPrice(discountPrice);
    }

    // 공통된 상품 수량 처리 메서드
    private <T> List<T> processProductQuantity(
            List<ReqPatchProductQuantityDto> reqPatchProductQuantityDto,
            boolean isRestore) {

        // 상품 ID 목록 추출
        List<UUID> productIds = reqPatchProductQuantityDto.stream()
                .map(ReqPatchProductQuantityDto::getProductId)
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
                            .map(ReqPatchProductQuantityDto::getQuantity)
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
        for (ReqPatchProductQuantityDto dto : reqPatchProductQuantityDto) {
            Product product = findProductById(dto.getProductId(), products);
            int quantityChange = isRestore ? dto.getQuantity() : -dto.getQuantity(); // 재고 복구 시 수량을 더함
            int newQuantity = product.getQuantity() + quantityChange;
            product.updateQuantity(newQuantity);

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

}
