package com.web.ecommerce.domain.coupon.entity;

import com.web.ecommerce.domain.coupon.enums.DiscountType;
import com.web.ecommerce.domain.coupon.enums.IssuanceMethod;
import com.web.ecommerce.global.common.BaseTimeEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "coupon")
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Coupon extends BaseTimeEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "code", nullable = false)
  private String code;

  @Column(name = "name")
  private String name;

  @Column(name = "discount_type", nullable = false)
  @Enumerated(EnumType.STRING)
  private DiscountType discountType;

  @Column(name = "discount_amount", nullable = false)
  private Integer discountAmount;

  @Column(name = "min_order_amount")
  private Integer minOrderAmount;

  @Column(name = "max_discount_amount")
  private Integer maxDiscountAmount;

  @Column(name = "expired_at")
  private Integer expiredAt;

  @Column(name = "issue_limit")
  private Integer issueLimit;

  @Column(name = "issuance_method", nullable = false)
  @Enumerated(EnumType.STRING)
  private IssuanceMethod issuanceMethod;

  public void update(String name, String code, DiscountType discountType, Integer discountAmount,
      Integer minOrderAmount, Integer maxDiscountAmount, Integer expiredAt,
      IssuanceMethod issuanceMethod, Integer issueLimit) {
    this.name = name;
    this.code = code;
    this.discountType = discountType;
    this.discountAmount = discountAmount;
    this.minOrderAmount = minOrderAmount;
    this.maxDiscountAmount = maxDiscountAmount;
    this.expiredAt = expiredAt;
    this.issuanceMethod = issuanceMethod;
    this.issueLimit = issueLimit;
  }
}
