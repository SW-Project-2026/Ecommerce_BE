package com.web.ecommerce.domain.ad.entity;

import com.web.ecommerce.domain.ad.enums.AdCategory;
import com.web.ecommerce.domain.coupon.entity.UserCoupon;
import com.web.ecommerce.domain.product.entity.Product;
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
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "ad")
public class AD extends BaseTimeEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "ad_id")
  private Long adId;

  @Column(name = "ad_name", nullable = false)
  private String adName;

  @Enumerated(EnumType.STRING)
  @Column(name = "category")
  private AdCategory category;

  @Column(name = "keyword")
  private String keyword;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "ad_exposure_id")
  private AdExposure adExposure;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "product_id")
  private Product product;

}
