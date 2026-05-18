package com.web.ecommerce.domain.campaign.service;

import com.web.ecommerce.domain.campaign.dto.reqeust.CreateCampaignRequest;
import com.web.ecommerce.domain.campaign.dto.reqeust.GetCampaignsRequest;
import com.web.ecommerce.domain.campaign.dto.reqeust.UpdateCampaignRequest;
import com.web.ecommerce.domain.campaign.dto.response.CampaignResponse;
import com.web.ecommerce.domain.campaign.dto.response.CampaignSummaryResponse;
import com.web.ecommerce.domain.campaign.enums.Status;
import org.springframework.data.domain.Page;

public interface CampaignService {

  CampaignResponse createCampaign(Long adminId, CreateCampaignRequest request);

  Page<CampaignSummaryResponse> getCampaigns(GetCampaignsRequest request);

  CampaignResponse getCampaign(Long campaignId);

  CampaignResponse updateCampaign(Long adminId, Long campaignId, UpdateCampaignRequest request);

  CampaignResponse updateCampaignStatus(Long campaignId, Status status);

  void deleteCampaign(Long campaignId);
}
