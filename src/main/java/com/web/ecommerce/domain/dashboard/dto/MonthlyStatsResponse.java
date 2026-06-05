package com.web.ecommerce.domain.dashboard.dto;

import java.util.List;

public record MonthlyStatsResponse(List<MonthlyStat> monthlyStats) {
    public record MonthlyStat(String month, long newCustomers, double churnRate) {}
}
