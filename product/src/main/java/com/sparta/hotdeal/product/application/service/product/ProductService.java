package com.sparta.hotdeal.product.application.service.product;

import com.sparta.hotdeal.product.application.dtos.req.product.ReqPatchProductStatusDto;
import com.sparta.hotdeal.product.application.dtos.req.product.ReqPostProductDto;
import com.sparta.hotdeal.product.application.dtos.req.product.ReqPutProductDto;
import com.sparta.hotdeal.product.application.dtos.res.product.ResGetProductDto;
import com.sparta.hotdeal.product.application.dtos.res.product.ResPatchProductStatusDto;
import com.sparta.hotdeal.product.application.dtos.res.product.ResPostProductDto;
import com.sparta.hotdeal.product.application.dtos.res.product.ResPutProductDto;
import com.sparta.hotdeal.product.application.exception.ApplicationException;
import com.sparta.hotdeal.product.application.exception.ErrorCode;
import com.sparta.hotdeal.product.application.service.ProductPromotionHelperService;
import com.sparta.hotdeal.product.application.service.client.CompanyClientService;
import com.sparta.hotdeal.product.application.service.file.FileService;
import com.sparta.hotdeal.product.application.service.file.SubFileService;
import com.sparta.hotdeal.product.domain.entity.product.*;
import com.sparta.hotdeal.product.domain.repository.product.ProductRepository;
import com.sparta.hotdeal.product.infrastructure.dtos.ResGetCompanyByIdDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.UUID;

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
//    private final ProductSearchRepository productSearchRepository;
//    private final ProductIndexService productIndexService;

    public ResPostProductDto createProduct(ReqPostProductDto productDto) {
        // company 검증
        ResGetCompanyByIdDto company = companyClientService.getCompany(productDto.getCompanyId());
        log.info("company Info : {}", company);

        // 이미지 업로드(파일 저장)
        // 상세 이미지
        File detailImgs = fileService.saveFile();
        for (MultipartFile file : productDto.getDetailImgs()) {
            subFileService.saveImg(file, detailImgs, ImageTypeEnum.PRODUCT);
        }
        log.info("detailImgs : {}", detailImgs);

        // 썸네일 이미지
        File thumbImg = fileService.saveFile();
        subFileService.saveImg(productDto.getThumbImg(), thumbImg, ImageTypeEnum.PRODUCT);
        log.info("thumbImg : {}", thumbImg);

        // 상품 저장
        Product product = Product.create(productDto.getName(), (Integer) productDto.getPrice(),
                productDto.getQuantity(),
                productDto.getCategory(), productDto.getDescription(), detailImgs, thumbImg, productDto.getCompanyId(),
                ProductStatusEnum.ON_SALE, 0D, 0);

        product = productRepository.save(product);
        log.info("product : {}", product);

        // ElasticSearch 색인
//        productIndexService.indexProduct(product);

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
            subFileService.updateSubFiles(reqPutUpdateProductDto.getDetailImgs(), detailImgsFile, username,
                    ImageTypeEnum.PRODUCT);
        }

        // 썸네일 이미지 업데이트
        if (reqPutUpdateProductDto.getThumbImg() != null) {
            log.info("thumbImgFile : {}", reqPutUpdateProductDto.getThumbImg());
            subFileService.updateImg(reqPutUpdateProductDto.getThumbImg(), thumbImgFile, username,
                    ImageTypeEnum.PRODUCT);
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

        // ElasticSearch 색인 갱신
//        productIndexService.indexProduct(product);

        return ResPutProductDto.of(product.getId());
    }

    public ResPatchProductStatusDto updateProductStatus(UUID productId,
                                                        ReqPatchProductStatusDto reqPatchUpdateProductStatusDto) {

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ApplicationException(ErrorCode.PRODUCT_NOT_FOUND_EXCEPTION));

        product.updateStatus(reqPatchUpdateProductStatusDto.getStatus());

        // ElasticSearch 색인 갱신
