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
        .batchCycle(campaign.getBatchCycle() != null ? campaign.getBatchCycle().name() : null)
        .batchTime(campaign.getBatchTime() != null ? campaign.getBatchTime().toString() : null)
        .batchDayOfWeek(campaign.getBatchDayOfWeek() != null ? campaign.getBatchDayOfWeek().name() : null)
        .batchDayOfMonth(campaign.getBatchDayOfMonth())
        .startedAt(campaign.getStartedAt().toString())
        .endedAt(campaign.getEndedAt().toString())
        .createdBy(campaign.getCreatedBy())
        .createdAt(campaign.getCreatedAt().toString())
        .filterLogicalOperator(campaign.getFilterLogicalOperator() != null
            ? campaign.getFilterLogicalOperator().name() : null)
        .couponRestrictionDays(campaign.getCouponRestrictionDays())
        .issueType(campaign.getIssueType() != null ? campaign.getIssueType().name() : null)
        .couponId(campaign.getCoupon() != null ? campaign.getCoupon().getId() : null)
        .couponName(campaign.getCoupon() != null ? campaign.getCoupon().getName() : null)
        .adId(campaign.getAd() != null ? campaign.getAd().getAdId() : null)
        .adName(campaign.getAd() != null ? campaign.getAd().getAdName() : null)
        .messageType(campaign.getMessageType())
        .messageSubject("LMS".equals(campaign.getMessageType()) ? campaign.getMessageSubject() : null)
        .messageContent(campaign.getMessageContent())
        .filters(filters.stream().map(this::toCampaignFilterResponse).toList())
        .build();
  }

  private CampaignFilterResponse toCampaignFilterResponse(CampaignFilter filter) {
    return CampaignFilterResponse.builder()
        .filterId(filter.getId())
        .eventName(filter.getEventField().getEvent().getEventName())
        .fieldName(filter.getEventField().getFieldName())
        .fieldType(filter.getEventField().getFieldType().name())
        .operator(filter.getOperator().name())
        .value(filter.getValue())
        .periodDays(filter.getPeriodDays())
        .build();
  }
}
