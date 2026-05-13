package com.web.ecommerce.domain.ad.entity;

import com.web.ecommerce.domain.user.entity.User;
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

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "ad_id", nullable = false)
  private AD ad;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id", nullable = false)
  private User user;

  @Column(name = "exposed_at", nullable = false)
  private LocalDateTime exposedAt;

  @Column(name = "clicked", nullable = false)
  @Builder.Default
  private Boolean clicked = false;

  @Column(name = "clicked_at")
  private LocalDateTime clickedAt;

}
