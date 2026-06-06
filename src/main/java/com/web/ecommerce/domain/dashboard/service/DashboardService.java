package com.web.ecommerce.domain.dashboard.service;

import com.web.ecommerce.domain.ad.repository.AdExposureRepository;
import com.web.ecommerce.domain.cart.repository.CartRepository;
import com.web.ecommerce.domain.coupon.enums.CouponStatus;
import com.web.ecommerce.domain.coupon.repository.UserCouponRepository;
import com.web.ecommerce.domain.dashboard.dto.CustomerListResponse;
import com.web.ecommerce.domain.dashboard.dto.CustomerListResponse.CustomerItem;
import com.web.ecommerce.domain.dashboard.dto.DashboardSummaryResponse;
import com.web.ecommerce.domain.dashboard.dto.MonthlyStatsResponse;
import com.web.ecommerce.domain.dashboard.dto.MonthlyStatsResponse.MonthlyStat;
import com.web.ecommerce.domain.dashboard.repository.EventLogRepository.MonthlyChurnRow;
import com.web.ecommerce.domain.dashboard.dto.CustomerDashboardResponse;
import com.web.ecommerce.domain.dashboard.dto.CustomerDashboardResponse.AccessTimeSlot;
import com.web.ecommerce.domain.dashboard.dto.CustomerDashboardResponse.AdConversionStats;
import com.web.ecommerce.domain.dashboard.dto.CustomerDashboardResponse.CtrStats;
import com.web.ecommerce.domain.dashboard.dto.CustomerDashboardResponse.CouponUsageStats;
import com.web.ecommerce.domain.dashboard.dto.CustomerDashboardResponse.CustomerInfo;
import com.web.ecommerce.domain.dashboard.dto.CustomerDashboardResponse.Tags;
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

    private static final List<String> ALL_TIME_SLOTS =
            List.of("00-03", "03-06", "06-09", "09-12", "12-15", "15-18", "18-21", "21-24");

    private final EventLogRepository eventLogRepository;
    private final UserRepository userRepository;
    private final OrderRepository orderRepository;
    private final OrderDetailRepository orderDetailRepository;
    private final CartRepository cartRepository;
    private final UserCouponRepository userCouponRepository;
    private final AdExposureRepository adExposureRepository;

    // ──────────────── Summary / Monthly / Customer List ────────────────

    public DashboardSummaryResponse getSummary() {
        long totalCustomers = userRepository.countByRoleAndIsActive(Role.USER, 1);

        long impressions = adExposureRepository.count();
        long clicks = adExposureRepository.countByClicked(true);
        double ctrRate = impressions > 0 ? Math.round((double) clicks / impressions * 1000.0) / 10.0 : 0;

        long couponSent = userCouponRepository.countByIsDuplicateFalse();
        long couponUsed = userCouponRepository.countByStatusAndIsDuplicateFalse(CouponStatus.USED);
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
                .stream().collect(Collectors.toMap(MonthlyChurnRow::month, MonthlyChurnRow::churnVisitors));

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
        LocalDateTime loginAfter = null;
        LocalDateTime createdAfter = null;

        if (filter != null) {
            switch (filter) {
                case "VIP"      -> grade = UserGrade.VIP.name();
                case "이탈위험높음" -> loginBefore = thirtyDaysAgo;
                case "이탈위험낮음" -> loginAfter = thirtyDaysAgo;
                case "신규"     -> createdAfter = thirtyDaysAgo;
            }
        }

        Pageable pageable = PageRequest.of(page, size, Sort.by("id").descending());
        Page<User> userPage = userRepository.findCustomersWithFilter(
                search != null ? search : "", grade, loginBefore, loginAfter, createdAfter, pageable);

        List<Long> userIds = userPage.getContent().stream().map(User::getId).toList();

        Map<Long, Long> purchaseCounts = orderRepository
                .countPurchasesByUserIds(userIds, thirtyDaysAgo, OrderStatus.CANCELLED.name())
                .stream().collect(Collectors.toMap(
                        row -> ((Number) row[0]).longValue(),
                        row -> ((Number) row[1]).longValue()
                ));

        Map<Long, long[]> adStats = adExposureRepository.findAdStatsByUserIds(userIds)
                .stream().collect(Collectors.toMap(
                        row -> ((Number) row[0]).longValue(),
                        row -> new long[]{((Number) row[1]).longValue(), ((Number) row[2]).longValue()}
                ));

        Map<Long, long[]> couponStats = userCouponRepository.findCouponStatsByUserIds(userIds)
                .stream().collect(Collectors.toMap(
                        row -> ((Number) row[0]).longValue(),
                        row -> new long[]{((Number) row[1]).longValue(), ((Number) row[2]).longValue()}
                ));

        List<CustomerItem> customers = userPage.getContent().stream().map(user -> {
            Long userId = user.getId();
            long purchaseCount = purchaseCounts.getOrDefault(userId, 0L);
            long[] ad = adStats.getOrDefault(userId, new long[]{0, 0});
            long[] coupon = couponStats.getOrDefault(userId, new long[]{0, 0});

            double ctr = ad[0] > 0 ? Math.round((double) ad[1] / ad[0] * 10000.0) / 100.0 : 0;
            double couponRate = coupon[0] > 0 ? Math.round((double) coupon[1] / coupon[0] * 1000.0) / 10.0 : 0;

            return new CustomerItem(
                    userId,
                    user.getLoginId(),
                    user.getGrade().name(),
                    formatLastLoginForList(user.getLastLoginAt()),
                    toPurchaseFrequency(purchaseCount),
                    toChurnRisk(user.getLastLoginAt()),
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

        // 최근 구매일 → N일 전
        Order lastOrder = orderRepository
                .findTopByUserIdAndStatusNotOrderByOrderDateDesc(userId, OrderStatus.CANCELLED)
                .orElse(null);
        long lastPurchaseDaysAgo = lastOrder != null
                ? ChronoUnit.DAYS.between(lastOrder.getOrderDate().toLocalDate(), LocalDate.now()) : 0;

        // 구매빈도
        long recentPurchaseCount = orderRepository.countByUserIdAndOrderDateAfterAndStatusNot(
                userId, thirtyDaysAgo, OrderStatus.CANCELLED);

        // 탈퇴 페이지 방문
        boolean churnPageVisited = eventLogRepository.hasWithdrawalPageVisit(userId, 30);

        // 이탈위험 (30일 기준)
        boolean loginOld = user.getLastLoginAt() == null ||
                user.getLastLoginAt().isBefore(thirtyDaysAgo);
        String churnRisk = (loginOld || churnPageVisited) ? "높음" : "낮음";

        // CTR (Server A - ad_exposure)
        long impressions = adExposureRepository.countByUser_Id(userId);
        long clicks = adExposureRepository.countByUser_IdAndClicked(userId, true);
        double ctrRate = impressions > 0 ? Math.round((double) clicks / impressions * 10000.0) / 100.0 : 0;

        // 쿠폰 (Server A - user_coupon)
        long couponReceived = userCouponRepository.countByUserIdAndIsDuplicateFalse(userId);
        long couponUsed = userCouponRepository.countByUserIdAndStatusAndIsDuplicateFalse(userId, CouponStatus.USED);
        long couponUnused = couponReceived - couponUsed;
        double couponRate = couponReceived > 0
                ? Math.round((double) couponUsed / couponReceived * 1000.0) / 10.0 : 0;

        // 광고→구매 전환율 - 광고 클릭 후 3일 내 구매한 건수
        long adLinkedPurchases = impressions > 0
                ? orderRepository.countPurchasesAfterAdClick(userId, OrderStatus.CANCELLED.name()) : 0;
        double conversionRate = impressions > 0
                ? Math.round((double) adLinkedPurchases / impressions * 10000.0) / 100.0 : 0;

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
                formatLastLogin(user.getLastLoginAt()),
                lastPurchaseDaysAgo,
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

    private String toChurnRisk(LocalDateTime lastLoginAt) {
        if (lastLoginAt == null) return "높음";
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
