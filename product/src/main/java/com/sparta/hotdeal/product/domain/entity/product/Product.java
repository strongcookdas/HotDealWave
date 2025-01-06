package com.sparta.hotdeal.product.domain.entity.product;

import com.sparta.hotdeal.product.domain.entity.AuditingDate;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Where;

@Getter
@Entity
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder(access = AccessLevel.PRIVATE)
@Table(name = "p_product")
@Where(clause = "deleted_at IS NULL")
public class Product extends AuditingDate {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private Integer price;

    @Column(nullable = false)
    private Integer quantity;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private ProductCategoryEnum category;

    @Column
    private String description;

    // detailImgs와 1:1 관계 설정
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "detail_imgs", nullable = false)
    private File detailImgs;

    // thumbImg와 1:1 관계 설정
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "thumb_img", nullable = false)
    private File thumbImg;

    @Column(nullable = false)
    private UUID companyId;

    @Column
    @Enumerated(EnumType.STRING)
    private ProductStatusEnum status;

    @Column
    private Integer ratingSum;

    @Column
    private Integer reviewCnt;

    @Column
    private Integer discountPrice;

    public static Product create(
            String name,
            int price,
            int quantity,
            ProductCategoryEnum category,
            String description,
            File detailImgs,
            File thumbImg,
            UUID companyId,
            ProductStatusEnum status
    ) {
        return Product.builder()
                .name(name)
                .price(price)
                .quantity(quantity)
                .category(category)
                .description(description)
                .detailImgs(detailImgs)
                .thumbImg(thumbImg)
                .companyId(companyId)
                .status(status)
                .build();
    }

    public void update(
            String name,
            Integer price,
            Integer quantity,
            ProductCategoryEnum category,
            String description,
            ProductStatusEnum status
    ) {
        if (name != null) {
            this.name = name;
        }
        if (price != null) {
            this.price = price;
        }
        if (quantity != null) {
            this.quantity = quantity;
        }
        if (category != null) {
            this.category = category;
        }
        if (description != null) {
            this.description = description;
        }
        if (status != null) {
            this.status = status;
        }
    }

    public void updateStatus(ProductStatusEnum status) {
        this.status = status;
    }

    public void updateQuantity(int quantity) {
        this.quantity = quantity;
    }

    public void incrementReview(int rating) {
        this.ratingSum += rating;
        this.reviewCnt++;
    }

    public void decrementReview(int rating) {
        this.ratingSum -= rating;
        this.reviewCnt--;
    }

    public void updateDiscountPrice(int discountPrice) {
        this.discountPrice = discountPrice;
    }
}
