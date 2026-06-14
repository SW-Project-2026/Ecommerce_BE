package com.web.ecommerce.domain.dashboard.dto;

import java.util.List;

public record CustomerDashboardResponse(
        CustomerInfo customerInfo,
        CtrStats ctr,
        CouponUsageStats couponUsage,
        AdConversionStats adConversion,
        List<String> interestedCategories,
        List<AccessTimeSlot> accessTimeSlots
) {
    public record CustomerInfo(
            String name,
            String grade,
            String joinDate,
            String lastLogin,
            String lastPurchase,
            boolean churnPageVisited,
            Tags tags
    ) {}

    public record Tags(String purchaseFrequency, String churnRisk) {}

    public record CtrStats(long clicks, long impressions, double rate) {}

    public record CouponUsageStats(long received, long used, long unused, double rate) {}

    public record AdConversionStats(long purchases, long adImpressions, double rate) {}

    public record AccessTimeSlot(String timeSlot, long count) {}

    public record CartItem(Long cartId, String productName, String category, int price) {}

    public record OrderItem(Long orderItemId, String productName, String category, int price) {}

    public record WishlistItem(Long wishlistId, String productName, String category, int price) {}
}
