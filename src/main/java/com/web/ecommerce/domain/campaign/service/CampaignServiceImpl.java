package com.web.ecommerce.domain.campaign.service;

import com.web.ecommerce.domain.ad.entity.AD;
import com.web.ecommerce.domain.ad.exception.AdErrorCode;
import com.web.ecommerce.domain.ad.repository.AdRepository;
import com.web.ecommerce.domain.campaign.dto.reqeust.CreateCampaignRequest;
import com.web.ecommerce.domain.campaign.dto.reqeust.GetCampaignsRequest;
import com.web.ecommerce.domain.campaign.dto.reqeust.UpdateCampaignRequest;
import com.web.ecommerce.domain.campaign.dto.response.CampaignResponse;
import com.web.ecommerce.domain.campaign.dto.response.CampaignSummaryResponse;
import com.web.ecommerce.domain.campaign.dto.response.CouponSendResponse;
import com.web.ecommerce.domain.campaign.entity.Campaign;
import com.web.ecommerce.domain.campaign.entity.CampaignFilter;
import com.web.ecommerce.domain.campaign.enums.BatchCycle;
import com.web.ecommerce.domain.campaign.enums.CampaignGoalType;
import com.web.ecommerce.domain.campaign.enums.CollectionType;
import com.web.ecommerce.domain.campaign.enums.CustomerSegment;
import com.web.ecommerce.domain.campaign.enums.Status;
import com.web.ecommerce.domain.campaign.exception.CampaignErrorCode;
import com.web.ecommerce.domain.campaign.mapper.CampaignMapper;
import com.web.ecommerce.domain.campaign.repository.CampaignFilterRepository;
import com.web.ecommerce.domain.campaign.repository.CampaignRepository;
import com.web.ecommerce.domain.coupon.entity.Coupon;
import com.web.ecommerce.domain.coupon.entity.UserCoupon;
import com.web.ecommerce.domain.coupon.enums.IssuanceMethod;
import com.web.ecommerce.domain.coupon.exception.CouponErrorCode;
import com.web.ecommerce.domain.coupon.repository.CouponRepository;
import com.web.ecommerce.domain.coupon.repository.UserCouponRepository;
import com.web.ecommerce.domain.event.entity.EventField;
import com.web.ecommerce.domain.event.repository.EventFieldRepository;
import com.web.ecommerce.domain.user.entity.User;
import com.web.ecommerce.domain.user.exception.UserErrorCode;
import com.web.ecommerce.domain.user.repository.UserRepository;
import com.web.ecommerce.global.exception.CustomException;
import com.web.ecommerce.global.exception.GlobalErrorCode;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CampaignServiceImpl implements CampaignService {

  private final CampaignRepository campaignRepository;
  private final CampaignFilterRepository campaignFilterRepository;
  private final EventFieldRepository eventFieldRepository;
  private final UserRepository userRepository;
  private final CouponRepository couponRepository;
  private final UserCouponRepository userCouponRepository;
  private final AdRepository adRepository;
  private final CampaignMapper campaignMapper;

  @Override
  @Transactional
  public CampaignResponse createCampaign(Long adminId, CreateCampaignRequest request) {
    userRepository.findById(adminId)
        .orElseThrow(() -> new CustomException(UserErrorCode.USER_NOT_FOUND));

    if (request.getCouponId() != null && request.getAdId() != null) {
      throw new CustomException(CampaignErrorCode.CANNOT_SET_BOTH_AD_AND_COUPON);
    }

    Coupon coupon = request.getCouponId() != null
        ? couponRepository.findById(request.getCouponId())
            .orElseThrow(() -> new CustomException(CouponErrorCode.COUPON_NOT_FOUND))
        : null;

    AD ad = request.getAdId() != null
        ? adRepository.findById(request.getAdId())
            .orElseThrow(() -> new CustomException(AdErrorCode.AD_NOT_FOUND))
        : null;

    Campaign campaign = Campaign.builder()
        .campaignName(request.getCampaignName())
        .description(request.getDescription())
        .campaignGoalType(CampaignGoalType.valueOf(request.getCampaignGoalType()))
        .customerSegment(CustomerSegment.valueOf(request.getCustomerSegment()))
        .collectionType(CollectionType.valueOf(request.getCollectionType()))
        .status(Status.IN_PROGRESS)
        .startedAt(LocalDate.parse(request.getStartedAt()).atStartOfDay())
        .endedAt(LocalDate.parse(request.getEndedAt()).atTime(23, 59, 59))
        .batchCycle(request.getBatchCycle())
        .batchTime(request.getBatchTime() != null ? LocalTime.parse(request.getBatchTime()) : null)
        .batchDayOfWeek(request.getBatchCycle() == BatchCycle.WEEKLY && request.getBatchDayOfWeek() != null ? DayOfWeek.valueOf(request.getBatchDayOfWeek()) : null)
        .batchDayOfMonth(request.getBatchCycle() == BatchCycle.MONTHLY ? request.getBatchDayOfMonth() : null)
        .filterLogicalOperator(request.getFilterLogicalOperator())
        .couponRestrictionDays(request.getCouponRestrictionDays())
        .issueType(request.getIssueType() != null ? IssuanceMethod.valueOf(request.getIssueType()) : null)
        .coupon(coupon)
        .ad(ad)
        .build();

    campaignRepository.save(campaign);

    List<CampaignFilter> filters = buildFilters(campaign, request.getFilters());
    campaignFilterRepository.saveAll(filters);

    return campaignMapper.toCampaignResponse(campaign, filters);
  }

  @Override
  @Transactional(readOnly = true)
  public Page<CampaignSummaryResponse> getCampaigns(GetCampaignsRequest request) {
    Pageable pageable = PageRequest.of(request.getPage(), request.getSize());
    return campaignRepository.findByFilters(
            request.getStatus(),
            request.getCampaignGoalType(),
            request.getCustomerSegment(),
            request.getCollectionType(),
            pageable)
        .map(campaignMapper::toCampaignSummaryResponse);
  }

  @Override
  @Transactional(readOnly = true)
  public CampaignResponse getCampaign(Long campaignId) {
    Campaign campaign = campaignRepository.findById(campaignId)
        .orElseThrow(() -> new CustomException(CampaignErrorCode.CAMPAIGN_NOT_FOUND));

    List<CampaignFilter> filters = campaignFilterRepository.findByCampaignId(campaignId);
    return campaignMapper.toCampaignResponse(campaign, filters);
  }

  @Override
  @Transactional
  public CampaignResponse updateCampaign(Long adminId, Long campaignId, UpdateCampaignRequest request) {
    userRepository.findById(adminId)
        .orElseThrow(() -> new CustomException(UserErrorCode.USER_NOT_FOUND));

    Campaign campaign = campaignRepository.findById(campaignId)
        .orElseThrow(() -> new CustomException(CampaignErrorCode.CAMPAIGN_NOT_FOUND));

    if (request.getCouponId() != null && request.getAdId() != null) {
      throw new CustomException(CampaignErrorCode.CANNOT_SET_BOTH_AD_AND_COUPON);
    }

    Coupon coupon = request.getCouponId() != null
        ? couponRepository.findById(request.getCouponId())
            .orElseThrow(() -> new CustomException(CouponErrorCode.COUPON_NOT_FOUND))
        : null;

    AD ad = request.getAdId() != null
        ? adRepository.findById(request.getAdId())
            .orElseThrow(() -> new CustomException(AdErrorCode.AD_NOT_FOUND))
        : null;

    campaign.update(
        request.getCampaignName(),
        request.getDescription(),
        CampaignGoalType.valueOf(request.getCampaignGoalType()),
        CustomerSegment.valueOf(request.getCustomerSegment()),
        CollectionType.valueOf(request.getCollectionType()),
        LocalDate.parse(request.getStartedAt()).atStartOfDay(),
        LocalDate.parse(request.getEndedAt()).atTime(23, 59, 59),
        request.getBatchCycle(),
        request.getBatchTime() != null ? LocalTime.parse(request.getBatchTime()) : null,
        request.getBatchCycle() == BatchCycle.WEEKLY && request.getBatchDayOfWeek() != null ? DayOfWeek.valueOf(request.getBatchDayOfWeek()) : null,
        request.getBatchCycle() == BatchCycle.MONTHLY ? request.getBatchDayOfMonth() : null,
        request.getFilterLogicalOperator(),
        request.getCouponRestrictionDays(),
        request.getIssueType() != null ? IssuanceMethod.valueOf(request.getIssueType()) : null
    );
    campaign.updateAdAndCoupon(ad, coupon);

    campaignFilterRepository.deleteByCampaignId(campaignId);
    List<CampaignFilter> filters = buildUpdateFilters(campaign, request.getFilters());
    campaignFilterRepository.saveAll(filters);

    return campaignMapper.toCampaignResponse(campaign, filters);
  }

  @Override
  @Transactional
  public CampaignResponse updateCampaignStatus(Long campaignId, Status status) {
    Campaign campaign = campaignRepository.findById(campaignId)
        .orElseThrow(() -> new CustomException(CampaignErrorCode.CAMPAIGN_NOT_FOUND));

    campaign.updateStatus(status);
    List<CampaignFilter> filters = campaignFilterRepository.findByCampaignId(campaignId);
    return campaignMapper.toCampaignResponse(campaign, filters);
  }

  @Override
  @Transactional
  public void deleteCampaign(Long campaignId) {
    Campaign campaign = campaignRepository.findById(campaignId)
        .orElseThrow(() -> new CustomException(CampaignErrorCode.CAMPAIGN_NOT_FOUND));

    campaignFilterRepository.deleteByCampaignId(campaignId);
    campaignRepository.delete(campaign);
  }

  private List<CampaignFilter> buildFilters(Campaign campaign,
      List<CreateCampaignRequest.CreateCampaignFilter> filterRequests) {
    if (filterRequests == null || filterRequests.isEmpty()) {
      return List.of();
    }
    return filterRequests.stream()
        .map(f -> {
          EventField eventField = eventFieldRepository
              .findByEventIdAndFieldName(f.getEventId(), f.getEventFieldName())
              .orElseThrow(() -> new CustomException(GlobalErrorCode.RESOURCE_NOT_FOUND));

          if (!f.getOperator().supports(eventField.getFieldType())) {
            throw new CustomException(CampaignErrorCode.INVALID_OPERATOR_FOR_FIELD_TYPE);
          }

          return CampaignFilter.builder()
              .campaign(campaign)
              .eventField(eventField)
              .operator(f.getOperator())
              .value(f.getValue())
              .periodDays(f.getPeriodDays())
              .build();
        })
        .toList();
  }

  private List<CampaignFilter> buildUpdateFilters(Campaign campaign,
      List<UpdateCampaignRequest.UpdateCampaignFilter> filterRequests) {
    if (filterRequests == null || filterRequests.isEmpty()) {
      return List.of();
    }
    return filterRequests.stream()
        .map(f -> {
          EventField eventField = eventFieldRepository
              .findByEventIdAndFieldName(f.getEventId(), f.getEventFieldName())
              .orElseThrow(() -> new CustomException(GlobalErrorCode.RESOURCE_NOT_FOUND));

          if (!f.getOperator().supports(eventField.getFieldType())) {
            throw new CustomException(CampaignErrorCode.INVALID_OPERATOR_FOR_FIELD_TYPE);
          }

          return CampaignFilter.builder()
              .campaign(campaign)
              .eventField(eventField)
              .operator(f.getOperator())
              .value(f.getValue())
              .periodDays(f.getPeriodDays())
              .build();
        })
        .toList();
  }
}
