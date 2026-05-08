package com.web.ecommerce.domain.campaign.entity;

import com.web.ecommerce.domain.campaign.enums.CampaignGoalType;
import com.web.ecommerce.domain.campaign.enums.CollectionType;
import com.web.ecommerce.domain.campaign.enums.CustomerSegment;
import com.web.ecommerce.domain.campaign.enums.LogicalOperator;
import com.web.ecommerce.domain.campaign.enums.Status;
import com.web.ecommerce.global.common.BaseTimeEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedBy;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Entity
@Table(name = "campaign")
public class Campaign extends BaseTimeEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "campaign_name", nullable = false)
  private String campaignName;

  @Column(name = "description")
  private String description;

  @Column(name = "campaign_goal_type", nullable = false)
  @Enumerated(EnumType.STRING)
  private CampaignGoalType campaignGoalType;

  @Column(name = "customer_segment", nullable = false)
  @Enumerated(EnumType.STRING)
  private CustomerSegment customerSegment;

  @Column(name = "status", nullable = false)
  @Enumerated(EnumType.STRING)
  private Status status;

  @Column(name = "started_at", nullable = false)
  private LocalDateTime startedAt;

  @Column(name = "ended_at", nullable = false)
  private LocalDateTime endedAt;

  @CreatedBy
  @Column(name = "created_by", updatable = false)
  private String createdBy;

  @Column(name = "campaign_type", nullable = false)
  @Enumerated(EnumType.STRING)
  private CollectionType collectionType;

  @Column(name = "batch_cycle")
  private Integer batchCycle;

  @Column(name = "is_duplicate", nullable = false)
  private Boolean isDuplicate;

  @Column(name = "filter_logical_operator")
  @Enumerated(EnumType.STRING)
  private LogicalOperator filterLogicalOperator;

  public void update(String campaignName, String description, CampaignGoalType campaignGoalType,
      CustomerSegment customerSegment, CollectionType collectionType,
      LocalDateTime startedAt, LocalDateTime endedAt, Integer batchCycle, Boolean isDuplicate,
      LogicalOperator filterLogicalOperator) {
    this.campaignName = campaignName;
    this.description = description;
    this.campaignGoalType = campaignGoalType;
    this.customerSegment = customerSegment;
    this.collectionType = collectionType;
    this.startedAt = startedAt;
    this.endedAt = endedAt;
    this.batchCycle = batchCycle;
    this.isDuplicate = isDuplicate;
    this.filterLogicalOperator = filterLogicalOperator;
  }

  public void updateStatus(Status status) {
    this.status = status;
  }
}
