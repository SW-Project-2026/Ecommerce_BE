package com.web.ecommerce.domain.dashboard.dto;

public record UserSummaryResponse(
        Long userId,
        String loginId,
        String grade,
        String lastLoginAt,
        String purchaseFrequency,
        String churnRisk,
        double ctr,
        double couponUsageRate
) {}
