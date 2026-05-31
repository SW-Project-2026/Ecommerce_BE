package com.web.ecommerce.domain.wishlist.controller;

import com.web.ecommerce.domain.wishlist.dto.response.WishlistResponse;
import com.web.ecommerce.global.response.BaseResponse;
import com.web.ecommerce.global.security.UserPrincipal;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;

import java.util.List;

@Tag(name = "Wishlist", description = "찜 API")
public interface WishlistController {

    @Operation(summary = "내 찜 목록 조회")
    ResponseEntity<BaseResponse<List<WishlistResponse>>> getWishlist(UserPrincipal userPrincipal);

    @Operation(summary = "찜 추가")
    ResponseEntity<BaseResponse<WishlistResponse>> addToWishlist(UserPrincipal userPrincipal, Long productId);

    @Operation(summary = "찜 삭제")
    ResponseEntity<Void> removeFromWishlist(UserPrincipal userPrincipal, Long wishId);
}
