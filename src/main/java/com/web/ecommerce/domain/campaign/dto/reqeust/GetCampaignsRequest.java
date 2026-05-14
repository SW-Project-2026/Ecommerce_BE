package com.web.ecommerce.domain.campaign.dto.reqeust;

import com.web.ecommerce.domain.campaign.enums.CampaignGoalType;
import com.web.ecommerce.domain.campaign.enums.CollectionType;
import com.web.ecommerce.domain.campaign.enums.CustomerSegment;
import com.web.ecommerce.domain.campaign.enums.Status;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class GetCampaignsRequest {

  private Status status;
  private CampaignGoalType campaignGoalType;
  private CustomerSegment customerSegment;
  private CollectionType collectionType;

  private int page = 0;
  private int size = 20;
}
