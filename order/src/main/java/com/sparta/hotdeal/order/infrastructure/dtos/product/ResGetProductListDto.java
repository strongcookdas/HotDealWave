package com.sparta.hotdeal.order.infrastructure.dtos.product;

import com.sparta.hotdeal.order.application.dtos.product.ProductListDto;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder(access = AccessLevel.PRIVATE)
public class ResGetProductListDto {
    private UUID productId;
    private String name;
    private Integer price;
    private Integer quantity;
    private String category;
    private UUID companyId;
    private String description;
    private List<String> detailImgs;
    private String thumbImg;
    private String status;
    private double rating;
    private Integer reviewCnt;
    private Integer discountPrice;

    public ProductListDto toDto(){
        return ProductListDto.create(productId, name, price, category, thumbImg, discountPrice);
    }

    // UUID 리스트를 기반으로 더미 데이터를 생성하는 메서드
    public static List<ResGetProductListDto> createDummyListFromIds(List<UUID> productIds) {
        List<ResGetProductListDto> dummyList = new ArrayList<>();
        int index = 1;

        for (UUID productId : productIds) {
            dummyList.add(
                    ResGetProductListDto.builder()
                            .productId(productId)
                            .name("Sample Product " + index)
                            .price(10000 * index) // 임의의 가격
                            .quantity(50 + index) // 임의의 수량
                            .category("Category " + index) // 임의의 카테고리
                            .companyId(UUID.randomUUID()) // 임의의 회사 ID
                            .description("This is a sample description for product " + index)
                            .detailImgs(Arrays.asList("detail1.jpg", "detail2.jpg")) // 임의의 상세 이미지
                            .thumbImg("thumb" + index + ".jpg") // 임의의 썸네일
                            .status("AVAILABLE") // 임의의 상태
                            .rating(4.0 + (index * 0.1)) // 임의의 평점
                            .reviewCnt(10 * index) // 임의의 리뷰 수
                            .discountPrice(8000 * index) // 임의의 할인 가격
                            .build()
            );
            index++;
        }

        return dummyList;
    }

}
