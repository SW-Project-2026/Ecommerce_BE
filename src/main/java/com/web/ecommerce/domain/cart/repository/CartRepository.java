package com.web.ecommerce.domain.cart.repository;

import com.web.ecommerce.domain.cart.entity.Cart;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface CartRepository extends JpaRepository<Cart, Long> {

    List<Cart> findAllByUserId(Long userId);

    Optional<Cart> findByUserIdAndProductProductId(Long userId, Long productId);

    void deleteAllByUserId(Long userId);

    @Query("SELECT c FROM Cart c JOIN FETCH c.product WHERE c.user.id = :userId AND (:cursor = 0 OR c.id > :cursor) ORDER BY c.id ASC")
    List<Cart> findByUserIdWithCursor(Long userId, Long cursor, Pageable pageable);

    @Query("SELECT c FROM Cart c JOIN FETCH c.product WHERE c.user.id = :userId AND (:cursor = 0 OR c.id < :cursor) ORDER BY c.id DESC")
    List<Cart> findByUserIdWithCursorDesc(Long userId, Long cursor, Pageable pageable);
}
