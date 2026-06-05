package com.web.ecommerce.domain.dashboard.dto;

public record DashboardSummaryResponse(
        long totalCustomers,
        CtrStats ctr,
        CouponUsageStats couponUsage
) {
    public record CtrStats(long clicks, long impressions, double rate) {}
    public record CouponUsageStats(long used, long sent, double rate) {}
}
