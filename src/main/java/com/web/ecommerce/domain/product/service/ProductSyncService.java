package com.web.ecommerce.domain.product.service;

import com.web.ecommerce.domain.product.dto.NaverProductItem;
import com.web.ecommerce.domain.product.dto.NaverSearchResponse;
import com.web.ecommerce.domain.product.entity.Product;
import com.web.ecommerce.domain.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestClient;

import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductSyncService {

    private final RestClient naverRestClient;
    private final ProductRepository productRepository;

    // 카테고리명 → 네이버 검색 키워드 목록
    private static final Map<String, List<String>> CATEGORY_QUERIES = Map.of(
            "가전/디지털", List.of("노트북", "스마트폰", "무선이어폰"),
            "패션",      List.of("티셔츠", "청바지", "원피스"),
            "뷰티",      List.of("스킨케어", "립스틱", "선크림"),
            "식품",      List.of("케이크", "과자", "음료"),
            "생활용품",  List.of("청소기", "주방용품", "수납"),
            "스포츠",    List.of("운동화", "요가매트", "헬스용품")
    );
    private static final int PAGE_SIZE = 100;
    private static final int MAX_PAGES = 1; // 카테고리당 키워드 3개 × 100개 = 300개

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

        for (Map.Entry<String, List<String>> entry : CATEGORY_QUERIES.entrySet()) {
            String category = entry.getKey();
            for (String query : entry.getValue()) {
                for (int page = 1; page <= MAX_PAGES; page++) {
                    int start = (page - 1) * PAGE_SIZE + 1;
                    try {
                        NaverSearchResponse response = fetchFromNaver(query, start);
                        if (response == null || response.getItems() == null || response.getItems().isEmpty()) {
                            break;
                        }
                        int saved = saveProducts(response.getItems(), category);
                        totalSaved += saved;
                        log.info("category={}, query={}, page={}, 저장={}개", category, query, page, saved);
                    } catch (Exception e) {
                        log.error("네이버 API 호출 실패. category={}, query={}, page={}", category, query, page, e);
                        break;
                    }
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

    private int saveProducts(List<NaverProductItem> items, String keyword) {
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
                    .subCategory(item.getCategory2())
                    .searchKeyword(keyword)
                    .imageUrl(item.getImage())
                    .isActive(1)
                    .naverProductId(item.getProductId())
                    .build();

            productRepository.save(product);
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
