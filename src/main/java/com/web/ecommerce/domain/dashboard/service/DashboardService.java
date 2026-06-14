package com.web.ecommerce.domain.dashboard.service;

import com.web.ecommerce.domain.cart.repository.CartRepository;
import com.web.ecommerce.domain.wishlist.repository.WishlistRepository;
import com.web.ecommerce.domain.dashboard.dto.CustomerListResponse;
import com.web.ecommerce.domain.dashboard.dto.CustomerListResponse.CustomerItem;
import com.web.ecommerce.domain.dashboard.dto.DashboardSummaryResponse;
import com.web.ecommerce.domain.dashboard.dto.MonthlyStatsResponse;
import com.web.ecommerce.domain.dashboard.dto.MonthlyStatsResponse.MonthlyStat;
import com.web.ecommerce.domain.dashboard.dto.CustomerDashboardResponse;
import com.web.ecommerce.domain.dashboard.dto.CustomerDashboardResponse.AccessTimeSlot;
import com.web.ecommerce.domain.dashboard.dto.CustomerDashboardResponse.AdConversionStats;
import com.web.ecommerce.domain.dashboard.dto.CustomerDashboardResponse.CtrStats;
import com.web.ecommerce.domain.dashboard.dto.CustomerDashboardResponse.CouponUsageStats;
import com.web.ecommerce.domain.dashboard.dto.CustomerDashboardResponse.CustomerInfo;
import com.web.ecommerce.domain.dashboard.dto.CustomerDashboardResponse.Tags;
import com.web.ecommerce.domain.dashboard.dto.DashboardShared;
import com.web.ecommerce.domain.dashboard.dto.UserDashboardResponse.TimeSlotCount;
import com.web.ecommerce.domain.dashboard.repository.EventLogRepository;
import com.web.ecommerce.domain.dashboard.repository.EventLogRepository.CategoryCount;
import com.web.ecommerce.domain.order.entity.Order;
import com.web.ecommerce.domain.order.enums.OrderStatus;
import com.web.ecommerce.domain.order.repository.OrderDetailRepository;
import com.web.ecommerce.domain.order.repository.OrderRepository;
import com.web.ecommerce.domain.user.entity.Role;
import com.web.ecommerce.domain.user.entity.User;
import com.web.ecommerce.domain.user.repository.UserRepository;
import com.web.ecommerce.global.exception.CustomException;
import com.web.ecommerce.global.exception.GlobalErrorCode;
import com.web.ecommerce.domain.user.entity.UserGrade;
import com.web.ecommerce.global.response.CursorResponse;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class DashboardService {

    private static final List<String> ALL_TIME_SLOTS =
            List.of("00-03", "03-06", "06-09", "09-12", "12-15", "15-18", "18-21", "21-24");

    private final EventLogRepository eventLogRepository;
    private final UserRepository userRepository;
    private final OrderRepository orderRepository;
    private final OrderDetailRepository orderDetailRepository;
    private final CartRepository cartRepository;
    private final WishlistRepository wishlistRepository;

    /// ──────────────── Summary / Monthly / Customer List ────────────────

    public DashboardSummaryResponse getSummary() {
        long totalCustomers = userRepository.countByRoleAndIsActive(Role.USER, 1);

        DashboardShared.AdStats adStats = eventLogRepository.findAdStatsAll();
        long impressions = adStats.impressions();
        long clicks = adStats.clicks();
        double ctrRate = Math.round(adStats.ctr() * 10.0) / 10.0;

        EventLogRepository.CouponStats couponStats = eventLogRepository.findCouponStatsAll();
        long couponSent = couponStats.received();
        long couponUsed = couponStats.used();
        double couponRate = couponSent > 0 ? Math.round((double) couponUsed / couponSent * 1000.0) / 10.0 : 0;

        return new DashboardSummaryResponse(
                totalCustomers,
                new DashboardSummaryResponse.CtrStats(clicks, impressions, ctrRate),
                new DashboardSummaryResponse.CouponUsageStats(couponUsed, couponSent, couponRate)
        );
    }

    public MonthlyStatsResponse getMonthlyStats() {
        LocalDateTime twelveMonthsAgo = LocalDateTime.now().minusMonths(12);
        long totalUsers = userRepository.countByRoleAndIsActive(Role.USER, 1);

        Map<String, Long> signupMap = userRepository.countMonthlySignups(twelveMonthsAgo)
                .stream().collect(Collectors.toMap(
                        row -> (String) row[0],
                        row -> ((Number) row[1]).longValue()
                ));

        Map<String, Long> churnMap = eventLogRepository.findMonthlyChurnVisitors()
                .stream().collect(Collectors.toMap(
                        EventLogRepository.MonthlyChurnRow::month,
                        EventLogRepository.MonthlyChurnRow::churnVisitors
                ));

        List<MonthlyStat> stats = new ArrayList<>();
        YearMonth current = YearMonth.now();
        for (int i = 11; i >= 0; i--) {
            String month = current.minusMonths(i).toString();
            long newCustomers = signupMap.getOrDefault(month, 0L);
            long churnVisitors = churnMap.getOrDefault(month, 0L);
            double churnRate = totalUsers > 0 ? Math.round((double) churnVisitors / totalUsers * 1000.0) / 10.0 : 0;
            stats.add(new MonthlyStat(month, newCustomers, churnRate));
        }
        return new MonthlyStatsResponse(stats);
    }

    public CustomerListResponse getCustomers(int page, int size, String search, String filter) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime thirtyDaysAgo = now.minusDays(30);

        String grade = null;
        LocalDateTime loginBefore = null;
        LocalDateTime createdAfter = null;

        if (filter != null) {
            switch (filter) {
                case "VIP"      -> grade = UserGrade.VIP.name();
                case "이탈위험높음" -> loginBefore = thirtyDaysAgo;
                case "신규"     -> createdAfter = thirtyDaysAgo;
            }
        }

        Pageable pageable = PageRequest.of(page, size, Sort.by("id").descending());
        Page<User> userPage;
        if ("이탈위험낮음".equals(filter)) {
            List<Long> recentLoginIds = eventLogRepository.findUserIdsWithRecentLogin(15);
            if (recentLoginIds.isEmpty()) {
                return new CustomerListResponse(List.of(),
                        new CustomerListResponse.Pagination(0, 0, 0, size));
            }
            userPage = userRepository.findCustomersByIds(search != null ? search : "", recentLoginIds, pageable);
        } else {
            userPage = userRepository.findCustomersWithFilter(
                    search != null ? search : "", grade, loginBefore, null, createdAfter, pageable);
        }

        List<Long> userIds = userPage.getContent().stream().map(User::getId).toList();
        if (userIds.isEmpty()) {
            return new CustomerListResponse(List.of(),
                    new CustomerListResponse.Pagination(
                            userPage.getNumber(), userPage.getTotalPages(), userPage.getTotalElements(), size));
        }

        Map<Long, Long> purchaseCounts = orderRepository
                .countPurchasesByUserIds(userIds, thirtyDaysAgo, OrderStatus.CANCELLED.name())
                .stream().collect(Collectors.toMap(
                        row -> ((Number) row[0]).longValue(),
                        row -> ((Number) row[1]).longValue()
                ));

        Map<Long, EventLogRepository.AdStatRow> adStats = eventLogRepository.findAdStatsByUserIds(userIds);

        Map<Long, EventLogRepository.CouponStats> couponStats = eventLogRepository.findCouponStatsByUserIds(userIds);

        Set<Long> withdrawalVisitors = eventLogRepository.findUsersWithWithdrawalPageVisit(userIds);
        Map<Long, LocalDateTime> lastLoginMap = eventLogRepository.findLastLoginByUserIds(userIds);

        List<CustomerItem> customers = userPage.getContent().stream().map(user -> {
            Long userId = user.getId();
            long purchaseCount = purchaseCounts.getOrDefault(userId, 0L);
            EventLogRepository.AdStatRow ad = adStats.getOrDefault(userId, new EventLogRepository.AdStatRow(0, 0));
            EventLogRepository.CouponStats coupon = couponStats.getOrDefault(userId, new EventLogRepository.CouponStats(0, 0));

            double ctr = ad.impressions() > 0 ? Math.round((double) ad.clicks() / ad.impressions() * 10000.0) / 100.0 : 0;
            double couponRate = coupon.received() > 0 ? Math.round((double) coupon.used() / coupon.received() * 1000.0) / 10.0 : 0;

            LocalDateTime lastLoginAt = lastLoginMap.get(userId);

            return new CustomerItem(
                    userId,
                    user.getLoginId(),
                    user.getGrade().name(),
                    formatLastLoginForList(lastLoginAt),
                    toPurchaseFrequency(purchaseCount),
                    toChurnRisk(lastLoginAt, withdrawalVisitors.contains(userId)),
                    ctr,
                    couponRate
            );
        }).toList();

        return new CustomerListResponse(
                customers,
                new CustomerListResponse.Pagination(
                        userPage.getNumber(),
                        userPage.getTotalPages(),
                        userPage.getTotalElements(),
                        size
                )
        );
    }

    // ──────────────── Customer Dashboard (Admin → 특정 유저 조회) ────────────────

    public CustomerDashboardResponse getCustomerDashboard(Long userId) {
        User user = userRepository.findByIdAndIsActive(userId, 1)
                .orElseThrow(() -> new CustomException(GlobalErrorCode.RESOURCE_NOT_FOUND));

        LocalDateTime thirtyDaysAgo = LocalDateTime.now().minusDays(30);

        // 최근 구매일 (Order 없으면 event_log fallback)
        List<LocalDateTime> orderDates = orderRepository
                .findLastOrderDateByUserId(userId, OrderStatus.CANCELLED, PageRequest.of(0, 1));
        log.info("[lastPurchase] userId={}, orderDates={}", userId, orderDates);
        LocalDateTime lastPurchaseAt = orderDates.stream().findFirst()
                .orElseGet(() -> {
                    LocalDateTime fromEvent = eventLogRepository.findLastPurchaseByUserId(userId).orElse(null);
                    log.info("[lastPurchase] event_log fallback userId={}, result={}", userId, fromEvent);
                    return fromEvent;
                });
        log.info("[lastPurchase] final lastPurchaseAt={}", lastPurchaseAt);
        String lastPurchase = formatLastLogin(lastPurchaseAt);

        // 구매빈도
        long recentPurchaseCount = orderRepository.countByUserIdAndOrderDateAfterAndStatusNot(
                userId, thirtyDaysAgo, OrderStatus.CANCELLED);

        // 탈퇴 페이지 방문
        boolean churnPageVisited = eventLogRepository.hasWithdrawalPageVisit(userId, 30);

        // 최근 접속: event_log 우선, 없으면 User 엔티티 fallback
        LocalDateTime lastLoginAt = eventLogRepository.findLastLoginByUserId(userId)
                .orElse(user.getLastLoginAt());

        // 이탈위험 (30일 기준)
        boolean loginOld = lastLoginAt != null && lastLoginAt.isBefore(thirtyDaysAgo);
        String churnRisk = (loginOld || churnPageVisited) ? "높음" : "낮음";

        // CTR (event_log)
        DashboardShared.AdStats adEventStats = eventLogRepository.findAdStats(userId);
        long impressions = adEventStats.impressions();
        long clicks = adEventStats.clicks();
        double ctrRate = impressions > 0 ? Math.round((double) clicks / impressions * 10000.0) / 100.0 : 0;

        // 쿠폰 (event_log)
        EventLogRepository.CouponStats couponEventStats = eventLogRepository.findCouponStatsByUserId(userId);
        long couponReceived = couponEventStats.received();
        long couponUsed = couponEventStats.used();
        long couponUnused = couponReceived - couponUsed;
        double couponRate = couponReceived > 0
                ? Math.round((double) couponUsed / couponReceived * 1000.0) / 10.0 : 0;

        // 광고→구매 전환율 - 광고 클릭 후 3일 내 구매한 건수
        long adLinkedPurchases = clicks > 0
                ? orderRepository.countPurchasesAfterAdClick(userId, OrderStatus.CANCELLED.name()) : 0;
        double conversionRate = clicks > 0
                ? Math.round((double) adLinkedPurchases / clicks * 10000.0) / 100.0 : 0;

        // 관심 카테고리 (event_log 유지)
        List<String> interestedCategories = eventLogRepository.findTopCategoriesByUser(userId, 5)
                .stream().map(CategoryCount::category).toList();

        // 주 접속 시간대 (event_log 유지, 빈 슬롯 채우기)
        List<AccessTimeSlot> accessTimeSlots = fillAllTimeSlots(
                eventLogRepository.findPeakHours(userId));

        CustomerInfo customerInfo = new CustomerInfo(
                user.getName(),
                user.getGrade().name(),
                user.getCreatedAt().toLocalDate().toString(),
                formatLastLogin(lastLoginAt),
                lastPurchase,
                churnPageVisited,
                new Tags(toPurchaseFrequency(recentPurchaseCount), churnRisk)
        );

        return new CustomerDashboardResponse(
                customerInfo,
                new CtrStats(clicks, impressions, ctrRate),
                new CouponUsageStats(couponReceived, couponUsed, couponUnused, couponRate),
                new AdConversionStats(adLinkedPurchases, impressions, conversionRate),
                interestedCategories,
                accessTimeSlots
        );
    }

    public CursorResponse<CustomerDashboardResponse.WishlistItem> getCustomerWishlist(Long userId, Long cursor, int size) {
        Pageable pageable = PageRequest.of(0, size + 1);
        List<CustomerDashboardResponse.WishlistItem> items =
                wishlistRepository.findByUserIdWithCursorDesc(userId, cursor == null ? 0L : cursor, pageable)
                        .stream().map(w -> new CustomerDashboardResponse.WishlistItem(
                                w.getId(),
                                w.getProduct().getName(),
                                w.getProduct().getSubCategory() != null ? w.getProduct().getSubCategory() : "",
                                w.getProduct().getMaxPrice()
                        )).toList();

        boolean hasNext = items.size() > size;
        List<CustomerDashboardResponse.WishlistItem> content = hasNext ? items.subList(0, size) : items;
        Long nextCursor = hasNext ? content.get(content.size() - 1).wishlistId() : null;
        return CursorResponse.of(content, nextCursor, hasNext);
    }

    public CursorResponse<CustomerDashboardResponse.CartItem> getCustomerCart(Long userId, Long cursor, int size) {
        Pageable pageable = PageRequest.of(0, size + 1);
        List<CustomerDashboardResponse.CartItem> items =
                cartRepository.findByUserIdWithCursorDesc(userId, cursor == null ? 0L : cursor, pageable)
                        .stream().map(c -> new CustomerDashboardResponse.CartItem(
                                c.getId(),
                                c.getProduct().getName(),
                                c.getProduct().getSubCategory() != null ? c.getProduct().getSubCategory() : "",
                                c.getProduct().getMaxPrice()
                        )).toList();

        boolean hasNext = items.size() > size;
        List<CustomerDashboardResponse.CartItem> content = hasNext ? items.subList(0, size) : items;
        Long nextCursor = hasNext ? content.get(content.size() - 1).cartId() : null;
        return CursorResponse.of(content, nextCursor, hasNext);
    }

    public CursorResponse<CustomerDashboardResponse.OrderItem> getCustomerOrders(Long userId, Long cursor, int size) {
        Pageable pageable = PageRequest.of(0, size + 1);
        List<CustomerDashboardResponse.OrderItem> items =
                orderDetailRepository.findByUserIdWithCursor(userId, cursor == null ? 0L : cursor, pageable)
                        .stream().map(od -> new CustomerDashboardResponse.OrderItem(
                                od.getId(),
                                od.getProduct().getName(),
                                od.getProduct().getSubCategory() != null ? od.getProduct().getSubCategory() : "",
                                od.getUnitPrice()
                        )).toList();

        boolean hasNext = items.size() > size;
        List<CustomerDashboardResponse.OrderItem> content = hasNext ? items.subList(0, size) : items;
        Long nextCursor = hasNext ? content.get(content.size() - 1).orderItemId() : null;
        return CursorResponse.of(content, nextCursor, hasNext);
    }

    // ──────────────── helpers ────────────────

    private String toPurchaseFrequency(long count) {
        if (count >= 10) return "HIGH";
        if (count >= 5) return "MID";
        return "LOW";
    }

    private String toChurnRisk(LocalDateTime lastLoginAt, boolean withdrawalVisited) {
        if (withdrawalVisited) return "높음";
        if (lastLoginAt == null) return "낮음";
        long days = ChronoUnit.DAYS.between(lastLoginAt.toLocalDate(), LocalDate.now());
        if (days >= 30) return "높음";
        if (days >= 15) return "보통";
        return "낮음";
    }

    private String formatLastLoginForList(LocalDateTime lastLoginAt) {
        if (lastLoginAt == null) return "";
        LocalDate today = LocalDate.now();
        LocalDate loginDate = lastLoginAt.toLocalDate();
        String time = lastLoginAt.format(DateTimeFormatter.ofPattern("H:mm"));
        if (loginDate.equals(today)) return "오늘 " + time;
        if (loginDate.equals(today.minusDays(1))) return "어제 " + time;
        return ChronoUnit.DAYS.between(loginDate, today) + "일 전";
    }

    private String formatLastLogin(LocalDateTime lastLoginAt) {
        if (lastLoginAt == null) return "";
        LocalDate today = LocalDate.now();
        LocalDate loginDate = lastLoginAt.toLocalDate();
        String time = lastLoginAt.format(DateTimeFormatter.ofPattern("a h:mm", Locale.KOREAN));
        if (loginDate.equals(today)) return "오늘 " + time;
        if (loginDate.equals(today.minusDays(1))) return "어제 " + time;
        return lastLoginAt.format(DateTimeFormatter.ofPattern("yyyy년 M월 d일", Locale.KOREAN));
    }

    private List<AccessTimeSlot> fillAllTimeSlots(List<TimeSlotCount> data) {
        Map<String, Long> map = data.stream()
                .collect(Collectors.toMap(TimeSlotCount::slot, TimeSlotCount::count));
        return ALL_TIME_SLOTS.stream()
                .map(slot -> new AccessTimeSlot(slot, map.getOrDefault(slot, 0L)))
                .toList();
    }
}
