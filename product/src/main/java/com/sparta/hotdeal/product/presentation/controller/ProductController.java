package com.sparta.hotdeal.product.presentation.controller;

import com.sparta.hotdeal.product.application.dtos.req.product.ReqPatchProductQuantityDto;
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
import com.sparta.hotdeal.product.application.service.product.ProductService;
import com.sparta.hotdeal.product.infrastructure.custom.RequestUserDetails;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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

@RestController
@RequestMapping("/api/v1/products")
@RequiredArgsConstructor
@Tag(name = "Product API", description = "상품 관련 API")
public class ProductController {

    private final ProductService productService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Secured({"ROLE_MASTER", "ROLE_MANAGER", "ROLE_SELLER"})
    @Operation(summary = "상품 생성 API", description = "상품을 생성합니다.")
    public ResponseDto<ResPostProductDto> createProduct(
            @AuthenticationPrincipal RequestUserDetails userDetails,
            @ModelAttribute ReqPostProductDto reqPostCreateProductDto
    ) {
        ResPostProductDto resPostProductDto = productService.createProduct(reqPostCreateProductDto);
        return ResponseDto.of("상품이 생성되었습니다.", resPostProductDto);
    }

    @PutMapping("/{productId}")
    @Secured({"ROLE_MASTER", "ROLE_MANAGER", "ROLE_SELLER"})
    @Operation(summary = "상품 수정 API", description = "상품을 수정합니다.")
    public ResponseDto<ResPutProductDto> updateProduct(
            @AuthenticationPrincipal RequestUserDetails userDetails,
            @PathVariable UUID productId,
            @ModelAttribute ReqPutProductDto reqPutUpdateProductDto
    ) {
        ResPutProductDto resPutProductDto = productService.updateProduct(productId, reqPutUpdateProductDto,
                userDetails.getEmail());

        return ResponseDto.of("상품이 수정되었습니다.", resPutProductDto);
    }

    @PatchMapping("/{productId}")
    @Secured({"ROLE_MASTER"})
    @Operation(summary = "상품 상태 수정 API", description = "상품을 상태를 수정합니다.")
    public ResponseDto<ResPatchProductStatusDto> updateProductStatus(
            @AuthenticationPrincipal RequestUserDetails userDetails,
            @PathVariable UUID productId,
            @RequestBody ReqPatchProductStatusDto reqPatchUpdateProductStatusDto
    ) {
        ResPatchProductStatusDto resPatchProductStatusDto = productService.updateProductStatus(productId,
                reqPatchUpdateProductStatusDto);

        return ResponseDto.of("상품이 수정되었습니다.", resPatchProductStatusDto);
    }

    @DeleteMapping("/{productId}")
    @Secured({"ROLE_MASTER", "ROLE_MANAGER", "ROLE_SELLER"})
    @Operation(summary = "상품 삭제 API", description = "상품을 삭제합니다.")
    public ResponseDto<Void> deleteProduct(
            @AuthenticationPrincipal RequestUserDetails userDetails,
            @PathVariable UUID productId
    ) {
        productService.deleteProduct(productId, userDetails.getEmail());

        return ResponseDto.of("상품이 삭제되었습니다.", null);
    }

    @PutMapping("/reduce-quantity")
    @Secured({"ROLE_MASTER"})
    @Operation(summary = "상품 수량 감소 API", description = "상품의 수량을 감소합니다.")
    public ResponseDto<List<ResPatchReduceProductQuantityDto>> reduceQuantity(
            @AuthenticationPrincipal RequestUserDetails userDetails,
            @RequestBody List<ReqPatchProductQuantityDto> reqPatchProductQuantityDto
    ) {
        List<ResPatchReduceProductQuantityDto> resPatchReduceProductQuantityDto = productService.reduceQuantity(
                reqPatchProductQuantityDto);

        return ResponseDto.of("상품이 수정되었습니다.", resPatchReduceProductQuantityDto);
    }

    @PutMapping("/restore-quantity")
    @Secured({"ROLE_MASTER"})
    @Operation(summary = "상품 수량 복구 API", description = "상품의 수량을 복구합니다.")
    public ResponseDto<List<ResPatchRestoreProductQuantityDto>> restoreQuantity(
            @AuthenticationPrincipal RequestUserDetails userDetails,
            @RequestBody List<ReqPatchProductQuantityDto> reqPatchProductQuantityDto
    ) {
        List<ResPatchRestoreProductQuantityDto> resPatchRestoreProductQuantityDto = productService.restoreQuantity(
                reqPatchProductQuantityDto);

        return ResponseDto.of("상품이 수정되었습니다.", resPatchRestoreProductQuantityDto);
    }

    @GetMapping("/{productId}")
    @Operation(summary = "상품 상세 조회 API", description = "상품을 조회합니다.")
    public ResponseDto<ResGetProductDto> getProduct(
            @AuthenticationPrincipal RequestUserDetails userDetails,
            @PathVariable UUID productId
    ) {
        ResGetProductDto resGetProductDto = productService.getProduct(productId);

        return ResponseDto.of("상품이 조회되었습니다.", resGetProductDto);
    }

    @GetMapping()
    @Operation(summary = "상품 목록 조회 API", description = "상품의 목록을 조회합니다.")
    public ResponseDto<Page<ResGetProductDto>> getAllProducts(
            @AuthenticationPrincipal RequestUserDetails userDetails,
            @RequestParam(defaultValue = "1") int page_number,
            @RequestParam(defaultValue = "10") int page_size,
            @RequestParam(defaultValue = "createdAt") String sort_by,
            @RequestParam(defaultValue = "asc") String direction,
            @RequestParam(required = false) String search,
            @RequestParam(required = false) List<UUID> productIds
    ) {
        Page<ResGetProductDto> productPage = productService.getAllProducts(page_number, page_size, sort_by, direction,
                search, productIds);

        // ResponseDto로 감싸서 반환
        return ResponseDto.of("상품이 조회되었습니다.", productPage);
    }
}
