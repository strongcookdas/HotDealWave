package com.sparta.hotdeal.order.infrastructure.dtos.product.res;

import com.sparta.hotdeal.order.application.dtos.product.ProductDto;
import java.util.List;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;

@Getter
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

    public static List<ResGetProductListDto> createDummyDataList() {
        ResGetProductListDto resGetProductDto1 = ResGetProductListDto.builder()
                .productId(UUID.fromString("76093f6e-64bb-43fa-8a1c-e9eeba6f457c"))
                .name("노트")
                .price(1000)
                .quantity(100)
                .category("OFFICE_SUPPLIES")
                .companyId(UUID.randomUUID())
                .description("줄선 노트입니다.")
                .detailImgs(List.of("img1", "img2"))
                .thumbImg("img")
                .status("ON_SALE")
                .rating(3.5)
                .reviewCnt(3)
                .discountPrice(null)
                .build();

        ResGetProductListDto resGetProductDto2 = ResGetProductListDto.builder()
                .productId(UUID.fromString("f50526d1-76e2-43e0-834b-c00ec26dc849"))
                .name("볼펜")
                .price(700)
                .quantity(100)
                .category("OFFICE_SUPPLIES")
                .companyId(UUID.randomUUID())
                .description("검정색 볼펜입니다.")
                .detailImgs(List.of("img1", "img2"))
                .thumbImg("img")
                .status("ON_SALE")
                .rating(4.3)
                .reviewCnt(7)
                .discountPrice(null)
                .build();

        return List.of(resGetProductDto1, resGetProductDto2);
    }

    public ProductDto toGetProductListForOrderDto() {
        return ProductDto.create(
                productId,
                name,
                price,
                category,
                thumbImg,
                discountPrice,
                status,
                rating,
                reviewCnt,
                quantity,
                companyId
        );
    }
}
