package com.web.ecommerce.domain.campaign.entity;


import com.web.ecommerce.global.common.BaseTimeEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import org.springframework.data.annotation.CreatedBy;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Entity
@Table(name = "Campign")
public class Campaign extends BaseTimeEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String campaignName;

  private String description;

  private CampaignGoalType campaignGoalType;

  private CustomerSegment customerSegment;

  private CampaignStatus campaignStatus;

  private LocalDateTime startedAt;

  private LocalDateTime endedAt;

  @CreatedBy
  @Column(updatable = false)
  private Long createdBy;

}
