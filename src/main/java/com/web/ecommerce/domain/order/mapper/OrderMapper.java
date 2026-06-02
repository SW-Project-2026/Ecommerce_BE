package com.web.ecommerce.domain.order.mapper;

import com.web.ecommerce.domain.order.dto.response.OrderDetailResponse;
import com.web.ecommerce.domain.order.dto.response.OrderResponse;
import com.web.ecommerce.domain.order.entity.Order;
import com.web.ecommerce.domain.order.entity.OrderDetail;
import com.web.ecommerce.domain.order.enums.OrderStatus;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public class OrderMapper {

    private static final Map<OrderStatus, String> STATUS_LABELS = Map.of(
            OrderStatus.PENDING,   "주문 완료",
            OrderStatus.SHIPPING,  "배송 중",
            OrderStatus.DELIVERED, "배송 완료",
            OrderStatus.CANCELLED, "주문 취소"
    );

    public OrderResponse toResponse(Order order) {
        List<OrderDetailResponse> items = order.getOrderDetails().stream()
                .map(this::toDetailResponse)
                .toList();

        return OrderResponse.builder()
                .orderId(order.getId())
                .orderDate(order.getOrderDate().toString())
                .status(order.getStatus().name())
                .statusLabel(STATUS_LABELS.get(order.getStatus()))
                .totalAmount(order.getTotalAmount())
                .discountAmount(order.getDiscountAmount())
                .finalAmount(order.getFinalAmount())
                .roadNameAddress(order.getAddress().getRoadNameAddress())
                .addressDetail(order.getAddress().getAddressDetail())
                .items(items)
                .build();
    }

    public OrderDetailResponse toDetailResponse(OrderDetail detail) {
        return OrderDetailResponse.builder()
                .orderItemId(detail.getId())
                .productId(detail.getProduct().getProductId())
                .productName(detail.getProduct().getName())
                .imageUrl(detail.getProduct().getImageUrl())
                .quantity(detail.getQuantity())
                .unitPrice(detail.getUnitPrice())
                .subtotal(detail.getUnitPrice() * detail.getQuantity())
                .build();
    }
}
