package com.web.ecommerce.domain.coupon.service;

import com.web.ecommerce.domain.coupon.entity.UserCoupon;
import com.web.ecommerce.domain.coupon.repository.UserCouponRepository;
import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class CouponExpiryBatchService {

  private final UserCouponRepository userCouponRepository;

  // 매일 자정 만료 쿠폰 처리
  @Scheduled(cron = "0 0 0 * * *")
  @Transactional
  public void expireOverdueCoupons() {
    List<UserCoupon> candidates = userCouponRepository.findAvailableWithExpiry();
    LocalDate today = LocalDate.now();
    int count = 0;

    for (UserCoupon uc : candidates) {
      LocalDate expiryDate = uc.getCreatedAt().toLocalDate()
          .plusDays(uc.getCoupon().getExpiredAt());
      if (expiryDate.isBefore(today)) {
        uc.expire();
        count++;
      }
    }

    if (count > 0) {
      log.info("만료 쿠폰 처리 완료: {}건", count);
    }
  }
}
