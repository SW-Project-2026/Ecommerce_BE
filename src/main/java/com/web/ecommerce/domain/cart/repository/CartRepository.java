package com.web.ecommerce.domain.cart.repository;

import com.web.ecommerce.domain.cart.entity.Cart;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CartRepository extends JpaRepository<Cart, Long> {

    List<Cart> findAllByUserId(Long userId);

    Optional<Cart> findByUserIdAndProductProductId(Long userId, Long productId);

    void deleteAllByUserId(Long userId);
}
