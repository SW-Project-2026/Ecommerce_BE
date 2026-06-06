package com.web.ecommerce.domain.product.controller;

import com.web.ecommerce.domain.product.dto.request.ProductCreateRequest;
import com.web.ecommerce.domain.product.dto.request.ProductSearchRequest;
import com.web.ecommerce.domain.product.dto.request.ProductUpdateRequest;
import com.web.ecommerce.domain.product.dto.request.SetScheduleRequest;
import com.web.ecommerce.domain.product.dto.ProductSearchResult;
import com.web.ecommerce.domain.product.dto.response.ProductDetailResponse;
import com.web.ecommerce.domain.product.dto.response.SyncResultResponse;
import com.web.ecommerce.domain.product.dto.response.SyncScheduleResponse;
import com.web.ecommerce.domain.product.service.ProductService;
import com.web.ecommerce.domain.product.service.ProductSyncScheduleService;
import com.web.ecommerce.domain.product.service.ProductSyncService;
import com.web.ecommerce.global.page.response.PageResponse;
import com.web.ecommerce.global.response.BaseResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Product", description = "상품 API")
@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;
    private final ProductSyncService productSyncService;
    private final ProductSyncScheduleService productSyncScheduleService;

    @Operation(summary = "상품 검색", description = "DB에 저장된 상품을 키워드로 검색하는 API")
    @GetMapping("/search")
    public ResponseEntity<BaseResponse<ProductSearchResult>> searchProducts(
            @Valid @ModelAttribute ProductSearchRequest request) {
        ProductSearchResult result = productService.searchProducts(request);
        return ResponseEntity.ok(BaseResponse.success(result));
    }

    @Operation(summary = "상품 목록 조회", description = "DB에 저장된 상품 목록을 페이징 조회하는 API. category 파라미터로 세부 카테고리 필터링 가능 (예: 과자/베이커리)")
    @GetMapping
    public ResponseEntity<BaseResponse<PageResponse<ProductDetailResponse>>> getProducts(
            @RequestParam(required = false) String productCategory,
            @ParameterObject @PageableDefault(size = 10, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
        PageResponse<ProductDetailResponse> result = productService.getProducts(productCategory, pageable);
        return ResponseEntity.ok(BaseResponse.success(result));
    }

    @Operation(summary = "상품 단건 조회", description = "상품 ID로 단건 조회하는 API")
    @GetMapping("/{productId}")
    public ResponseEntity<BaseResponse<ProductDetailResponse>> getProduct(@PathVariable Long productId) {
        ProductDetailResponse result = productService.getProduct(productId);
        return ResponseEntity.ok(BaseResponse.success(result));
    }

    @Operation(summary = "연관 상품 조회", description = "같은 카테고리 상품 최신순 반환 (기본 8개)")
    @GetMapping("/{productId}/related")
    public ResponseEntity<BaseResponse<List<ProductDetailResponse>>> getRelatedProducts(
            @PathVariable Long productId,
            @RequestParam(defaultValue = "8") int limit) {
        List<ProductDetailResponse> result = productService.getRelatedProducts(productId, limit);
        return ResponseEntity.ok(BaseResponse.success(result));
    }

    @Operation(summary = "상품 등록", description = "새 상품을 등록하는 API")
    @PostMapping
    public ResponseEntity<BaseResponse<ProductDetailResponse>> createProduct(
            @Valid @RequestBody ProductCreateRequest request) {
        ProductDetailResponse result = productService.createProduct(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(BaseResponse.success(201, "상품이 등록되었습니다.", result));
    }

    @Operation(summary = "상품 수정", description = "상품 정보를 수정하는 API")
    @PutMapping("/{productId}")
    public ResponseEntity<BaseResponse<ProductDetailResponse>> updateProduct(
            @PathVariable Long productId,
            @Valid @RequestBody ProductUpdateRequest request) {
        ProductDetailResponse result = productService.updateProduct(productId, request);
        return ResponseEntity.ok(BaseResponse.success(result));
    }

    @Operation(summary = "상품 삭제", description = "상품을 삭제하는 API")
    @DeleteMapping("/{productId}")
    public ResponseEntity<BaseResponse<Void>> deleteProduct(@PathVariable Long productId) {
        productService.deleteProduct(productId);
        return ResponseEntity.ok(BaseResponse.success(null));
    }

    @Operation(summary = "수동 수집 현황 조회", description = "마지막 수집 시각 및 전체 상품 수 조회")
    @GetMapping("/sync/status")
    public ResponseEntity<BaseResponse<SyncResultResponse>> getSyncStatus() {
        long totalCount = productService.countProducts();
        LocalDateTime lastSyncedAt = productService.getLastSyncedAt();
        return ResponseEntity.ok(BaseResponse.success(SyncResultResponse.ofStatus(totalCount, lastSyncedAt)));
    }

    @Operation(summary = "상품 수동 수집", description = "네이버 쇼핑 API를 즉시 호출하여 DB에 상품 데이터를 저장하는 API")
    @PostMapping("/sync")
    public ResponseEntity<BaseResponse<SyncResultResponse>> syncProducts() {
        int saved = productSyncService.sync();
        return ResponseEntity.ok(BaseResponse.success(200, "상품 수집이 완료되었습니다.", SyncResultResponse.of(saved)));
    }

    @Operation(summary = "자동 수집 스케줄 등록", description = "주기(DAILY/WEEKLY/MONTHLY)와 시각(HH:mm)을 지정하여 자동 수집 스케줄을 등록하는 API. 기존 스케줄이 있으면 덮어씁니다.")
    @PostMapping("/sync/schedule")
    public ResponseEntity<BaseResponse<SyncScheduleResponse>> setSchedule(
            @Valid @RequestBody SetScheduleRequest request) {
        SyncScheduleResponse response = productSyncScheduleService.setSchedule(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(BaseResponse.success(201, "자동 수집 스케줄이 등록되었습니다.", response));
    }

    @Operation(summary = "자동 수집 스케줄 조회", description = "현재 등록된 자동 수집 스케줄을 조회하는 API")
    @GetMapping("/sync/schedule")
    public ResponseEntity<BaseResponse<SyncScheduleResponse>> getSchedule() {
        SyncScheduleResponse response = productSyncScheduleService.getSchedule();
        return ResponseEntity.ok(BaseResponse.success(response));
    }

    @Operation(summary = "자동 수집 스케줄 취소", description = "등록된 자동 수집 스케줄을 취소하는 API")
    @DeleteMapping("/sync/schedule")
    public ResponseEntity<BaseResponse<Void>> cancelSchedule() {
        productSyncScheduleService.cancelSchedule();
        return ResponseEntity.ok(BaseResponse.success(200, "자동 수집 스케줄이 취소되었습니다.", null));
    }
}
