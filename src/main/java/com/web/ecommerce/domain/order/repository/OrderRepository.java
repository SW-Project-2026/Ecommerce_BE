package com.web.ecommerce.domain.order.repository;

import com.web.ecommerce.domain.order.entity.Order;
import com.web.ecommerce.domain.order.enums.OrderStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface OrderRepository extends JpaRepository<Order, Long> {

    @Query("SELECT o FROM Order o JOIN FETCH o.address WHERE o.user.id = :userId")
    Page<Order> findAllByUserId(Long userId, Pageable pageable);

    @Query("SELECT COALESCE(SUM(o.finalAmount), 0) FROM Order o WHERE o.user.id = :userId AND o.status <> :excludedStatus")
    int sumFinalAmountByUserIdExcludingStatus(Long userId, OrderStatus excludedStatus);
}
