package com.web.ecommerce.domain.campaign.service;

import com.web.ecommerce.domain.campaign.dto.reqeust.CreateCampaignRequest;
import com.web.ecommerce.domain.campaign.dto.reqeust.UpdateCampaignRequest;
import com.web.ecommerce.domain.campaign.dto.reqeust.UpdateCampaignStatusRequest;
import com.web.ecommerce.domain.campaign.dto.response.CampaignResponse;
import com.web.ecommerce.domain.campaign.enums.Status;
import java.util.List;

public interface CampaignService {

  CampaignResponse createCampaign(Long adminId, CreateCampaignRequest request);

  List<CampaignResponse> getCampaigns(Status status);

  CampaignResponse getCampaign(Long campaignId);

  CampaignResponse updateCampaign(Long adminId, Long campaignId, UpdateCampaignRequest request);

  CampaignResponse updateCampaignStatus(Long campaignId, UpdateCampaignStatusRequest request);

  void deleteCampaign(Long campaignId);
}
