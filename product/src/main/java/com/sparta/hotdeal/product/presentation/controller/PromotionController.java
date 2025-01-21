package com.sparta.hotdeal.product.presentation.controller;

import com.sparta.hotdeal.product.application.dtos.req.promotion.ReqPostPromotionDto;
import com.sparta.hotdeal.product.application.dtos.req.promotion.ReqPutPromotionDto;
import com.sparta.hotdeal.product.application.dtos.res.ResponseDto;
import com.sparta.hotdeal.product.application.dtos.res.promotion.ResGetPromotionDto;
import com.sparta.hotdeal.product.application.dtos.res.promotion.ResPostPromotionDto;
import com.sparta.hotdeal.product.application.dtos.res.promotion.ResPutPromotionDto;
import com.sparta.hotdeal.product.application.service.promotion.PromotionService;
import com.sparta.hotdeal.product.domain.entity.promotion.PromotionStatusEnum;
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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/v1/promotions")
@RequiredArgsConstructor
@Tag(name = "Promotion API", description = "프로모션 관련 API")
public class PromotionController {

    private final PromotionService promotionService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Secured({"ROLE_MASTER", "ROLE_MANAGER", "ROLE_SELLER"})
    @Operation(summary = "프로모션 생성 API", description = "프로모션을 생성합니다.")
    public ResponseDto<ResPostPromotionDto> createPromotion(
            @AuthenticationPrincipal RequestUserDetails userDetails,
            @RequestBody ReqPostPromotionDto reqPostCreatePromotionDto
    ) {
        ResPostPromotionDto resPostPromotionDto = promotionService.createPromotion(reqPostCreatePromotionDto);

        return ResponseDto.of("프로모션이 생성되었습니다.", resPostPromotionDto);
    }

    @PutMapping("/{promotionId}")
    @Secured({"ROLE_MASTER", "ROLE_MANAGER", "ROLE_SELLER"})
    @Operation(summary = "프로모션 수정 API", description = "프로모션을 수정합니다.")
    public ResponseDto<ResPutPromotionDto> updatePromotion(
            @AuthenticationPrincipal RequestUserDetails userDetails,
            @PathVariable UUID promotionId,
            @RequestBody ReqPutPromotionDto reqPutUpdatePromotionDto
    ) {
        ResPutPromotionDto resPutPromotionDto = promotionService.updatePromotion(promotionId, reqPutUpdatePromotionDto);

        return ResponseDto.of("프로모션이 수정되었습니다.", resPutPromotionDto);
    }

    @DeleteMapping("/{promotionId}")
    @Secured({"ROLE_MASTER", "ROLE_MANAGER", "ROLE_SELLER"})
    @Operation(summary = "프로모션 삭제 API", description = "프로모션을 삭제합니다.")
    public ResponseDto<Void> deletePromotion(
            @AuthenticationPrincipal RequestUserDetails userDetails,
            @PathVariable UUID promotionId
    ) {
        promotionService.deletePromotion(promotionId, userDetails.getEmail());

        return ResponseDto.of("프로모션이 삭제되었습니다.", null);
    }

    @GetMapping("/{promotionId}")
    @Operation(summary = "프로모션 상세 조회 API", description = "프로모션을 조회합니다.")
    public ResponseDto<ResGetPromotionDto> getPromotion(
            @AuthenticationPrincipal RequestUserDetails userDetails,
            @PathVariable UUID promotionId
    ) {
        ResGetPromotionDto resGetPromotionDto = promotionService.getPromotion(promotionId);

        return ResponseDto.of("프로모션이 조회되었습니다.", resGetPromotionDto);
    }

    @GetMapping()
    @Operation(summary = "프로모션 목록 조회 API", description = "프로모션 목록을 조회합니다.")
    public ResponseDto<Page<ResGetPromotionDto>> getAllPromotions(
            @AuthenticationPrincipal RequestUserDetails userDetails,
            @RequestParam(defaultValue = "1") int page_number,
            @RequestParam(defaultValue = "10") int page_size,
            @RequestParam(defaultValue = "createdAt") String sort_by,
            @RequestParam(defaultValue = "asc") String direction,
            @RequestParam(required = false) List<UUID> productIds,
            @RequestParam(required = false) PromotionStatusEnum status
    ) {
        Page<ResGetPromotionDto> promotionPage = promotionService.getAllPromotions(page_number, page_size, sort_by,
                direction, productIds, status);

        // ResponseDto로 감싸서 반환
        return ResponseDto.of("타임 세일이 조회되었습니다.", promotionPage);
    }
}
