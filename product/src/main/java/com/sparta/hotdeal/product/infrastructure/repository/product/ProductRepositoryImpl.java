package com.sparta.hotdeal.product.infrastructure.repository.product;

import com.sparta.hotdeal.product.domain.entity.product.Product;
import com.sparta.hotdeal.product.domain.repository.product.ProductRepository;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
public class ProductRepositoryImpl implements ProductRepository {

    private final ProductRepositoryJpa productRepositoryJpa;
    private final ProductRepositoryCustom productRepositoryCustom;

    public ProductRepositoryImpl(ProductRepositoryJpa productRepositoryJpa,
                                 ProductRepositoryCustom productRepositoryCustom) {
        this.productRepositoryJpa = productRepositoryJpa;
        this.productRepositoryCustom = productRepositoryCustom;
    }

    @Override
    public Page<Product> findAllWithSearchAndPaging(String search, List<UUID> productIds, Pageable pageable) {
        return productRepositoryCustom.findAllWithSearchAndPaging(search, productIds, pageable);
    }

    @Override
    public Optional<Product> findById(UUID id) {
        return productRepositoryJpa.findById(id);
    }

    @Override
    public Product save(Product product) {
        return productRepositoryJpa.save(product);
    }

    @Override
    public List<Product> findAllByIdIn(List<UUID> productIds) {
        return productRepositoryJpa.findAllByIdIn(productIds);
    }

    @Override
    public List<Product> findAllByIdInWithLock(List<UUID> productIds) {
        return productRepositoryJpa.findAllByIdInWithLock(productIds);
    }
}
