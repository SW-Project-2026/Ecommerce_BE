package com.web.ecommerce.domain.product.repository;

import com.web.ecommerce.domain.product.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {

    boolean existsByNaverProductId(String naverProductId);
}
