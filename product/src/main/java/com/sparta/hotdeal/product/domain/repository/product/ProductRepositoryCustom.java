package com.sparta.hotdeal.product.domain.repository.product;

import com.sparta.hotdeal.product.domain.entity.product.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepositoryCustom {

    Page<Product> findAllWithSearchAndPaging(String search, Pageable pageable);
}
