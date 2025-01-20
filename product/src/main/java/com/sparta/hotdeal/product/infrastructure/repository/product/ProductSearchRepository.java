package com.sparta.hotdeal.product.infrastructure.repository.product;

import com.sparta.hotdeal.product.domain.entity.product.ProductDocument;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface ProductSearchRepository extends ElasticsearchRepository<ProductDocument, String>,
        ProductSearchRepositoryCustom {

}
