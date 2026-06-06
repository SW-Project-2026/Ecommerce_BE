package com.web.ecommerce.domain.dashboard.dto;

public class DashboardShared {
    public record AdStats(long impressions, long clicks, double ctr) {}
    public record CouponStats(long sent, long used, double usageRate) {}
}
