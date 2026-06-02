package com.web.ecommerce.domain.order.scheduler;

import com.web.ecommerce.domain.order.entity.Order;
import com.web.ecommerce.domain.order.enums.OrderStatus;
import com.web.ecommerce.domain.order.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class OrderStatusScheduler {

    private final OrderRepository orderRepository;

    // 매 10분마다 실행
    @Scheduled(cron = "0 */10 * * * *")
    @Transactional
    public void progressOrderStatus() {
        LocalDateTime oneHourAgo = LocalDateTime.now().minusHours(1);
        LocalDateTime threeDaysAgo = LocalDateTime.now().minusDays(3);

        // PENDING → SHIPPING (주문 완료 후 1시간 경과)
        List<Order> paidOrders = orderRepository.findByStatusAndUpdatedAtBefore(
                OrderStatus.PENDING, oneHourAgo);
        paidOrders.forEach(order -> {
            order.ship();
            log.info("[주문 배치] PENDING → SHIPPING. orderId={}", order.getId());
        });

        // SHIPPING → DELIVERED (배송 시작 후 3일 경과)
        List<Order> shippingOrders = orderRepository.findByStatusAndUpdatedAtBefore(
                OrderStatus.SHIPPING, threeDaysAgo);
        shippingOrders.forEach(order -> {
            order.deliver();
            log.info("[주문 배치] SHIPPING → DELIVERED. orderId={}", order.getId());
        });
    }
}
