package com.web.ecommerce.domain.campaign.entity;

import com.web.ecommerce.domain.campaign.enums.SendStatus;
import com.web.ecommerce.domain.user.entity.User;
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
import jakarta.persistence.UniqueConstraint;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "campaign_target", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"campaign_id", "user_id"})
})
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class CampaignTarget {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "campaign_id", nullable = false)
  private Campaign campaign;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id", nullable = false)
  private User user;

  @Column(name = "sent_at")
  private LocalDateTime sentAt;

  @Enumerated(EnumType.STRING)
  @Column(name = "status", nullable = false)
  @Builder.Default
  private SendStatus status = SendStatus.PENDING;

  public void markSent() {
    this.status = SendStatus.SENT;
    this.sentAt = LocalDateTime.now();
  }

  public void markFailed() {
    this.status = SendStatus.FAILED;
    this.sentAt = LocalDateTime.now();
  }
}
