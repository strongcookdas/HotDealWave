package com.sparta.hotdeal.product.presentation.controller;

import com.sparta.hotdeal.product.application.dtos.req.product.ReqPostPromotionDto;
import com.sparta.hotdeal.product.application.dtos.req.product.ReqPutPromotionDto;
import com.sparta.hotdeal.product.application.dtos.res.ResponseDto;
import com.sparta.hotdeal.product.application.dtos.res.product.ResGetPromotionDto;
import com.sparta.hotdeal.product.application.dtos.res.product.ResPostPromotionDto;
import com.sparta.hotdeal.product.application.dtos.res.product.ResPutPromotionDto;
import com.sparta.hotdeal.product.application.service.PromotionService;
import com.sparta.hotdeal.product.domain.entity.product.PromotionStatusEnum;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
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
public class PromotionController {

    private final PromotionService promotionService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseDto<ResPostPromotionDto> createPromotion(
            @RequestBody ReqPostPromotionDto reqPostCreatePromotionDto
    ) {
//        ResPostPromotionDto resPostPromotionDto = ResPostPromotionDto.builder()
//                .promotionId(UUID.randomUUID())
//                .build();
        ResPostPromotionDto resPostPromotionDto = promotionService.createPromotion(reqPostCreatePromotionDto);

        return ResponseDto.of("프로모션이 생성되었습니다.", resPostPromotionDto);
    }

    @PutMapping("/{promotionId}")
    public ResponseDto<ResPutPromotionDto> updatePromotion(
            @PathVariable UUID promotionId,
            @RequestBody ReqPutPromotionDto reqPutUpdatePromotionDto
    ) {
//        ResPutPromotionDto resPutPromotionDto = ResPutPromotionDto.builder()
//                .promotionId(UUID.randomUUID())
//                .build();
        ResPutPromotionDto resPutPromotionDto = promotionService.updatePromotion(promotionId, reqPutUpdatePromotionDto);

        return ResponseDto.of("프로모션이 수정되었습니다.", resPutPromotionDto);
    }

    @DeleteMapping("/{promotionId}")
    public ResponseDto<Void> deletePromotion(
            @PathVariable UUID promotionId
    ) {
        // 임시 user
        String username = "testUser";
        promotionService.deletePromotion(promotionId, username);

        return ResponseDto.of("프로모션이 삭제되었습니다.", null);
    }

    @GetMapping("/{promotionId}")
    public ResponseDto<ResGetPromotionDto> getPromotion(
            @PathVariable UUID promotionId
    ) {
//        ResGetPromotionDto resGetPromotionDto = ResGetPromotionDto.builder()
//                .promotionId(UUID.randomUUID())
//                .productId(UUID.randomUUID())
//                .start(Timestamp.valueOf(LocalDateTime.now()))
//                .end(Timestamp.valueOf(LocalDateTime.now().plusHours(3)))
//                .discountRate(10)
//                .discountPrice(1800)
//                .quantity(8)
//                .remaining(8)
//                .build();
        ResGetPromotionDto resGetPromotionDto = promotionService.getPromotion(promotionId);

        return ResponseDto.of("프로모션이 조회되었습니다.", resGetPromotionDto);
    }

    @GetMapping()
    public ResponseDto<Page<ResGetPromotionDto>> getAllPromotions(
            @RequestParam(defaultValue = "1") int page_number,
            @RequestParam(defaultValue = "10") int page_size,
            @RequestParam(defaultValue = "createdAt") String sort_by,
            @RequestParam(defaultValue = "asc") String direction,
            @RequestParam(required = false) List<UUID> productIds,
            @RequestParam(required = false) PromotionStatusEnum status
    ) {
//        ResGetPromotionDto resGetPromotionDto1 = ResGetPromotionDto.builder()
//                .promotionId(UUID.randomUUID())
//                .productId(UUID.randomUUID())
//                .start(Timestamp.valueOf(LocalDateTime.now()))
//                .end(Timestamp.valueOf(LocalDateTime.now().plusHours(3)))
//                .discountRate(10)
//                .discountPrice(1800)
//                .quantity(8)
//                .remaining(8)
//                .build();
//
//        ResGetPromotionDto resGetPromotionDto2 = ResGetPromotionDto.builder()
//                .promotionId(UUID.randomUUID())
//                .productId(UUID.randomUUID())
//                .start(Timestamp.valueOf(LocalDateTime.now()))
//                .end(Timestamp.valueOf(LocalDateTime.now().plusHours(1)))
//                .discountRate(10)
//                .discountPrice(1800)
//                .quantity(8)
//                .remaining(8)
//                .build();
//
//        List<ResGetPromotionDto> promotionList = List.of(resGetPromotionDto1, resGetPromotionDto2);
//
//        // Pageable 생성
//        Pageable pageable = PageRequest.of(page_number - 1, page_size,
//                "asc".equalsIgnoreCase(direction) ? Sort.by(sort_by).ascending() : Sort.by(sort_by).descending());
//
//        // Page 객체 생성
//        Page<ResGetPromotionDto> promotionPage = new PageImpl<>(promotionList, pageable, promotionList.size());

        Page<ResGetPromotionDto> promotionPage = promotionService.getAllPromotions(page_number, page_size, sort_by,
                direction, productIds, status);

        // ResponseDto로 감싸서 반환
        return ResponseDto.of("타임 세일이 조회되었습니다.", promotionPage);
    }
}
