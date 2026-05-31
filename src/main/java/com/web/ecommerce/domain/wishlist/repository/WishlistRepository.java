package com.web.ecommerce.domain.wishlist.repository;

import com.web.ecommerce.domain.wishlist.entity.Wishlist;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface WishlistRepository extends JpaRepository<Wishlist, Long> {

    List<Wishlist> findAllByUserId(Long userId);

    Optional<Wishlist> findByUserIdAndProductProductId(Long userId, Long productId);

    boolean existsByUserIdAndProductProductId(Long userId, Long productId);
}
