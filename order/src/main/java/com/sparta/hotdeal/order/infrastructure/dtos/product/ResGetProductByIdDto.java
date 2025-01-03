package com.sparta.hotdeal.order.infrastructure.dtos.product;

import com.sparta.hotdeal.order.application.dtos.product.ProductDto;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
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

    public ProductDto toDto(){
        return ProductDto.create(productId, name, price, category, thumbImg, discountPrice);
    }

    // 더미 데이터를 반환하는 정적 메서드
    public static ResGetProductByIdDto createDummy() {
        return ResGetProductByIdDto.builder()
                .productId(UUID.randomUUID())
                .name("Sample Product")
                .price(10000)
                .quantity(50)
                .category("Electronics")
                .companyId(UUID.randomUUID())
                .description("This is a sample product description.")
                .detailImgs(Arrays.asList("img1.jpg", "img2.jpg"))
                .thumbImg("thumb.jpg")
                .status("AVAILABLE")
                .rating(4.5)
                .reviewCnt(120)
                .discountPrice(8000)
                .build();
    }
}
