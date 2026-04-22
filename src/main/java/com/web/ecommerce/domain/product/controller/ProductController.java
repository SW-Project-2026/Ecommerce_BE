package com.web.ecommerce.domain.product.controller;

import com.web.ecommerce.domain.product.dto.request.ProductCreateRequest;
import com.web.ecommerce.domain.product.dto.request.ProductSearchRequest;
import com.web.ecommerce.domain.product.dto.request.ProductUpdateRequest;
import com.web.ecommerce.domain.product.dto.ProductSearchResult;
import com.web.ecommerce.domain.product.dto.response.ProductDetailResponse;
import com.web.ecommerce.domain.product.service.ProductService;
import com.web.ecommerce.global.page.response.PageResponse;
import com.web.ecommerce.global.response.BaseResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
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

    @Operation(summary = "네이버 상품 검색", description = "네이버 쇼핑 API를 통해 상품을 검색하는 API")
    @GetMapping("/search")
    public ResponseEntity<BaseResponse<ProductSearchResult>> searchProducts(
            @Valid @ModelAttribute ProductSearchRequest request) {
        ProductSearchResult result = productService.searchProducts(request);
        return ResponseEntity.ok(BaseResponse.success(result));
    }

    @Operation(summary = "상품 목록 조회", description = "DB에 저장된 상품 목록을 페이징 조회하는 API. category 파라미터로 세부 카테고리 필터링 가능 (예: 과자/베이커리)")
    @GetMapping
    public ResponseEntity<BaseResponse<PageResponse<ProductDetailResponse>>> getProducts(
            @RequestParam(required = false) String category,
            @ParameterObject @PageableDefault(size = 10, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
        PageResponse<ProductDetailResponse> result = productService.getProducts(category, pageable);
        return ResponseEntity.ok(BaseResponse.success(result));
    }

    @Operation(summary = "상품 단건 조회", description = "상품 ID로 단건 조회하는 API")
    @GetMapping("/{productId}")
    public ResponseEntity<BaseResponse<ProductDetailResponse>> getProduct(@PathVariable Long productId) {
        ProductDetailResponse result = productService.getProduct(productId);
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
}
