package com.web.ecommerce.domain.campaign.mapper;

import com.web.ecommerce.domain.campaign.dto.response.CampaignFilterResponse;
import com.web.ecommerce.domain.campaign.dto.response.CampaignResponse;
import com.web.ecommerce.domain.campaign.dto.response.CampaignSummaryResponse;
import com.web.ecommerce.domain.campaign.entity.Campaign;
import com.web.ecommerce.domain.campaign.entity.CampaignFilter;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class CampaignMapper {

  public CampaignSummaryResponse toCampaignSummaryResponse(Campaign campaign) {
    return CampaignSummaryResponse.builder()
        .campaignId(campaign.getId())
        .campaignGoalType(campaign.getCampaignGoalType().name())
        .customerSegment(campaign.getCustomerSegment().name())
        .campaignName(campaign.getCampaignName())
        .status(campaign.getStatus().name())
        .startedAt(campaign.getStartedAt().toString())
        .endedAt(campaign.getEndedAt().toString())
        .createdBy(campaign.getCreatedBy())
        .createdAt(campaign.getCreatedAt().toString())
        .build();
  }

  public CampaignResponse toCampaignResponse(Campaign campaign, List<CampaignFilter> filters) {
    return CampaignResponse.builder()
        .campaignId(campaign.getId())
        .campaignName(campaign.getCampaignName())
        .description(campaign.getDescription())
        .campaignGoalType(campaign.getCampaignGoalType().name())
        .customerSegment(campaign.getCustomerSegment().name())
        .status(campaign.getStatus().name())
        .collectionType(campaign.getCollectionType().name())
        .batchCycle(campaign.getBatchCycle())
        .isDuplicate(campaign.getIsDuplicate())
        .startedAt(campaign.getStartedAt().toString())
        .endedAt(campaign.getEndedAt().toString())
        .createdBy(campaign.getCreatedBy())
        .createdAt(campaign.getCreatedAt().toString())
        .filters(filters.stream().map(this::toCampaignFilterResponse).toList())
        .build();
  }

  private CampaignFilterResponse toCampaignFilterResponse(CampaignFilter filter) {
    return CampaignFilterResponse.builder()
        .filterId(filter.getId())
        .eventName(filter.getEventField().getEvent().getEventName())
        .fieldName(filter.getEventField().getFieldName())
        .fieldType(filter.getEventField().getFieldType())
        .operator(filter.getOperator())
        .value(filter.getValue())
        .periodDays(filter.getPeriodDays())
        .logicalOperator(filter.getLogicalOperator().name())
        .build();
  }
}
