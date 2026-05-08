package com.web.ecommerce.domain.campaign.controller;

import com.web.ecommerce.domain.campaign.dto.reqeust.CreateCampaignRequest;
import com.web.ecommerce.domain.campaign.dto.reqeust.GetCampaignsRequest;
import com.web.ecommerce.domain.campaign.dto.reqeust.UpdateCampaignRequest;
import com.web.ecommerce.domain.campaign.dto.response.CampaignResponse;
import com.web.ecommerce.domain.campaign.dto.response.CampaignSummaryResponse;
import com.web.ecommerce.domain.campaign.enums.Status;
import com.web.ecommerce.domain.campaign.service.CampaignService;
import com.web.ecommerce.global.response.BaseResponse;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/campaigns")
@RequiredArgsConstructor
public class CampaignControllerImpl implements CampaignController {

  private final CampaignService campaignService;

  @Override
  @PostMapping
  public ResponseEntity<BaseResponse<CampaignResponse>> createCampaign(@RequestBody CreateCampaignRequest request) {
    Long adminId = (Long) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    return ResponseEntity.status(HttpStatus.CREATED)
        .body(BaseResponse.success(201, "캠페인이 생성되었습니다.", campaignService.createCampaign(adminId, request)));
  }

  @Override
  @GetMapping
  public ResponseEntity<BaseResponse<List<CampaignSummaryResponse>>> getCampaigns(
      @ModelAttribute GetCampaignsRequest request) {
    return ResponseEntity.ok(BaseResponse.success(campaignService.getCampaigns(request)));
  }

  @Override
  @GetMapping("/{campaignId}")
  public ResponseEntity<BaseResponse<CampaignResponse>> getCampaign(@PathVariable Long campaignId) {
    return ResponseEntity.ok(BaseResponse.success(campaignService.getCampaign(campaignId)));
  }

  @Override
  @PutMapping("/{campaignId}")
  public ResponseEntity<BaseResponse<CampaignResponse>> updateCampaign(@PathVariable Long campaignId,
      @RequestBody UpdateCampaignRequest request) {
    Long adminId = (Long) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    return ResponseEntity.ok(BaseResponse.success(campaignService.updateCampaign(adminId, campaignId, request)));
  }

  @Override
  @PatchMapping("/{campaignId}/status")
  public ResponseEntity<BaseResponse<CampaignResponse>> updateCampaignStatus(@PathVariable Long campaignId,
      @RequestParam Status status) {
    return ResponseEntity.ok(BaseResponse.success(campaignService.updateCampaignStatus(campaignId, status)));
  }

  @Override
  @DeleteMapping("/{campaignId}")
  public ResponseEntity<Void> deleteCampaign(@PathVariable Long campaignId) {
    campaignService.deleteCampaign(campaignId);
    return ResponseEntity.noContent().build();
  }
}
