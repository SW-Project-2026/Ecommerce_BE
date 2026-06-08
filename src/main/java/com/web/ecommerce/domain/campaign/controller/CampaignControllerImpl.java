package com.web.ecommerce.domain.campaign.controller;

import com.web.ecommerce.domain.campaign.dto.reqeust.CreateCampaignRequest;
import com.web.ecommerce.domain.campaign.dto.reqeust.GetCampaignsRequest;
import com.web.ecommerce.domain.campaign.dto.reqeust.SendSmsRequest;
import com.web.ecommerce.domain.campaign.dto.reqeust.UpdateCampaignRequest;
import com.web.ecommerce.domain.campaign.dto.reqeust.WebhookSmsRequest;
import com.web.ecommerce.domain.campaign.dto.response.CampaignResponse;
import com.web.ecommerce.domain.campaign.dto.response.CampaignSummaryResponse;
import com.web.ecommerce.domain.campaign.dto.response.SmsSendResponse;
import com.web.ecommerce.domain.campaign.dto.response.SmsStatusResponse;
import com.web.ecommerce.domain.campaign.enums.Status;
import com.web.ecommerce.domain.campaign.service.CampaignService;
import com.web.ecommerce.global.exception.CustomException;
import com.web.ecommerce.global.exception.GlobalErrorCode;
import com.web.ecommerce.global.response.BaseResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import com.web.ecommerce.global.security.UserPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/campaigns")
@RequiredArgsConstructor
public class CampaignControllerImpl implements CampaignController {

  private final CampaignService campaignService;

  @Value("${webhook.secret}")
  private String webhookSecret;

  @Override
  @PostMapping
  public ResponseEntity<BaseResponse<CampaignResponse>> createCampaign(@RequestBody CreateCampaignRequest request) {
    Long adminId = ((UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).id();
    return ResponseEntity.status(HttpStatus.CREATED)
        .body(BaseResponse.success(201, "캠페인이 생성되었습니다.", campaignService.createCampaign(adminId, request)));
  }

  @Override
  @GetMapping
  public ResponseEntity<BaseResponse<Page<CampaignSummaryResponse>>> getCampaigns(
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
    Long adminId = ((UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).id();
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

  @Override
  @PostMapping("/{campaignId}/send-sms")
  public ResponseEntity<BaseResponse<SmsSendResponse>> sendSms(@PathVariable Long campaignId,
      @RequestBody SendSmsRequest request) {
    return ResponseEntity.ok(BaseResponse.success(campaignService.sendSms(campaignId, request)));
  }

  @Override
  @PostMapping("/{campaignId}/retry-sms")
  public ResponseEntity<BaseResponse<SmsSendResponse>> retrySms(@PathVariable Long campaignId,
      @RequestBody SendSmsRequest request) {
    return ResponseEntity.ok(BaseResponse.success(campaignService.retrySms(campaignId, request)));
  }

  @Override
  @GetMapping("/{campaignId}/sms-status")
  public ResponseEntity<BaseResponse<SmsStatusResponse>> getSmsStatus(@PathVariable Long campaignId,
      @RequestParam(required = false) Long cursor,
      @RequestParam(required = false) String date,
      @RequestParam(required = false) String time) {
    return ResponseEntity.ok(BaseResponse.success(campaignService.getSmsStatus(campaignId, cursor, date, time)));
  }

  @Override
  @PostMapping("/{campaignId}/webhook")
  public ResponseEntity<BaseResponse<SmsSendResponse>> handleEventWebhook(
      @PathVariable Long campaignId,
      @RequestHeader("X-Webhook-Secret") String secret,
      @RequestBody WebhookSmsRequest request) {
    if (!webhookSecret.equals(secret)) {
      throw new CustomException(GlobalErrorCode.UNAUTHORIZED);
    }
    return ResponseEntity.ok(BaseResponse.success(campaignService.handleEventWebhook(campaignId, request.getUserIds(), request.getSource())));
  }

}
