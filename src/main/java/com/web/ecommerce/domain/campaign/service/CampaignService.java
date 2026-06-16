package com.web.ecommerce.domain.campaign.service;

import com.web.ecommerce.domain.campaign.dto.reqeust.CreateCampaignRequest;
import com.web.ecommerce.domain.campaign.dto.reqeust.GetCampaignsRequest;
import com.web.ecommerce.domain.campaign.dto.reqeust.UpdateCampaignRequest;
import com.web.ecommerce.domain.campaign.dto.reqeust.SendSmsRequest;
import com.web.ecommerce.domain.campaign.dto.response.CampaignResponse;
import com.web.ecommerce.domain.campaign.dto.response.CampaignSummaryResponse;
import com.web.ecommerce.domain.campaign.dto.response.SmsSendResponse;
import com.web.ecommerce.domain.campaign.dto.response.SmsStatusResponse;
import com.web.ecommerce.domain.campaign.enums.CollectionType;
import com.web.ecommerce.domain.campaign.enums.Status;
import java.util.List;
import org.springframework.data.domain.Page;

public interface CampaignService {

  CampaignResponse createCampaign(Long adminId, CreateCampaignRequest request);

  Page<CampaignSummaryResponse> getCampaigns(GetCampaignsRequest request);

  CampaignResponse getCampaign(Long campaignId);

  CampaignResponse updateCampaign(Long adminId, Long campaignId, UpdateCampaignRequest request);

  CampaignResponse updateCampaignStatus(Long campaignId, Status status);

  void deleteCampaign(Long campaignId);

  SmsSendResponse sendSms(Long campaignId, SendSmsRequest request);

  SmsSendResponse retrySms(Long campaignId, SendSmsRequest request);

  void executeBatchSend(Long campaignId);

  SmsStatusResponse getSmsStatus(Long campaignId, Long cursor, String date, String time);

  SmsSendResponse handleEventWebhook(Long campaignId, List<Long> userIds, CollectionType source);
}
