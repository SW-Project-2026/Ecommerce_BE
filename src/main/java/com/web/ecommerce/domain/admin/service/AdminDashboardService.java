package com.web.ecommerce.domain.admin.service;

import com.web.ecommerce.domain.admin.dto.response.AdminDashboardResponse;
import com.web.ecommerce.domain.cart.dto.response.CartResponse;
import com.web.ecommerce.domain.order.dto.response.OrderResponse;
import com.web.ecommerce.domain.wishlist.dto.response.WishlistResponse;
import com.web.ecommerce.global.response.CursorResponse;

public interface AdminDashboardService {

    AdminDashboardResponse getDashboard();

    CursorResponse<OrderResponse> getUserOrders(Long userId, Long cursor, String period, int size);

    CursorResponse<CartResponse> getUserCart(Long userId, Long cursor, int size);

    CursorResponse<WishlistResponse> getUserWishlist(Long userId, Long cursor, int size);
}
