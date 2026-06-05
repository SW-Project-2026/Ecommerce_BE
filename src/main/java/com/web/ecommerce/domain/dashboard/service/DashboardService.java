package com.web.ecommerce.domain.dashboard.service;

import com.web.ecommerce.domain.cart.repository.CartRepository;
import com.web.ecommerce.domain.coupon.enums.CouponStatus;
import com.web.ecommerce.domain.coupon.repository.UserCouponRepository;
import com.web.ecommerce.domain.dashboard.dto.AdminDashboardResponse;
import com.web.ecommerce.domain.dashboard.dto.AdminDashboardResponse.*;
import com.web.ecommerce.domain.dashboard.dto.CartItemResponse;
import com.web.ecommerce.domain.dashboard.dto.OrderHistoryResponse;
import com.web.ecommerce.domain.dashboard.dto.UserDashboardResponse;
import com.web.ecommerce.domain.dashboard.dto.UserDashboardResponse.*;
import com.web.ecommerce.domain.dashboard.dto.UserDashboardResponse.AdStats;
import com.web.ecommerce.domain.dashboard.dto.UserSummaryResponse;
import com.web.ecommerce.domain.dashboard.repository.EventLogRepository;
import com.web.ecommerce.domain.dashboard.repository.EventLogRepository.AdStatRow;
import com.web.ecommerce.domain.order.entity.Order;
import com.web.ecommerce.domain.order.enums.OrderStatus;
import com.web.ecommerce.domain.order.repository.OrderRepository;
import com.web.ecommerce.domain.user.entity.Role;
import com.web.ecommerce.domain.user.entity.User;
import com.web.ecommerce.domain.user.repository.UserRepository;
import com.web.ecommerce.global.exception.CustomException;
import com.web.ecommerce.global.exception.GlobalErrorCode;
import com.web.ecommerce.global.response.CursorResponse;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class DashboardService {

    private final EventLogRepository eventLogRepository;
    private final UserRepository userRepository;
    private final OrderRepository orderRepository;
    private final CartRepository cartRepository;
    private final UserCouponRepository userCouponRepository;

    // ──────────────── Admin ────────────────

    public AdminDashboardResponse getAdminDashboard(int days) {
        LocalDateTime twelveMonthsAgo = LocalDateTime.now().minusMonths(12);

        AdStats adStats = eventLogRepository.findAdStatsAll();

        long couponSent = userCouponRepository.countByIsDuplicateFalse();
        long couponUsed = userCouponRepository.countByStatusAndIsDuplicateFalse(CouponStatus.USED);
        double couponUsageRate = couponSent > 0 ? (double) couponUsed / couponSent * 100 : 0;

        List<MonthlySignup> monthlySignups = userRepository.countMonthlySignups(twelveMonthsAgo)
                .stream().map(row -> new MonthlySignup((String) row[0], ((Number) row[1]).longValue()))
                .toList();

        List<MonthlyChurn> monthlyChurnRates = userRepository.countMonthlyChurn(twelveMonthsAgo)
                .stream().map(row -> {
                    long withdrawn = ((Number) row[1]).longValue();
                    long total = ((Number) row[2]).longValue();
                    double rate = total > 0 ? (double) withdrawn / total * 100 : 0;
                    return new MonthlyChurn((String) row[0], withdrawn, total, rate);
                }).toList();

        return new AdminDashboardResponse(
                eventLogRepository.findDailySales(days),
                eventLogRepository.findDau(days),
                eventLogRepository.findTopProducts(5),
                eventLogRepository.findTopCategories(5),
                eventLogRepository.findTodayEventCounts(),
                adStats,
                new CouponStats(couponSent, couponUsed, couponUsageRate),
                monthlySignups,
                monthlyChurnRates
        );
    }

    public Page<UserSummaryResponse> getAdminUserList(int page, int size) {
        Page<User> users = userRepository.findAllByRole(
                Role.USER, PageRequest.of(page, size, Sort.by("id").descending()));
        List<Long> userIds = users.getContent().stream().map(User::getId).toList();

        LocalDateTime thirtyDaysAgo = LocalDateTime.now().minusDays(30);

        Map<Long, Long> purchaseCounts = orderRepository
                .countPurchasesByUserIds(userIds, thirtyDaysAgo, OrderStatus.CANCELLED.name())
                .stream().collect(Collectors.toMap(
                        row -> ((Number) row[0]).longValue(),
                        row -> ((Number) row[1]).longValue()
                ));

        Map<Long, long[]> couponStats = userCouponRepository.findCouponStatsByUserIds(userIds)
                .stream().collect(Collectors.toMap(
                        row -> ((Number) row[0]).longValue(),
                        row -> new long[]{((Number) row[1]).longValue(), ((Number) row[2]).longValue()}
                ));

        Map<Long, AdStatRow> adStats = eventLogRepository.findAdStatsByUserIds(userIds);

        return users.map(user -> {
            Long userId = user.getId();
            long purchaseCount = purchaseCounts.getOrDefault(userId, 0L);
            long[] coupon = couponStats.getOrDefault(userId, new long[]{0, 0});
            AdStatRow ad = adStats.getOrDefault(userId, new AdStatRow(0, 0));

            double ctr = ad.impressions() > 0 ? (double) ad.clicks() / ad.impressions() * 100 : 0;
            double couponUsageRate = coupon[0] > 0 ? (double) coupon[1] / coupon[0] * 100 : 0;
            boolean loginOld = user.getLastLoginAt() == null ||
                    user.getLastLoginAt().isBefore(LocalDateTime.now().minusDays(14));

            return new UserSummaryResponse(
                    userId,
                    user.getLoginId(),
                    user.getGrade().name(),
                    user.getLastLoginAt() != null ? user.getLastLoginAt().toString() : null,
                    toPurchaseFrequency(purchaseCount),
                    loginOld ? "높음" : "낮음",
                    Math.round(ctr * 100.0) / 100.0,
                    Math.round(couponUsageRate * 100.0) / 100.0
            );
        });
    }

    // ──────────────── User ────────────────

    public UserDashboardResponse getUserDashboard(Long userId) {
        User user = userRepository.findByIdAndIsActive(userId, 1)
                .orElseThrow(() -> new CustomException(GlobalErrorCode.RESOURCE_NOT_FOUND));

        LocalDateTime thirtyDaysAgo = LocalDateTime.now().minusDays(30);

        Order lastOrder = orderRepository
                .findTopByUserIdAndStatusNotOrderByOrderDateDesc(userId, OrderStatus.CANCELLED)
                .orElse(null);
        long recentPurchaseCount = orderRepository.countByUserIdAndOrderDateAfterAndStatusNot(
                userId, thirtyDaysAgo, OrderStatus.CANCELLED);

        long couponSent = userCouponRepository.countByUserIdAndIsDuplicateFalse(userId);
        long couponUsed = userCouponRepository.countByUserIdAndStatusAndIsDuplicateFalse(userId, CouponStatus.USED);

        boolean withdrawalVisited = eventLogRepository.hasWithdrawalPageVisit(userId, 30);
        AdStats adStats = eventLogRepository.findAdStats(userId);
        double conversionRate = eventLogRepository.findAdConversionRate(userId);
        List<String> recentKeywords = eventLogRepository.findRecentKeywords(userId, 5);
        Object CategoryCount;
        List<String> topCategories = eventLogRepository.findTopCategoriesByUser(userId, 3)
                .stream().map(CategoryCount::category).toList();
        List<TimeSlotCount> peakHours = eventLogRepository.findPeakHours(userId);

        String purchaseFrequency = toPurchaseFrequency(recentPurchaseCount);
        boolean loginOld = user.getLastLoginAt() == null ||
                user.getLastLoginAt().isBefore(LocalDateTime.now().minusDays(14));
        String churnRisk = (loginOld || withdrawalVisited) ? "높음" : "낮음";
        double couponUsageRate = couponSent > 0 ? (double) couponUsed / couponSent * 100 : 0;

        return new UserDashboardResponse(
                user.getLastLoginAt() != null ? user.getLastLoginAt().toString() : null,
                lastOrder != null ? lastOrder.getOrderDate().toString() : null,
                withdrawalVisited,
                user.getGrade().name(),
                purchaseFrequency,
                churnRisk,
                adStats,
                new CouponStats(couponSent, couponUsed, couponUsageRate),
                Math.round(conversionRate * 100.0) / 100.0,
                recentKeywords,
                topCategories,
                peakHours
        );
    }

    public CursorResponse<CartItemResponse> getUserCart(Long userId, Long cursor, int size) {
        Pageable pageable = PageRequest.of(0, size + 1);
        List<CartItemResponse> items = cartRepository.findByUserIdWithCursor(userId, cursor, pageable)
                .stream().map(c -> new CartItemResponse(
                        c.getId(),
                        c.getProduct().getProductId(),
                        c.getProduct().getName(),
                        c.getProduct().getImageUrl(),
                        c.getProduct().getMinPrice(),
                        c.getQuantity(),
                        c.getProduct().getMinPrice() * c.getQuantity()
                )).toList();

        boolean hasNext = items.size() > size;
        List<CartItemResponse> content = hasNext ? items.subList(0, size) : items;
        Long nextCursor = hasNext ? content.get(content.size() - 1).cartId() : null;
        return CursorResponse.of(content, nextCursor, hasNext);
    }

    public CursorResponse<OrderHistoryResponse> getUserOrders(Long userId, Long cursor, int size) {
        Pageable pageable = PageRequest.of(0, size + 1);
        List<OrderHistoryResponse> items = orderRepository
                .findByUserIdWithCursorForDashboard(userId, cursor, pageable)
                .stream().map(o -> new OrderHistoryResponse(
                        o.getId(),
                        o.getOrderDate().toString(),
                        o.getStatus().name(),
                        o.getFinalAmount(),
                        o.getOrderDetails().stream().map(d -> new OrderHistoryResponse.OrderItem(
                                d.getProduct().getName(),
                                d.getQuantity(),
                                d.getUnitPrice()
                        )).toList()
                )).toList();

        boolean hasNext = items.size() > size;
        List<OrderHistoryResponse> content = hasNext ? items.subList(0, size) : items;
        Long nextCursor = hasNext ? content.get(content.size() - 1).orderId() : null;
        return CursorResponse.of(content, nextCursor, hasNext);
    }

    // ──────────────── helpers ────────────────

    private String toPurchaseFrequency(long count) {
        if (count >= 5) return "HIGH";
        if (count >= 1) return "MID";
        return "LOW";
    }
}
