package com.web.ecommerce.domain.product.repository;

import com.web.ecommerce.domain.product.entity.ProductSyncSchedule;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProductSyncScheduleRepository extends JpaRepository<ProductSyncSchedule, Long> {

    Optional<ProductSyncSchedule> findByActiveTrue();
}
