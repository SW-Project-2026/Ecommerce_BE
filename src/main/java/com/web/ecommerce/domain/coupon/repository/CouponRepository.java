package com.web.ecommerce.domain.coupon.repository;

import com.web.ecommerce.domain.coupon.entity.Coupon;
import jakarta.persistence.LockModeType;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;

public interface CouponRepository extends JpaRepository<Coupon, Long> {

  boolean existsByCode(String code);

  @Lock(LockModeType.PESSIMISTIC_WRITE)
  @Query("SELECT c FROM Coupon c WHERE c.id = :id")
  Optional<Coupon> findByIdWithLock(Long id);

  @Query("SELECT c FROM Coupon c WHERE c.id > :cursor ORDER BY c.id ASC")
  List<Coupon> findByCursor(Long cursor, Pageable pageable);

  @Query("SELECT c FROM Coupon c ORDER BY c.createdAt DESC")
  List<Coupon> findRecent(Pageable pageable);

  @Query("SELECT ca.coupon FROM Campaign ca " +
      "WHERE ca.customerSegment = com.web.ecommerce.domain.campaign.enums.CustomerSegment.NEW " +
      "AND ca.issueType = com.web.ecommerce.domain.coupon.enums.IssuanceMethod.DOWNLOAD " +
      "AND ca.coupon IS NOT NULL " +
      "AND ca.status = com.web.ecommerce.domain.campaign.enums.Status.IN_PROGRESS")
  List<Coupon> findWelcomeCoupons();
}
