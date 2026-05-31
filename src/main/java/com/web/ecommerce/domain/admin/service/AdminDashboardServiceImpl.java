package com.web.ecommerce.domain.admin.service;

import com.web.ecommerce.domain.admin.dto.response.AdminDashboardResponse;
import com.web.ecommerce.domain.cart.dto.response.CartResponse;
import com.web.ecommerce.domain.cart.mapper.CartMapper;
import com.web.ecommerce.domain.cart.repository.CartRepository;
import com.web.ecommerce.domain.order.dto.response.OrderResponse;
import com.web.ecommerce.domain.order.enums.OrderStatus;
import com.web.ecommerce.domain.order.mapper.OrderMapper;
import com.web.ecommerce.domain.order.repository.OrderRepository;
import com.web.ecommerce.domain.user.entity.Role;
import com.web.ecommerce.domain.user.entity.UserGrade;
import com.web.ecommerce.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
    private final OrderMapper orderMapper;
    private final CartMapper cartMapper;

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
    public Page<OrderResponse> getUserOrders(Long userId, Pageable pageable) {
        return orderRepository.findAllByUserId(userId, pageable)
                .map(orderMapper::toResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CartResponse> getUserCart(Long userId) {
        return cartRepository.findAllByUserId(userId).stream()
                .map(cartMapper::toResponse)
                .toList();
    }
}
