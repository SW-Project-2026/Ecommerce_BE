package com.web.ecommerce.domain.product.repository;

import com.web.ecommerce.domain.product.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long> {

    boolean existsByNaverProductId(String naverProductId);

    @Query("SELECT MAX(p.createdAt) FROM Product p")
    Optional<LocalDateTime> findLastSyncedAt();

    Page<Product> findByIsActive(int isActive, Pageable pageable);

    Page<Product> findByIsActiveAndProductCategory(int isActive, String productCategory, Pageable pageable);

    Page<Product> findByIsActiveAndNameContainingIgnoreCase(int isActive, String name, Pageable pageable);
}
