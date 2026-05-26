package com.web.ecommerce.domain.campaign.controller;

import com.web.ecommerce.domain.campaign.dto.reqeust.CreateCampaignRequest;
import com.web.ecommerce.domain.campaign.dto.reqeust.GetCampaignsRequest;
import com.web.ecommerce.domain.campaign.dto.reqeust.SendSmsRequest;
import com.web.ecommerce.domain.campaign.dto.reqeust.UpdateCampaignRequest;
import com.web.ecommerce.domain.campaign.dto.response.CampaignResponse;
import com.web.ecommerce.domain.campaign.dto.response.CampaignSummaryResponse;
import com.web.ecommerce.domain.campaign.dto.response.SmsSendResponse;
import com.web.ecommerce.domain.campaign.enums.Status;
import com.web.ecommerce.global.response.BaseResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@Tag(name = "Campaign", description = "캠페인 관리 API")
public interface CampaignController {

  @Operation(summary = "캠페인 생성")
  ResponseEntity<BaseResponse<CampaignResponse>> createCampaign(@RequestBody CreateCampaignRequest request);

  @Operation(summary = "캠페인 목록 조회")
  ResponseEntity<BaseResponse<Page<CampaignSummaryResponse>>> getCampaigns(@ModelAttribute GetCampaignsRequest request);

  @Operation(summary = "캠페인 단건 조회")
  ResponseEntity<BaseResponse<CampaignResponse>> getCampaign(@PathVariable Long campaignId);

  @Operation(summary = "캠페인 수정")
  ResponseEntity<BaseResponse<CampaignResponse>> updateCampaign(@PathVariable Long campaignId,
      @RequestBody UpdateCampaignRequest request);

  @Operation(summary = "캠페인 상태 변경")
  ResponseEntity<BaseResponse<CampaignResponse>> updateCampaignStatus(@PathVariable Long campaignId,
      @RequestParam Status status);

  @Operation(summary = "캠페인 삭제")
  ResponseEntity<Void> deleteCampaign(@PathVariable Long campaignId);

  @Operation(summary = "캠페인 문자 발송 (SMS/LMS)")
  ResponseEntity<BaseResponse<SmsSendResponse>> sendSms(@PathVariable Long campaignId,
      @RequestBody SendSmsRequest request);

  @Operation(summary = "캠페인 문자 재발송 (실패 대상)")
  ResponseEntity<BaseResponse<SmsSendResponse>> retrySms(@PathVariable Long campaignId,
      @RequestBody SendSmsRequest request);
}
