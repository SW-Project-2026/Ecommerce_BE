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
    private String statusLabel;   // 주문 대기 / 결제 완료 / 배송 중 / 배송 완료 / 주문 취소
    private int totalAmount;
    private int discountAmount;
    private int finalAmount;
    private String roadNameAddress;
    private String addressDetail;
    private List<OrderDetailResponse> items;
}
