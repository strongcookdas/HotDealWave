package com.sparta.hotdeal.product.domain.repository.review;

import com.sparta.hotdeal.product.domain.entity.review.Review;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
public interface ReviewRepository {
    Review save(Review review);

    Optional<Review> findById(UUID reviewId);

    Page<Review> findAll(Pageable pageable);
}