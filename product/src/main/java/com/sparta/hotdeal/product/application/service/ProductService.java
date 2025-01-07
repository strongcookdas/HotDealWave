package com.sparta.hotdeal.product.application.service;

import com.sparta.hotdeal.product.application.dtos.req.product.ReqPatchProductStatusDto;
import com.sparta.hotdeal.product.application.dtos.req.product.ReqPostProductDto;
import com.sparta.hotdeal.product.application.dtos.req.product.ReqPutProductDto;
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
import com.sparta.hotdeal.product.infrastructure.repository.product.ProductRepositoryCustomImpl;
import java.math.BigDecimal;
import java.math.RoundingMode;
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
    private final ProductRepositoryCustomImpl productRepositoryCustomImpl;
    private final FileService fileService;
    private final SubFileService subFileService;

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
        Product product = Product.create(productDto.getName(), productDto.getPrice(), productDto.getQuantity(),
                productDto.getCategory(), productDto.getDescription(), detailImgs, thumbImg, productDto.getCompanyId(),
                ProductStatusEnum.ON_SALE);

        product = productRepository.save(product);

        return ResPostProductDto.of(product.getId());
    }

    public ResPutProductDto updateProduct(UUID productId, ReqPutProductDto reqPutUpdateProductDto, String username) {

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ApplicationException(ErrorCode.NOT_FOUND_EXCEPTION));

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
                .orElseThrow(() -> new ApplicationException(ErrorCode.NOT_FOUND_EXCEPTION));

        product.updateStatus(reqPatchUpdateProductStatusDto.getStatus());

        return ResPatchProductStatusDto.of(product.getId());
    }

    public void deleteProduct(UUID productId, String username) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ApplicationException(ErrorCode.NOT_FOUND_EXCEPTION));

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

    public ResPatchReduceProductQuantityDto reduceQuantity(UUID productId, int quantity, Boolean promotion) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ApplicationException(ErrorCode.NOT_FOUND_EXCEPTION));

        int newQuantity = product.getQuantity() - quantity;

        // TODO: 프로모션 상품인 경우

        product.updateQuantity(newQuantity);

        return ResPatchReduceProductQuantityDto.of(product.getId());
    }

    public ResPatchRestoreProductQuantityDto restoreQuantity(UUID productId, int quantity, Boolean promotion) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ApplicationException(ErrorCode.NOT_FOUND_EXCEPTION));

        int originQuantity = product.getQuantity() + quantity;

        // TODO: 프로모션 상품인 경우

        product.updateQuantity(originQuantity);

        return ResPatchRestoreProductQuantityDto.of(product.getId());

    }

    @Transactional(readOnly = true)
    public ResGetProductDto getProduct(UUID productId) {
        // 상품 조회
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ApplicationException(ErrorCode.NOT_FOUND_EXCEPTION));

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

    public void updateProductDiscountPrice(UUID productId, int discountPrice) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ApplicationException(ErrorCode.NOT_FOUND_EXCEPTION));

        product.updateDiscountPrice(discountPrice);
    }

}
