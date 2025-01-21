package com.sparta.hotdeal.order.infrastructure.dtos.product.res;

import com.sparta.hotdeal.order.application.dtos.product.ProductByIdtDto;
import java.util.List;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder(access = AccessLevel.PRIVATE)
public class ResGetProductByIdDto {
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

    public static ResGetProductByIdDto createDummyData(UUID productId) {
        return ResGetProductByIdDto.builder()
                .productId(productId)
                .name("product")
                .price(10000)
                .quantity(100)
                .category("category")
                .companyId(UUID.randomUUID())
                .description("description")
                .detailImgs(List.of("image1", "image2"))
                .thumbImg("image")
                .status("ON_SALE")
                .rating(3.5)
                .reviewCnt(5)
                .discountPrice(null)
                .build();
    }

    public ProductByIdtDto toDto() {
        return ProductByIdtDto.create(
                productId,
                name,
                price,
                category,
                thumbImg,
                discountPrice,
                status,
                rating,
                reviewCnt,
                quantity
        );
    }
}
