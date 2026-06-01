package com.web.ecommerce.domain.admin.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class AdminDashboardResponse {

    // 주문
    private long totalOrders;
    private long totalRevenue;
    private long todayOrders;
    private long todayRevenue;

    // 유저
    private long totalUsers;
    private long todayNewUsers;
    private long newGradeCount;
    private long generalGradeCount;
    private long vipGradeCount;
}
