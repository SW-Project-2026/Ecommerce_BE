package com.web.ecommerce.domain.coupon.entity;

import com.web.ecommerce.domain.campaign.entity.Campaign;
import com.web.ecommerce.domain.coupon.enums.CouponStatus;
import com.web.ecommerce.domain.user.entity.User;
import com.web.ecommerce.global.common.BaseTimeEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "user_coupon")
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserCoupon extends BaseTimeEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id", nullable = false)
  private User user;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "coupon_id", nullable = false)
  private Coupon coupon;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "campaign_id")
  private Campaign campaign;

  @Column(name = "is_duplicate", nullable = false)
  @Builder.Default
  private Boolean isDuplicate = false;

  @Column(name = "used_at")
  private LocalDateTime usedAt;

  @Enumerated(EnumType.STRING)
  @Column(name = "status", nullable = false)
  @Builder.Default
  private CouponStatus status = CouponStatus.AVAILABLE;

  public void use() {
    if(this.status == CouponStatus.USED) {
      throw new IllegalStateException("Coupon is already used");
    }
    if(this.status == CouponStatus.EXPIRED){
      throw new IllegalStateException("Coupon is expired");
    }
    this.status = CouponStatus.USED;
    this.usedAt = LocalDateTime.now();
  }
  //쿠폰 만료처리
  public void expire() {
    if(this.status == CouponStatus.AVAILABLE) {
      this.status = CouponStatus.EXPIRED;
    }
  }
}
