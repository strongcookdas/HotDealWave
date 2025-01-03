package com.sparta.hotdeal.product.presentation.controller;

import com.sparta.hotdeal.product.application.dtos.ResponseDto;
import com.sparta.hotdeal.product.application.dtos.product.ResGetPromotionDto;
import com.sparta.hotdeal.product.application.dtos.product.ResPromotionIdDto;
import com.sparta.hotdeal.product.presentation.dtos.product.ReqPostCreatePromotionDto;
import com.sparta.hotdeal.product.presentation.dtos.product.ReqPutUpdatePromotionDto;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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
public class PromotionController {

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseDto<ResPromotionIdDto> createPromotion(
            @RequestBody ReqPostCreatePromotionDto reqPostCreatePromotionDto
    ) {
        ResPromotionIdDto resPromotionIdDto = ResPromotionIdDto.builder()
                .promotionId(UUID.randomUUID())
                .build();

        return ResponseDto.of("프로모션이 생성되었습니다.", resPromotionIdDto);
    }

    @PutMapping("/{promotionId}")
    public ResponseDto<ResPromotionIdDto> updatePromotion(
            @PathVariable UUID promotionId,
            @RequestBody ReqPutUpdatePromotionDto reqPutUpdatePromotionDto
    ) {
        ResPromotionIdDto resPromotionIdDto = ResPromotionIdDto.builder()
                .promotionId(UUID.randomUUID())
                .build();

        return ResponseDto.of("프로모션이 수정되었습니다.", resPromotionIdDto);
    }

    @DeleteMapping("/{promotionId}")
    public ResponseDto<ResPromotionIdDto> deletePromotion(
            @PathVariable UUID promotionId
    ) {
        return ResponseDto.of("프로모션이 삭제되었습니다.", null);
    }

    @GetMapping("/{promotionId}")
    public ResponseDto<ResGetPromotionDto> getPromotion(
            @PathVariable UUID promotionId
    ) {
        ResGetPromotionDto resGetPromotionDto = ResGetPromotionDto.builder()
                .promotionId(UUID.randomUUID())
                .productId(UUID.randomUUID())
                .start(Timestamp.valueOf(LocalDateTime.now()))
                .end(Timestamp.valueOf(LocalDateTime.now().plusHours(3)))
                .discountRate(10)
                .discountPrice(1800)
                .quantity(8)
                .remaining(8)
                .build();

        return ResponseDto.of("프로모션이 조회되었습니다.", resGetPromotionDto);
    }

    @GetMapping()
    public ResponseDto<Page<ResGetPromotionDto>> getAllPromotions(
            @RequestParam(defaultValue = "1") int page_number,
            @RequestParam(defaultValue = "10") int page_size,
            @RequestParam(defaultValue = "createdAt") String sort_by,
            @RequestParam(defaultValue = "asc") String direction,
            @RequestParam(required = false) String search
    ) {
        ResGetPromotionDto resGetPromotionDto1 = ResGetPromotionDto.builder()
                .promotionId(UUID.randomUUID())
                .productId(UUID.randomUUID())
                .start(Timestamp.valueOf(LocalDateTime.now()))
                .end(Timestamp.valueOf(LocalDateTime.now().plusHours(3)))
                .discountRate(10)
                .discountPrice(1800)
                .quantity(8)
                .remaining(8)
                .build();

        ResGetPromotionDto resGetPromotionDto2 = ResGetPromotionDto.builder()
                .promotionId(UUID.randomUUID())
                .productId(UUID.randomUUID())
                .start(Timestamp.valueOf(LocalDateTime.now()))
                .end(Timestamp.valueOf(LocalDateTime.now().plusHours(1)))
                .discountRate(10)
                .discountPrice(1800)
                .quantity(8)
                .remaining(8)
                .build();

        List<ResGetPromotionDto> promotionList = List.of(resGetPromotionDto1, resGetPromotionDto2);

        // Pageable 생성
        Pageable pageable = PageRequest.of(page_number - 1, page_size,
                "asc".equalsIgnoreCase(direction) ? Sort.by(sort_by).ascending() : Sort.by(sort_by).descending());

        // Page 객체 생성
        Page<ResGetPromotionDto> promotionPage = new PageImpl<>(promotionList, pageable, promotionList.size());

        // ResponseDto로 감싸서 반환
        return ResponseDto.of("상품이 조회되었습니다.", promotionPage);
    }
}
