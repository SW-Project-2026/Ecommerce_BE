package com.web.ecommerce.domain.wishlist.repository;

import com.web.ecommerce.domain.wishlist.entity.Wishlist;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface WishlistRepository extends JpaRepository<Wishlist, Long> {

    List<Wishlist> findAllByUserId(Long userId);

    Optional<Wishlist> findByUserIdAndProductProductId(Long userId, Long productId);

    boolean existsByUserIdAndProductProductId(Long userId, Long productId);

    Page<Wishlist> findAllByUserId(Long userId, Pageable pageable);

    @Query("SELECT w FROM Wishlist w WHERE w.user.id = :userId AND w.id > :cursor ORDER BY w.id ASC")
    List<Wishlist> findByUserIdWithCursor(Long userId, Long cursor, Pageable pageable);

    @Query("SELECT w.product.productId FROM Wishlist w WHERE w.user.id = :userId")
    Set<Long> findProductIdsByUserId(@Param("userId") Long userId);
}
