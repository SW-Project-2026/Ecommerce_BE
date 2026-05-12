package com.web.ecommerce.domain.ad.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "ad_exposure")
public class AdExposure {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "ad_exposure_id")
  private Long id;

  @Column(name = "exposed_at", nullable = false)
  private LocalDateTime exposedAt;

  @Column(name = "clicked", nullable = false)
  @Builder.Default
  private Boolean clicked = false;

  @Column(name = "clicked_at", nullable = false)
  private LocalDateTime clickedAt;

}