//        productIndexService.indexProduct(product);

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

        // ElasticSearch 색인 삭제
//        productSearchRepository.deleteById(product.getId().toString());

        // 상품 삭제 처리
        product.delete(username);
    }

    @Transactional(readOnly = true)
    public ResGetProductDto getProduct(UUID productId) {
        // 상품 조회
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ApplicationException(ErrorCode.PRODUCT_NOT_FOUND_EXCEPTION));

        return convertToResGetProductDtoForDetail(product);
    }

    @Transactional(readOnly = true)
    public Page<ResGetProductDto> getAllProducts(int pageNumber, int pageSize, String sortBy, String direction,
                                                 String search, List<UUID> productIds) {
        Sort sort = direction.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(pageNumber - 1, pageSize, sort);

        // ElasticSearch 검색
//        List<ProductDocument> searchResults = productSearchRepository.findBySearchAndIds(search, productIds, pageable);
        Page<Product> products = productRepository.findAllWithSearchAndPaging(search, productIds, pageable);

        // ElasticSearch 결과를 기반으로 JPA에서 상세 정보를 조회하거나 보완
//        List<ResGetProductDto> productDtos = searchResults.stream()
//                .map(this::convertToResGetProductDtoForList)
//                .collect(Collectors.toList());

        List<ResGetProductDto> productDtos = products.stream()
                .map(this::convertToResGetProductDto).toList();
        // 반환
//        return new PageImpl<>(productDtos, pageable, searchResults.size());
        return new PageImpl<>(productDtos, pageable, products.getTotalElements());
    }

    private ResGetProductDto convertToResGetProductDtoForDetail(Product product) {
        // 평점 계산
        BigDecimal rating =
                product.getRatingSum() == 0 ? BigDecimal.valueOf(0.0) : BigDecimal.valueOf(product.getRatingSum())
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

//    private ResGetProductDto convertToResGetProductDtoForList(ProductDocument productDoc) {
//        // 평점 계산
//        BigDecimal rating = productDoc.getRatingSum() == 0 || productDoc.getReviewCnt() == 0
//                ? BigDecimal.valueOf(0.0)
//                : BigDecimal.valueOf(productDoc.getRatingSum())
//                        .divide(BigDecimal.valueOf(productDoc.getReviewCnt()), 1, RoundingMode.HALF_UP);
//
//        // ResGetProductDto 생성
//        return ResGetProductDto.builder()
//                .productId(UUID.fromString(productDoc.getId()))
//                .name(productDoc.getName())
//                .price(productDoc.getPrice())
//                .quantity(productDoc.getQuantity())
//                .category(productDoc.getCategory())
//                .companyId(UUID.fromString(productDoc.getCompanyId()))
//                .description(productDoc.getDescription())
//                .detailImgs(productDoc.getDetailImgs())
//                .thumbImg(productDoc.getThumbImg())
//                .status(productDoc.getStatus())
//                .rating(rating.doubleValue())
//                .reviewCnt(productDoc.getReviewCnt())
//                .discountPrice(productDoc.getDiscountPrice())
//                .build();
//    }

    private ResGetProductDto convertToResGetProductDto(Product product) {
        // 평점 계산
        BigDecimal rating = product.getRatingSum() == 0 || product.getReviewCnt() == 0
                ? BigDecimal.valueOf(0.0)
                : BigDecimal.valueOf(product.getRatingSum())
                .divide(BigDecimal.valueOf(product.getReviewCnt()), 1, RoundingMode.HALF_UP);

        File detailImgsFile = product.getDetailImgs();
        File thumbImgFile = product.getThumbImg();
        List<String> detailImgs = detailImgsFile.getSubFiles().stream().map(SubFile::getResource).toList();
        String thumbImg = thumbImgFile.getSubFiles().get(0).getResource();

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
    
}
