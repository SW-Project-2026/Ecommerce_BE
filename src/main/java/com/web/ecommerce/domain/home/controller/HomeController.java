package com.web.ecommerce.domain.home.controller;

import com.web.ecommerce.domain.home.dto.HomeResponse;
import com.web.ecommerce.domain.home.service.HomeService;
import com.web.ecommerce.global.response.BaseResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Home", description = "홈 화면 API")
@RestController
@RequestMapping("/api/home")
@RequiredArgsConstructor
public class HomeController {

    private final HomeService homeService;

    @Operation(summary = "홈 화면 (비로그인)", description = "추천 상품(베스트), 베스트 상품 TOP 10, 쿠폰 프로모션 반환")
    @GetMapping
    public ResponseEntity<BaseResponse<HomeResponse>> getHomeGuest() {
        return ResponseEntity.ok(BaseResponse.success(homeService.getHomeForGuest()));
    }

    @Operation(summary = "홈 화면 (로그인)", description = "관심 카테고리 추천, 최근 본 상품, 구매 이력, 베스트 TOP 3, 쿠폰 프로모션 반환")
    @GetMapping("/{userId}")
    public ResponseEntity<BaseResponse<HomeResponse>> getHomeUser(@PathVariable Long userId) {
        return ResponseEntity.ok(BaseResponse.success(homeService.getHomeForUser(userId)));
    }
}
