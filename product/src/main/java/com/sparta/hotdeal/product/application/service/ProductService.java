package com.sparta.hotdeal.product.application.service;

import com.sparta.hotdeal.product.application.dtos.req.product.ReqPostProductDto;
import com.sparta.hotdeal.product.application.dtos.res.product.ResPostProductDto;
import com.sparta.hotdeal.product.application.service.client.CompanyClientService;
import com.sparta.hotdeal.product.domain.entity.product.File;
import com.sparta.hotdeal.product.domain.entity.product.Product;
import com.sparta.hotdeal.product.domain.entity.product.ProductStatusEnum;
import com.sparta.hotdeal.product.domain.repository.product.ProductRepository;
import com.sparta.hotdeal.product.domain.repository.product.ProductRepositoryCustomImpl;
import com.sparta.hotdeal.product.infrastructure.dtos.ResGetCompanyByIdDto;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
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

    @Transactional
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
}
