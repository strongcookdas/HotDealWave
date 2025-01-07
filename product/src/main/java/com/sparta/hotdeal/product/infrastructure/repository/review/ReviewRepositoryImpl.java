package com.sparta.hotdeal.product.infrastructure.repository.review;

import com.sparta.hotdeal.product.domain.entity.review.Review;
import com.sparta.hotdeal.product.domain.repository.review.ReviewRepository;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewRepositoryImpl extends JpaRepository<Review, UUID>, ReviewRepository {
}
