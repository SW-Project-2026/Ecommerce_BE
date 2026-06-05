package com.web.ecommerce.domain.dashboard.dto;

import java.util.List;

public record UserDashboardResponse(
        String lastLoginAt,
        String lastPurchaseAt,
        boolean withdrawalPageVisited,
        String grade,
        String purchaseFrequency,
        String churnRisk,
        AdStats adStats,
        CouponStats couponStats,
        double adConversionRate,
        List<String> recentKeywords,
        List<String> topCategories,
        List<TimeSlotCount> peakHours
) {
    public record AdStats(long impressions, long clicks, double ctr) {}
    public record CouponStats(long sent, long used, double usageRate) {}
    public record TimeSlotCount(String slot, long count) {}
}
