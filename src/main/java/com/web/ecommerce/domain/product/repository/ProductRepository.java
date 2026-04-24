package com.web.ecommerce.domain.product.repository;

import com.web.ecommerce.domain.product.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {

    boolean existsByNaverProductId(String naverProductId);

    Page<Product> findByIsActive(int isActive, Pageable pageable);

    Page<Product> findByIsActiveAndProductCategory(int isActive, String productCategory, Pageable pageable);
}
