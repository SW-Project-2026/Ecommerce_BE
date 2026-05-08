package com.web.ecommerce.domain.campaign.controller;

import com.web.ecommerce.domain.campaign.dto.reqeust.CreateCampaignRequest;
import com.web.ecommerce.domain.campaign.dto.reqeust.UpdateCampaignRequest;
import com.web.ecommerce.domain.campaign.dto.reqeust.UpdateCampaignStatusRequest;
import com.web.ecommerce.domain.campaign.dto.response.CampaignResponse;
import com.web.ecommerce.domain.campaign.enums.Status;
import com.web.ecommerce.global.response.BaseResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@Tag(name = "Campaign", description = "캠페인 관리 API")
public interface CampaignController {

  @Operation(summary = "캠페인 생성")
  BaseResponse<CampaignResponse> createCampaign(@RequestBody CreateCampaignRequest request);

  @Operation(summary = "캠페인 목록 조회")
  BaseResponse<List<CampaignResponse>> getCampaigns(@RequestParam(required = false) Status status);

  @Operation(summary = "캠페인 단건 조회")
  BaseResponse<CampaignResponse> getCampaign(@PathVariable Long campaignId);

  @Operation(summary = "캠페인 수정")
  BaseResponse<CampaignResponse> updateCampaign(@PathVariable Long campaignId,
      @RequestBody UpdateCampaignRequest request);

  @Operation(summary = "캠페인 상태 변경")
  BaseResponse<CampaignResponse> updateCampaignStatus(@PathVariable Long campaignId,
      @RequestBody UpdateCampaignStatusRequest request);

  @Operation(summary = "캠페인 삭제")
  void deleteCampaign(@PathVariable Long campaignId);
}
