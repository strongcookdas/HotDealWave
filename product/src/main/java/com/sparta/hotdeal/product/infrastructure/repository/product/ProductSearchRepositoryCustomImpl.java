//package com.sparta.hotdeal.product.infrastructure.repository.product;
//
//import co.elastic.clients.elasticsearch.ElasticsearchClient;
//import co.elastic.clients.elasticsearch._types.FieldValue;
//import co.elastic.clients.elasticsearch._types.SortOrder;
//import co.elastic.clients.elasticsearch._types.query_dsl.BoolQuery;
//import co.elastic.clients.elasticsearch._types.query_dsl.QueryBuilders;
//import co.elastic.clients.elasticsearch._types.query_dsl.TermsQuery;
//import co.elastic.clients.elasticsearch.core.SearchRequest;
//import co.elastic.clients.elasticsearch.core.SearchResponse;
//import com.sparta.hotdeal.product.domain.entity.product.ProductDocument;
//import java.util.List;
//import java.util.UUID;
//import java.util.stream.Collectors;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.data.domain.Pageable;
//import org.springframework.stereotype.Repository;
//
//
//@Slf4j
//@RequiredArgsConstructor
//@Repository
//public class ProductSearchRepositoryCustomImpl implements ProductSearchRepositoryCustom {
//
//    private final ElasticsearchClient elasticsearchClient;
//
//    @Override
//    public List<ProductDocument> findBySearchAndIds(String search, List<UUID> productIds, Pageable pageable) {
//        try {
//            BoolQuery.Builder boolQuery = QueryBuilders.bool();
//
//            if (search != null && !search.isEmpty()) {
//                boolQuery.must(QueryBuilders.wildcard()
//                        .field("name")
//                        .value("*" + search + "*")
//                        .build()._toQuery());
//            }
//
//            if (productIds != null && !productIds.isEmpty()) {
//                boolQuery.filter(new TermsQuery.Builder()
//                        .field("id.keyword") // id.keyword로 수정
//                        .terms(t -> t.value(productIds.stream()
//                                .map(UUID::toString)
//                                .map(FieldValue::of)
//                                .toList()))
//                        .build()._toQuery());
//            }
//
//            SearchRequest request = new SearchRequest.Builder()
//                    .index("products")
//                    .query(boolQuery.build()._toQuery())
//                    .from((int) pageable.getOffset())
//                    .size(pageable.getPageSize())
//                    .sort(s -> {
//                        pageable.getSort().forEach(order -> {
//                            s.field(f -> f
//                                    .field(order.getProperty()) // .keyword 제거
//                                    .order(order.getDirection().isAscending() ? SortOrder.Asc : SortOrder.Desc));
//                        });
//                        return s;
//                    })
//                    .build();
//
//            log.info("Generated Elasticsearch query: {}", request);
//
//            SearchResponse<ProductDocument> response = elasticsearchClient.search(request, ProductDocument.class);
//
//            log.info("Elasticsearch response: {}", response);
//
//            return response.hits().hits().stream()
//                    .map(hit -> hit.source())
//                    .collect(Collectors.toList());
//
//        } catch (Exception e) {
//            log.error("Elasticsearch 검색 실패: {}", e.getMessage(), e);
//            throw new RuntimeException("Elasticsearch 검색 실패", e);
//        }
//    }
//}
