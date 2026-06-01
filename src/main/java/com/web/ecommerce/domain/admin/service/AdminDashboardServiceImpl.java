package com.web.ecommerce.domain.admin.service;

import com.web.ecommerce.domain.admin.dto.response.AdminDashboardResponse;
import com.web.ecommerce.domain.cart.dto.response.CartResponse;
import com.web.ecommerce.domain.cart.entity.Cart;
import com.web.ecommerce.domain.cart.mapper.CartMapper;
import com.web.ecommerce.domain.cart.repository.CartRepository;
import com.web.ecommerce.domain.order.dto.response.OrderResponse;
import com.web.ecommerce.domain.order.entity.Order;
import com.web.ecommerce.domain.order.enums.OrderStatus;
import com.web.ecommerce.domain.order.mapper.OrderMapper;
import com.web.ecommerce.domain.order.repository.OrderRepository;
import com.web.ecommerce.domain.user.entity.Role;
import com.web.ecommerce.domain.user.entity.UserGrade;
import com.web.ecommerce.domain.user.repository.UserRepository;
import com.web.ecommerce.domain.wishlist.dto.response.WishlistResponse;
import com.web.ecommerce.domain.wishlist.entity.Wishlist;
import com.web.ecommerce.domain.wishlist.mapper.WishlistMapper;
import com.web.ecommerce.domain.wishlist.repository.WishlistRepository;
import com.web.ecommerce.global.response.CursorResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminDashboardServiceImpl implements AdminDashboardService {

    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final CartRepository cartRepository;
    private final WishlistRepository wishlistRepository;
    private final OrderMapper orderMapper;
    private final CartMapper cartMapper;
    private final WishlistMapper wishlistMapper;

    @Override
    @Transactional(readOnly = true)
    public AdminDashboardResponse getDashboard() {
        LocalDateTime todayStart = LocalDate.now().atStartOfDay();

        return AdminDashboardResponse.builder()
                .totalOrders(orderRepository.countExcludingStatus(OrderStatus.CANCELLED))
                .totalRevenue(orderRepository.sumFinalAmountExcludingStatus(OrderStatus.CANCELLED))
                .todayOrders(orderRepository.countAfterExcludingStatus(todayStart, OrderStatus.CANCELLED))
                .todayRevenue(orderRepository.sumFinalAmountAfterExcludingStatus(todayStart, OrderStatus.CANCELLED))
                .totalUsers(userRepository.countByRoleAndIsActive(Role.USER, 1))
                .todayNewUsers(userRepository.countByRoleAndIsActiveAndCreatedAtAfter(Role.USER, 1, todayStart))
                .newGradeCount(userRepository.countByRoleAndIsActiveAndGrade(Role.USER, 1, UserGrade.NEW))
                .generalGradeCount(userRepository.countByRoleAndIsActiveAndGrade(Role.USER, 1, UserGrade.GENERAL))
                .vipGradeCount(userRepository.countByRoleAndIsActiveAndGrade(Role.USER, 1, UserGrade.VIP))
                .build();
    }

    @Override
    @Transactional(readOnly = true)
    public CursorResponse<OrderResponse> getUserOrders(Long userId, Long cursor, String period, int size) {
        List<Order> orders = orderRepository.findByUserIdWithCursor(
                userId,
                cursor == null ? 0L : cursor,
                periodToDateTime(period),
                PageRequest.of(0, size + 1)
        );

        boolean hasNext = orders.size() > size;
        if (hasNext) orders = orders.subList(0, size);

        Long nextCursor = hasNext ? orders.get(orders.size() - 1).getId() : null;
        return CursorResponse.of(orders.stream().map(orderMapper::toResponse).toList(), nextCursor, hasNext);
    }

    @Override
    @Transactional(readOnly = true)
    public CursorResponse<CartResponse> getUserCart(Long userId, Long cursor, int size) {
        List<Cart> carts = cartRepository.findByUserIdWithCursor(
                userId,
                cursor == null ? 0L : cursor,
                PageRequest.of(0, size + 1)
        );

        boolean hasNext = carts.size() > size;
        if (hasNext) carts = carts.subList(0, size);

        Long nextCursor = hasNext ? carts.get(carts.size() - 1).getId() : null;
        return CursorResponse.of(carts.stream().map(cartMapper::toResponse).toList(), nextCursor, hasNext);
    }

    @Override
    @Transactional(readOnly = true)
    public CursorResponse<WishlistResponse> getUserWishlist(Long userId, Long cursor, int size) {
        List<Wishlist> wishlists = wishlistRepository.findByUserIdWithCursor(
                userId,
                cursor == null ? 0L : cursor,
                PageRequest.of(0, size + 1)
        );

        boolean hasNext = wishlists.size() > size;
        if (hasNext) wishlists = wishlists.subList(0, size);

        Long nextCursor = hasNext ? wishlists.get(wishlists.size() - 1).getId() : null;
        return CursorResponse.of(wishlists.stream().map(wishlistMapper::toResponse).toList(), nextCursor, hasNext);
    }

    private LocalDateTime periodToDateTime(String period) {
        if (period == null) return LocalDateTime.of(2000, 1, 1, 0, 0);
        return switch (period) {
            case "1m" -> LocalDateTime.now().minusMonths(1);
            case "3m" -> LocalDateTime.now().minusMonths(3);
            case "6m" -> LocalDateTime.now().minusMonths(6);
            default   -> LocalDateTime.of(2000, 1, 1, 0, 0);
        };
    }
}
