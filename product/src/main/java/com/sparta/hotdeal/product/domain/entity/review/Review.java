package com.sparta.hotdeal.product.domain.entity.review;

import com.sparta.hotdeal.product.domain.entity.AuditingDate;
import com.sparta.hotdeal.product.domain.entity.product.File;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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

@Getter
@Entity
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder(access = AccessLevel.PRIVATE)
@Table(name = "p_review")
public class Review extends AuditingDate {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private UUID orderID;

    @Column(nullable = false)
    private UUID productID;

    @Column(nullable = false)
    private Long userId;

    @Column(nullable = false)
    private double rating;

    @Column(nullable = false)
    private String review;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "review_imgs", nullable = false)
    private File reviewImgs;

    public static Review create(
            UUID orderID,
            UUID productID,
            Long userId,
            double rating,
            String review,
            File reviewImgs
    ) {
        return Review.builder()
                .orderID(orderID)
                .productID(productID)
                .userId(userId)
                .rating(rating)
                .review(review)
                .reviewImgs(reviewImgs)
                .build();
    }

    public void update(
            double rating,
            String review
    ) {
        this.rating = rating;
        this.review = review;
    }
}