package com.web.ecommerce.domain.coupon.entity;

import com.web.ecommerce.domain.user.entity.User;
import com.web.ecommerce.global.common.BaseTimeEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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

  @Column(name = "used_at")
  private LocalDateTime usedAt;

  @Column(name = "status", nullable = false)
  private Boolean status;

  public void use() {
    this.status = true;
    this.usedAt = LocalDateTime.now();
  }
}
