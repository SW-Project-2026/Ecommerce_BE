package com.web.ecommerce.domain.campaign.entity;

import com.web.ecommerce.domain.campaign.enums.LogicalOperator;
import com.web.ecommerce.domain.campaign.enums.Operator;
import com.web.ecommerce.domain.event.entity.EventField;
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

@Entity
@Table(name = "campaign_filter")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class CampaignFilter {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "campaign_id", nullable = false)
  private Campaign campaign;

  @Column(name = "operator", nullable = false)
  @Enumerated(EnumType.STRING)
  private Operator operator;

  @Column(name = "value", nullable = false)
  private String value;

  @Column(name = "period_days", nullable = false)
  private Integer periodDays;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "event_field_id", nullable = false)
  private EventField eventField;

  @Column(name = "logical_operator", nullable = false)
  @Enumerated(EnumType.STRING)
  private LogicalOperator logicalOperator;
}
