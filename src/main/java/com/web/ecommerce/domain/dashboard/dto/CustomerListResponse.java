package com.web.ecommerce.domain.dashboard.dto;

import java.util.List;

public record CustomerListResponse(
        List<CustomerItem> customers,
        Pagination pagination
) {
    public record CustomerItem(
            Long userId,
            String loginId,
            String grade,
            String lastLogin,
            String purchaseFrequency,
            String churnRisk,
            double ctr,
            double couponUsageRate
    ) {}

    public record Pagination(
            int currentPage,
            int totalPages,
            long totalElements,
            int size
    ) {}
}
