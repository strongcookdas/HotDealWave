package com.sparta.hotdeal.product.presentation.controller;

import com.sparta.hotdeal.product.application.dtos.ResponseDto;
import com.sparta.hotdeal.product.application.dtos.product.ResGetProductDto;
import com.sparta.hotdeal.product.application.dtos.product.ResProductIdDto;
import com.sparta.hotdeal.product.domain.entity.product.ProductCategoryEnum;
import com.sparta.hotdeal.product.domain.entity.product.ProductStatusEnum;
import com.sparta.hotdeal.product.presentation.dtos.product.ReqPatchUpdateProductStatusDto;
import com.sparta.hotdeal.product.presentation.dtos.product.ReqPostCreateProductDto;
import com.sparta.hotdeal.product.presentation.dtos.product.ReqPutUpdateProductDto;
import java.util.List;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/products")
public class ProductController {

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseDto<ResProductIdDto> createProduct(
            @RequestBody ReqPostCreateProductDto reqPostCreateProductDto
    ) {
        ResProductIdDto resProductIdDto = ResProductIdDto.builder()
                .productId(UUID.randomUUID())
                .build();

        return ResponseDto.of("상품이 생성되었습니다.", resProductIdDto);
    }

    @PutMapping("/{productId}")
    public ResponseDto<ResProductIdDto> updateProduct(
            @PathVariable UUID productId,
            @RequestBody ReqPutUpdateProductDto reqPutUpdateProductDto
    ) {
        ResProductIdDto resProductIdDto = ResProductIdDto.builder()
                .productId(UUID.randomUUID())
                .build();

        return ResponseDto.of("상품이 수정되었습니다.", resProductIdDto);
    }

    @PatchMapping("/{productId}")
    public ResponseDto<ResProductIdDto> updateProductStatus(
            @PathVariable UUID productId,
            @RequestBody ReqPatchUpdateProductStatusDto reqPatchUpdateProductStatusDto
    ) {
        ResProductIdDto resProductIdDto = ResProductIdDto.builder()
                .productId(UUID.randomUUID())
                .build();

        return ResponseDto.of("상품이 수정되었습니다.", resProductIdDto);
    }

    @PatchMapping("/{productId}/reduceQuantity")
    public ResponseDto<ResProductIdDto> reduceQuantity(
            @PathVariable UUID productId,
            @RequestParam int quantity
    ) {
        ResProductIdDto resProductIdDto = ResProductIdDto.builder()
                .productId(UUID.randomUUID())
                .build();

        return ResponseDto.of("상품이 수정되었습니다.", resProductIdDto);
    }

    @PatchMapping("/{productId}/restoreQuantity")
    public ResponseDto<ResProductIdDto> restoreQuantity(
            @PathVariable UUID productId,
            @RequestParam int quantity
    ) {
        ResProductIdDto resProductIdDto = ResProductIdDto.builder()
                .productId(UUID.randomUUID())
                .build();

        return ResponseDto.of("상품이 수정되었습니다.", resProductIdDto);
    }

    @GetMapping("/{productId}")
    public ResponseDto<ResGetProductDto> getProduct(
            @PathVariable UUID productId
    ) {
        ResGetProductDto resGetProductDto = ResGetProductDto.builder()
                .productId(UUID.randomUUID())
                .name("노트")
                .price(1000)
                .quantity(100)
                .category(ProductCategoryEnum.OFFICE_SUPPLIES)
                .companyId(UUID.randomUUID())
                .description("줄선 노트입니다.")
                .detailImgs(List.of("img1", "img2"))
                .thumbImg("img")
                .status(ProductStatusEnum.ON_SALE)
                .rating(3.5)
                .reviewCnt(3)
                .discountPrice(null)
                .build();

        return ResponseDto.of("상품이 조회되었습니다.", resGetProductDto);
    }

    @GetMapping()
    public ResponseDto<Page<ResGetProductDto>> getAllProducts(
            @RequestParam(defaultValue = "1") int page_number,
            @RequestParam(defaultValue = "10") int page_size,
            @RequestParam(defaultValue = "createdAt") String sort_by,
            @RequestParam(defaultValue = "asc") String direction,
            @RequestParam(required = false) String search
    ) {
        ResGetProductDto resGetProductDto1 = ResGetProductDto.builder()
                .productId(UUID.randomUUID())
                .name("노트")
                .price(1000)
                .quantity(100)
                .category(ProductCategoryEnum.OFFICE_SUPPLIES)
                .companyId(UUID.randomUUID())
                .description("줄선 노트입니다.")
                .detailImgs(List.of("img1", "img2"))
                .thumbImg("img")
                .status(ProductStatusEnum.ON_SALE)
                .rating(3.5)
                .reviewCnt(3)
                .discountPrice(null)
                .build();

        ResGetProductDto resGetProductDto2 = ResGetProductDto.builder()
                .productId(UUID.randomUUID())
                .name("볼펜")
                .price(700)
                .quantity(100)
                .category(ProductCategoryEnum.OFFICE_SUPPLIES)
                .companyId(UUID.randomUUID())
                .description("검정색 볼펜입니다.")
                .detailImgs(List.of("img1", "img2"))
                .thumbImg("img")
                .status(ProductStatusEnum.ON_SALE)
                .rating(4.3)
                .reviewCnt(7)
                .discountPrice(null)
                .build();

        List<ResGetProductDto> productList = List.of(resGetProductDto1, resGetProductDto2);

        // Pageable 생성
        Pageable pageable = PageRequest.of(page_number - 1, page_size,
                "asc".equalsIgnoreCase(direction) ? Sort.by(sort_by).ascending() : Sort.by(sort_by).descending());

        // Page 객체 생성
        Page<ResGetProductDto> productPage = new PageImpl<>(productList, pageable, productList.size());

        // ResponseDto로 감싸서 반환
        return ResponseDto.of("상품이 조회되었습니다.", productPage);
    }
}
