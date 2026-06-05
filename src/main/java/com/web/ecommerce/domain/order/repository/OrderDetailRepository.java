package com.web.ecommerce.domain.order.repository;

import com.web.ecommerce.domain.order.entity.OrderDetail;
import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface OrderDetailRepository extends JpaRepository<OrderDetail, Long> {

    @Query("SELECT od FROM OrderDetail od JOIN FETCH od.product WHERE od.order.user.id = :userId AND (:cursor = 0 OR od.id < :cursor) ORDER BY od.id DESC")
    List<OrderDetail> findByUserIdWithCursor(@Param("userId") Long userId, @Param("cursor") Long cursor, Pageable pageable);
}
