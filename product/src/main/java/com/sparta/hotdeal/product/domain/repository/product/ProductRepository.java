package com.sparta.hotdeal.product.domain.repository.product;

import com.sparta.hotdeal.product.domain.entity.product.Product;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ProductRepository {

    Page<Product> findAllWithSearchAndPaging(String search, List<UUID> productIds, Pageable pageable);

    Optional<Product> findById(UUID id);

    Product save(Product product);
}
