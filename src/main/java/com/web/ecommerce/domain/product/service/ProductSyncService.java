package com.web.ecommerce.domain.product.service;

import com.web.ecommerce.domain.product.dto.NaverProductItem;
import com.web.ecommerce.domain.product.dto.NaverSearchResponse;
import com.web.ecommerce.domain.product.entity.Product;
import com.web.ecommerce.domain.product.entity.ProductImage;
import com.web.ecommerce.domain.product.repository.ProductImageRepository;
import com.web.ecommerce.domain.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestClient;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductSyncService {

    private final RestClient naverRestClient;
    private final ProductRepository productRepository;
    private final ProductImageRepository productImageRepository;

    private static final List<String> SEARCH_QUERIES = List.of(
            "케이크", "마카롱", "쿠키", "디저트", "베이커리"
    );
    private static final int PAGE_SIZE = 100;
    private static final int MAX_PAGES = 2; // 쿼리당 최대 200개

    @Transactional
    public void syncIfEmpty() {
        if (productRepository.count() > 0) {
            log.info("상품 데이터가 이미 존재합니다. sync를 건너뜁니다.");
            return;
        }
        log.info("상품 데이터가 없습니다. 네이버 API에서 상품을 가져옵니다.");
        sync();
    }

    @Transactional
    public void sync() {
        int totalSaved = 0;

        for (String query : SEARCH_QUERIES) {
            for (int page = 1; page <= MAX_PAGES; page++) {
                int start = (page - 1) * PAGE_SIZE + 1;
                try {
                    NaverSearchResponse response = fetchFromNaver(query, start);
                    if (response == null || response.getItems() == null || response.getItems().isEmpty()) {
                        break;
                    }
                    int saved = saveProducts(response.getItems());
                    totalSaved += saved;
                    log.info("query={}, page={}, 저장={}개", query, page, saved);
                } catch (Exception e) {
                    log.error("네이버 API 호출 실패. query={}, page={}", query, page, e);
                    break;
                }
            }
        }
        log.info("상품 sync 완료. 총 {}개 저장", totalSaved);
    }

    private NaverSearchResponse fetchFromNaver(String query, int start) {
        return naverRestClient.get()
                .uri(uriBuilder -> uriBuilder
                        .queryParam("query", query)
                        .queryParam("display", PAGE_SIZE)
                        .queryParam("start", start)
                        .queryParam("sort", "sim")
                        .build())
                .retrieve()
                .body(NaverSearchResponse.class);
    }

    private int saveProducts(List<NaverProductItem> items) {
        int count = 0;
        for (NaverProductItem item : items) {
            if (item.getProductId() == null || productRepository.existsByNaverProductId(item.getProductId())) {
                continue;
            }
            String title = item.getTitle().replaceAll("<[^>]*>", "");
            String description = buildDescription(item);

            int price = 0;
            try {
                if (item.getLprice() != null && !item.getLprice().isBlank()) {
                    price = Integer.parseInt(item.getLprice());
                }
            } catch (NumberFormatException ignored) {}

            Product product = Product.builder()
                    .name(title.length() > 100 ? title.substring(0, 100) : title)
                    .description(description)
                    .price(price)
                    .stockQuantity(100)
                    .productCategory(item.getCategory1())
                    .isActive(1)
                    .naverProductId(item.getProductId())
                    .build();

            Product savedProduct = productRepository.save(product);

            if (item.getImage() != null && !item.getImage().isBlank()) {
                ProductImage image = ProductImage.builder()
                        .product(savedProduct)
                        .imageUrl(item.getImage())
                        .sortOrder(1)
                        .build();
                productImageRepository.save(image);
            }
            count++;
        }
        return count;
    }

    private String buildDescription(NaverProductItem item) {
        StringBuilder sb = new StringBuilder();
        if (item.getBrand() != null && !item.getBrand().isBlank()) {
            sb.append("브랜드: ").append(item.getBrand()).append(" ");
        }
        if (item.getMaker() != null && !item.getMaker().isBlank()) {
            sb.append("제조사: ").append(item.getMaker()).append(" ");
        }
        if (item.getCategory2() != null && !item.getCategory2().isBlank()) {
            sb.append("카테고리: ").append(item.getCategory2());
        }
        String result = sb.toString().trim();
        return result.isEmpty() ? "상품 설명 없음" : result;
    }
}
