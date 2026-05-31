package com.web.ecommerce.domain.order.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class OrderResponse {

    private Long orderId;
    private String orderDate;
    private String status;
    private int totalAmount;
    private int discountAmount;
    private int finalAmount;
    private String roadNameAddress;
    private String addressDetail;
    private List<OrderDetailResponse> items;
}
