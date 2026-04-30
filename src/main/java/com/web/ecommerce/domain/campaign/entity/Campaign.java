package com.web.ecommerce.domain.campaign.entity;


import com.web.ecommerce.domain.campaign.enums.CampaignGoalType;
import com.web.ecommerce.domain.campaign.enums.CampaignType;
import com.web.ecommerce.domain.campaign.enums.Status;
import com.web.ecommerce.domain.campaign.enums.CustomerSegment;
import com.web.ecommerce.global.common.BaseTimeEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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
@Table(name = "campign")
public class Campaign extends BaseTimeEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name ="campaign_name", nullable = false)
  private String campaignName;

  @Column(name ="description")
  private String description;

  @Column(name ="campaign_goal_type", nullable = false)
  @Enumerated(EnumType.STRING)
  private CampaignGoalType campaignGoalType;

  @Column(name ="customer_segment", nullable = false)
  @Enumerated(EnumType.STRING)
  private CustomerSegment customerSegment;

  @Column(name ="status", nullable = false)
  @Enumerated(EnumType.STRING)
  private Status status;

  @Column(name ="started_at", nullable = false)
  private LocalDateTime startedAt;

  @Column(name ="ended_at", nullable = false)
  private LocalDateTime endedAt;

  @CreatedBy
  @Column(name="created_by",updatable = false)
  private Long createdBy;

  @Column(name = "campaign_type", nullable = false)
  @Enumerated(EnumType.STRING)
  private CampaignType campaignType;

  @Column(name="batch_cycle")
  private Integer batchCycle;

  @Column(name="is_duplicate", nullable = false)
  private Boolean isDuplicate;

}
