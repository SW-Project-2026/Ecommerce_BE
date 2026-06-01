package com.web.ecommerce.domain.wishlist.controller;

import com.web.ecommerce.domain.wishlist.dto.response.WishlistResponse;
import com.web.ecommerce.domain.wishlist.service.WishlistService;
import com.web.ecommerce.global.response.BaseResponse;
import com.web.ecommerce.global.response.CursorResponse;
import com.web.ecommerce.global.security.UserPrincipal;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/wishlist")
@RequiredArgsConstructor
public class WishlistControllerImpl implements WishlistController {

    private final WishlistService wishlistService;

    @Override
    @GetMapping
    public ResponseEntity<BaseResponse<CursorResponse<WishlistResponse>>> getWishlist(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @RequestParam(required = false) Long cursor,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(BaseResponse.success(
                wishlistService.getWishlist(userPrincipal.id(), cursor, size)));
    }

    @Override
    @PostMapping("/{productId}")
    public ResponseEntity<BaseResponse<WishlistResponse>> addToWishlist(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @PathVariable Long productId) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(BaseResponse.success(201, "찜 목록에 추가되었습니다.", wishlistService.addToWishlist(userPrincipal.id(), productId)));
    }

    @Override
    @DeleteMapping("/{wishId}")
    public ResponseEntity<Void> removeFromWishlist(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @PathVariable Long wishId) {
        wishlistService.removeFromWishlist(userPrincipal.id(), wishId);
        return ResponseEntity.noContent().build();
    }
}
