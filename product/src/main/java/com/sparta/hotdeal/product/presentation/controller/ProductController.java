package com.sparta.hotdeal.product.presentation.controller;

import com.sparta.hotdeal.product.application.dtos.req.product.ReqPatchProductStatusDto;
import com.sparta.hotdeal.product.application.dtos.req.product.ReqPostProductDto;
import com.sparta.hotdeal.product.application.dtos.req.product.ReqPutProductDto;
import com.sparta.hotdeal.product.application.dtos.res.ResponseDto;
import com.sparta.hotdeal.product.application.dtos.res.product.ResGetProductDto;
import com.sparta.hotdeal.product.application.dtos.res.product.ResPatchProductStatusDto;
import com.sparta.hotdeal.product.application.dtos.res.product.ResPatchReduceProductQuantityDto;
import com.sparta.hotdeal.product.application.dtos.res.product.ResPatchRestoreProductQuantityDto;
import com.sparta.hotdeal.product.application.dtos.res.product.ResPostProductDto;
import com.sparta.hotdeal.product.application.dtos.res.product.ResPutProductDto;
import com.sparta.hotdeal.product.application.service.ProductService;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/v1/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseDto<ResPostProductDto> createProduct(
            @ModelAttribute ReqPostProductDto reqPostCreateProductDto
    ) {
//        ResPostProductDto resPostProductDto = ResPostProductDto.builder()
//                .productId(UUID.randomUUID())
//                .build();

        ResPostProductDto resPostProductDto = productService.createProduct(reqPostCreateProductDto);
        return ResponseDto.of("상품이 생성되었습니다.", resPostProductDto);
    }

    @PutMapping("/{productId}")
    public ResponseDto<ResPutProductDto> updateProduct(
            @PathVariable UUID productId,
            @ModelAttribute ReqPutProductDto reqPutUpdateProductDto
    ) {
//        ResPutProductDto resPutProductDto = ResPutProductDto.builder()
//                .productId(UUID.randomUUID())
//                .build();

        // 임시 유저 정보
        String deletedBy = "testUser";

        ResPutProductDto resPutProductDto = productService.updateProduct(productId, reqPutUpdateProductDto, deletedBy);

        return ResponseDto.of("상품이 수정되었습니다.", resPutProductDto);
    }

    @PatchMapping("/{productId}")
    public ResponseDto<ResPatchProductStatusDto> updateProductStatus(
            @PathVariable UUID productId,
            @RequestBody ReqPatchProductStatusDto reqPatchUpdateProductStatusDto
    ) {
//        ResPatchProductStatusDto resPatchProductStatusDto = ResPatchProductStatusDto.builder()
//                .productId(UUID.randomUUID())
//                .build();

        ResPatchProductStatusDto resPatchProductStatusDto = productService.updateProductStatus(productId,
                reqPatchUpdateProductStatusDto);

        return ResponseDto.of("상품이 수정되었습니다.", resPatchProductStatusDto);
    }

    @DeleteMapping("/{productId}")
    public ResponseDto<Void> deleteProduct(
            @PathVariable UUID productId
    ) {
        // 임시 user
        String username = "testUser";
        productService.deleteProduct(productId, username);

        return ResponseDto.of("상품이 삭제되었습니다.", null);
    }

    @PatchMapping("/{productId}/reduceQuantity")
    public ResponseDto<ResPatchReduceProductQuantityDto> reduceQuantity(
            @PathVariable UUID productId,
            @RequestParam int quantity,
            @RequestParam Boolean promotion
    ) {
//        ResPatchReduceProductQuantityDto resPatchReduceProductQuantityDto = ResPatchReduceProductQuantityDto.builder()
//                .productId(UUID.randomUUID())
//                .build();
        ResPatchReduceProductQuantityDto resPatchReduceProductQuantityDto = productService.reduceQuantity(productId,
                quantity, promotion);

        return ResponseDto.of("상품이 수정되었습니다.", resPatchReduceProductQuantityDto);
    }

    @PatchMapping("/{productId}/restoreQuantity")
    public ResponseDto<ResPatchRestoreProductQuantityDto> restoreQuantity(
            @PathVariable UUID productId,
            @RequestParam int quantity,
            @RequestParam Boolean promotion
    ) {
//        ResPatchRestoreProductQuantityDto resPatchRestoreProductQuantityDto = ResPatchRestoreProductQuantityDto.builder()
//                .productId(UUID.randomUUID())
//                .build();
        ResPatchRestoreProductQuantityDto resPatchRestoreProductQuantityDto = productService.restoreQuantity(productId,
                quantity, promotion);

        return ResponseDto.of("상품이 수정되었습니다.", resPatchRestoreProductQuantityDto);
    }

    @GetMapping("/{productId}")
    public ResponseDto<ResGetProductDto> getProduct(
            @PathVariable UUID productId
    ) {
//        ResGetProductDto resGetProductDto = ResGetProductDto.builder()
//                .productId(UUID.randomUUID())
//                .name("노트")
//                .price(1000)
//                .quantity(100)
//                .category(ProductCategoryEnum.OFFICE_SUPPLIES)
//                .companyId(UUID.randomUUID())
//                .description("줄선 노트입니다.")
//                .detailImgs(List.of("img1", "img2"))
//                .thumbImg("img")
//                .status(ProductStatusEnum.ON_SALE)
//                .rating(3.5)
//                .reviewCnt(3)
//                .discountPrice(null)
//                .build();
        ResGetProductDto resGetProductDto = productService.getProduct(productId);

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
//        ResGetProductDto resGetProductDto1 = ResGetProductDto.builder()
//                .productId(UUID.randomUUID())
//                .name("노트")
//                .price(1000)
//                .quantity(100)
//                .category(ProductCategoryEnum.OFFICE_SUPPLIES)
//                .companyId(UUID.randomUUID())
//                .description("줄선 노트입니다.")
//                .detailImgs(List.of("img1", "img2"))
//                .thumbImg("img")
//                .status(ProductStatusEnum.ON_SALE)
//                .rating(3.5)
//                .reviewCnt(3)
//                .discountPrice(null)
//                .build();
//
//        ResGetProductDto resGetProductDto2 = ResGetProductDto.builder()
//                .productId(UUID.randomUUID())
//                .name("볼펜")
//                .price(700)
//                .quantity(100)
//                .category(ProductCategoryEnum.OFFICE_SUPPLIES)
//                .companyId(UUID.randomUUID())
//                .description("검정색 볼펜입니다.")
//                .detailImgs(List.of("img1", "img2"))
//                .thumbImg("img")
//                .status(ProductStatusEnum.ON_SALE)
//                .rating(4.3)
//                .reviewCnt(7)
//                .discountPrice(null)
//                .build();
//
//        List<ResGetProductDto> productList = List.of(resGetProductDto1, resGetProductDto2);
//
//        // Pageable 생성
//        Pageable pageable = PageRequest.of(page_number - 1, page_size,
//                "asc".equalsIgnoreCase(direction) ? Sort.by(sort_by).ascending() : Sort.by(sort_by).descending());
//
//        // Page 객체 생성
//        Page<ResGetProductDto> productPage = new PageImpl<>(productList, pageable, productList.size());

        Page<ResGetProductDto> productPage = productService.getAllProducts(page_number, page_size, sort_by, direction,
                search);

        // ResponseDto로 감싸서 반환
        return ResponseDto.of("상품이 조회되었습니다.", productPage);
    }
}
