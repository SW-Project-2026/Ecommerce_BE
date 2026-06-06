package com.web.ecommerce.domain.order.repository;

import com.web.ecommerce.domain.order.entity.Order;
import com.web.ecommerce.domain.order.enums.OrderStatus;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface OrderRepository extends JpaRepository<Order, Long> {

    @Query("SELECT o FROM Order o JOIN FETCH o.address WHERE o.user.id = :userId AND o.orderDate >= :from")
    Page<Order> findAllByUserId(Long userId, LocalDateTime from, Pageable pageable);


    // 관리자용 커서 기반 (최신순)
    @Query("SELECT o FROM Order o JOIN FETCH o.address WHERE o.user.id = :userId AND (:cursor = 0 OR o.id < :cursor) AND o.orderDate >= :from ORDER BY o.id DESC")
    List<Order> findByUserIdWithCursor(Long userId, Long cursor, LocalDateTime from, Pageable pageable);

    @Query("SELECT COALESCE(SUM(o.finalAmount), 0) FROM Order o WHERE o.user.id = :userId AND o.status <> :excludedStatus")
    int sumFinalAmountByUserIdExcludingStatus(Long userId, OrderStatus excludedStatus);

    Optional<Order> findTopByUserIdAndStatusNotOrderByOrderDateDesc(Long userId, OrderStatus status);

    long countByUserIdAndStatusNot(Long userId, OrderStatus status);

    @Query(value = """
            SELECT COUNT(DISTINCT od.order_item_id)
            FROM order_detail od
            JOIN orders o ON o.order_id = od.order_id
            JOIN ad_exposure ae ON ae.user_id = o.user_id
            JOIN ad a ON a.ad_id = ae.ad_id
            WHERE o.user_id = :userId
              AND ae.clicked = true
              AND od.product_id = a.product_id
              AND o.status != :status
            """, nativeQuery = true)
    long countPurchasesAfterAdClick(@Param("userId") Long userId, @Param("status") String status);

    long countByUserIdAndOrderDateAfterAndStatusNot(Long userId, LocalDateTime from, OrderStatus status);

    @Query("SELECT o FROM Order o JOIN FETCH o.orderDetails od JOIN FETCH od.product WHERE o.user.id = :userId AND (:cursor = 0 OR o.id < :cursor) ORDER BY o.id DESC")
    List<Order> findByUserIdWithCursorForDashboard(Long userId, Long cursor, Pageable pageable);

    @Query(value = "SELECT o.user_id, COUNT(o.order_id) FROM orders o WHERE o.user_id IN :userIds AND o.order_date >= :from AND o.status != :status GROUP BY o.user_id", nativeQuery = true)
    List<Object[]> countPurchasesByUserIds(@Param("userIds") List<Long> userIds, @Param("from") LocalDateTime from, @Param("status") String status);

    @Query("SELECT o FROM Order o WHERE o.status = :status AND o.updatedAt <= :before")
    List<Order> findByStatusAndUpdatedAtBefore(OrderStatus status, LocalDateTime before);

    @Query("SELECT COUNT(o) FROM Order o WHERE o.status <> :excludedStatus")
    long countExcludingStatus(OrderStatus excludedStatus);

    @Query("SELECT COALESCE(SUM(o.finalAmount), 0) FROM Order o WHERE o.status <> :excludedStatus")
    long sumFinalAmountExcludingStatus(OrderStatus excludedStatus);

    @Query("SELECT COUNT(o) FROM Order o WHERE o.orderDate >= :from AND o.status <> :excludedStatus")
    long countAfterExcludingStatus(LocalDateTime from, OrderStatus excludedStatus);

    @Query("SELECT COALESCE(SUM(o.finalAmount), 0) FROM Order o WHERE o.orderDate >= :from AND o.status <> :excludedStatus")
    long sumFinalAmountAfterExcludingStatus(LocalDateTime from, OrderStatus excludedStatus);
}
