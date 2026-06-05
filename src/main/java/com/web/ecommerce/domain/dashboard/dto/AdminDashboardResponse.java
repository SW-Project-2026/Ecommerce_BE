package com.web.ecommerce.domain.dashboard.dto;

import com.web.ecommerce.domain.dashboard.dto.DashboardShared.AdStats;
import com.web.ecommerce.domain.dashboard.dto.DashboardShared.CouponStats;
import java.util.List;

public record AdminDashboardResponse(
        List<DailySales> dailySales,
        List<DailyUsers> dau,
        List<TopProduct> topProducts,
        List<TopCategory> topCategories,
        List<EventCount> eventCounts,
        AdStats adStats,
        CouponStats couponStats,
        List<MonthlySignup> monthlySignups,
        List<MonthlyChurn> monthlyChurnRates
) {
    public record DailySales(String date, long totalAmount, long orderCount) {}
    public record DailyUsers(String date, long count) {}
    public record TopProduct(String productName, String category, long count, long totalAmount) {}
    public record TopCategory(String category, long count, long totalAmount) {}
    public record EventCount(String eventName, long count) {}
    public record MonthlySignup(String month, long count) {}
    public record MonthlyChurn(String month, long withdrawals, long total, double rate) {}
}
