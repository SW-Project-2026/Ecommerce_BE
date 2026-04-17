package com.web.ecommerce.domain.product.service;

import com.web.ecommerce.domain.product.dto.NaverSearchResponse;
import com.web.ecommerce.domain.product.dto.ProductResponse;
import com.web.ecommerce.domain.product.dto.ProductSearchRequest;
import com.web.ecommerce.domain.product.dto.ProductSearchResult;
import com.web.ecommerce.global.exception.CustomException;
import com.web.ecommerce.global.exception.GlobalErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductService {

    private final RestClient naverRestClient;

    public ProductSearchResult searchProducts(ProductSearchRequest request) {
        try {
            NaverSearchResponse response = naverRestClient.get()
                    .uri(uriBuilder -> uriBuilder
                            .queryParam("query", request.getQuery())
                            .queryParam("display", request.getDisplay())
                            .queryParam("start", request.getStart())
                            .queryParam("sort", request.getSort())
                            .build())
                    .retrieve()
                    .body(NaverSearchResponse.class);

            if (response == null || response.getItems() == null) {
                log.warn("네이버 API 응답이 비어있습니다. query={}", request.getQuery());
                throw new CustomException(GlobalErrorCode.NAVER_API_EMPTY_RESPONSE);
            }

            List<ProductResponse> products = response.getItems().stream()
                    .map(ProductResponse::from)
                    .toList();

            return ProductSearchResult.builder()
                    .total(response.getTotal())
                    .start(response.getStart())
                    .display(response.getDisplay())
                    .products(products)
                    .build();

        } catch (RestClientException e) {
            log.error("네이버 쇼핑 API 호출 실패. query={}", request.getQuery(), e);
            throw new CustomException(GlobalErrorCode.NAVER_API_CALL_FAILED);
        }
    }
}
