package com.web.ecommerce.domain.product.service;

import com.web.ecommerce.domain.product.dto.NaverSearchResponse;
import com.web.ecommerce.domain.product.dto.response.ProductResponse;
import com.web.ecommerce.domain.product.dto.request.ProductCreateRequest;
import com.web.ecommerce.domain.product.dto.request.ProductSearchRequest;
import com.web.ecommerce.domain.product.dto.request.ProductUpdateRequest;
import com.web.ecommerce.domain.product.dto.ProductSearchResult;
import com.web.ecommerce.domain.product.dto.response.ProductDetailResponse;
import com.web.ecommerce.domain.product.entity.Product;
import com.web.ecommerce.domain.product.exception.ProductErrorCode;
import com.web.ecommerce.domain.product.repository.ProductRepository;
import com.web.ecommerce.global.exception.CustomException;
import com.web.ecommerce.global.page.mapper.PageMapper;
import com.web.ecommerce.global.page.response.PageResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductService {

    private final RestClient naverRestClient;
    private final ProductRepository productRepository;
    private final PageMapper pageMapper;

    public ProductSearchResult searchProducts(ProductSearchRequest request) {
        NaverSearchResponse response;
        try {
            response = naverRestClient.get()
                    .uri(uriBuilder -> uriBuilder
                            .queryParam("query", request.getQuery())
                            .queryParam("display", request.getDisplay())
                            .queryParam("start", request.getStart())
                            .queryParam("sort", request.getSort())
                            .build())
                    .retrieve()
                    .body(NaverSearchResponse.class);
        } catch (RestClientException e) {
            log.error("네이버 쇼핑 API 호출 실패. query={}", request.getQuery(), e);
            throw new CustomException(ProductErrorCode.NAVER_API_CALL_FAILED);
        }

        if (response == null || response.getItems() == null) {
            log.warn("네이버 API 응답이 비어있습니다. query={}", request.getQuery());
            throw new CustomException(ProductErrorCode.NAVER_API_EMPTY_RESPONSE);
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
    }

    @Transactional(readOnly = true)
    public PageResponse<ProductDetailResponse> getProducts(String productCategory, Pageable pageable) {
        Pageable resolved = resolveSort(pageable);
        Page<Product> products = (productCategory != null && !productCategory.isBlank())
                ? productRepository.findByIsActiveAndProductCategory(1, productCategory, resolved)
                : productRepository.findByIsActive(1, resolved);

        return pageMapper.toPageResponse(products.map(ProductDetailResponse::from));
    }

    @Transactional(readOnly = true)
    public ProductDetailResponse getProduct(Long productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new CustomException(ProductErrorCode.PRODUCT_NOT_FOUND));

        if (product.getIsActive() != null && product.getIsActive() == 0) {
            throw new CustomException(ProductErrorCode.PRODUCT_INACTIVE);
        }

        return ProductDetailResponse.from(product);
    }

    @Transactional
    public ProductDetailResponse createProduct(ProductCreateRequest request) {
        Product product = Product.builder()
                .name(request.getName())
                .description(request.getDescription())
                .minPrice(request.getMinPrice())
                .maxPrice(request.getMaxPrice())
                .stockQuantity(request.getStockQuantity())
                .productCategory(request.getProductCategory())
                .imageUrl(request.getImageUrl())
                .isActive(1)
                .build();

        return ProductDetailResponse.from(productRepository.save(product));
    }

    @Transactional
    public ProductDetailResponse updateProduct(Long productId, ProductUpdateRequest request) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new CustomException(ProductErrorCode.PRODUCT_NOT_FOUND));

        product.update(
                request.getName(),
                request.getDescription(),
                request.getMinPrice(),
                request.getMaxPrice(),
                request.getStockQuantity(),
                request.getProductCategory(),
                request.getIsActive(),
                request.getImageUrl()
        );

        return ProductDetailResponse.from(product);
    }

    @Transactional
    public void deleteProduct(Long productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new CustomException(ProductErrorCode.PRODUCT_NOT_FOUND));
        product.deactivate();
    }

    // 프론트에서 "price" 정렬 요청 시 엔티티 필드명 "minPrice"로 변환
    private Pageable resolveSort(Pageable pageable) {
        Sort mapped = Sort.by(
        pageable.getSort().stream()
        .map(order -> {
        String prop = order.getProperty().equals("price") ? "minPrice" : order.getProperty();
            return order.isAscending() ? Sort.Order.asc(prop) : Sort.Order.desc(prop);
        })
        .toList()
        );
        return PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), mapped);
    }

}
