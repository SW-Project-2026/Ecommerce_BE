package com.web.ecommerce.domain.dashboard.dto;

import com.web.ecommerce.domain.dashboard.dto.DashboardShared.AdStats;
import com.web.ecommerce.domain.dashboard.dto.DashboardShared.CouponStats;
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
    public record TimeSlotCount(String slot, long count) {}
}
