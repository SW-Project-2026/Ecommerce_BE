package com.web.ecommerce.domain.coupon.service;

import com.web.ecommerce.domain.campaign.entity.Campaign;
import com.web.ecommerce.domain.campaign.repository.CampaignRepository;
import com.web.ecommerce.domain.coupon.dto.request.CreateCouponRequest;
import com.web.ecommerce.domain.coupon.dto.request.UpdateCouponRequest;
import com.web.ecommerce.domain.coupon.dto.response.CouponResponse;
import com.web.ecommerce.domain.coupon.dto.response.CouponSelectResponse;
import com.web.ecommerce.domain.coupon.dto.response.DownloadableCouponResponse;
import com.web.ecommerce.domain.coupon.dto.response.UserCouponResponse;
import com.web.ecommerce.domain.coupon.entity.Coupon;
import com.web.ecommerce.domain.coupon.entity.UserCoupon;
import com.web.ecommerce.domain.coupon.enums.CouponStatus;
import com.web.ecommerce.domain.coupon.enums.DiscountType;
import com.web.ecommerce.domain.coupon.enums.IssuanceMethod;
import java.util.ArrayList;
import com.web.ecommerce.domain.coupon.exception.CouponErrorCode;
import com.web.ecommerce.domain.coupon.mapper.CouponMapper;
import com.web.ecommerce.domain.coupon.repository.CouponRepository;
import com.web.ecommerce.domain.coupon.repository.UserCouponRepository;
import com.web.ecommerce.domain.user.entity.User;
import com.web.ecommerce.domain.user.exception.UserErrorCode;
import com.web.ecommerce.domain.user.repository.UserRepository;
import com.web.ecommerce.global.exception.CustomException;
import com.web.ecommerce.global.response.CursorResponse;
import com.web.ecommerce.global.sms.SmsService;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CouponServiceImpl implements CouponService {

  private final CouponRepository couponRepository;
  private final UserCouponRepository userCouponRepository;
  private final UserRepository userRepository;
  private final CampaignRepository campaignRepository;
  private final CouponMapper couponMapper;
  private final SmsService smsService;

  @Override
  @Transactional
  public CouponResponse createCoupon(CreateCouponRequest request) {
    validateCouponRequest(request.getCode(), request.getDiscountType(),
        request.getDiscountAmount(), request.getMinOrderAmount());

    if (couponRepository.existsByCode(request.getCode())) {
      throw new CustomException(CouponErrorCode.DUPLICATE_COUPON_CODE);
    }

    Coupon coupon = Coupon.builder()
        .name(request.getName())
        .code(request.getCode())
        .discountType(DiscountType.valueOf(request.getDiscountType()))
        .discountAmount(request.getDiscountAmount())
        .minOrderAmount(request.getMinOrderAmount())
        .maxDiscountAmount(request.getMaxDiscountAmount())
        .expiredAt(request.getExpiredAt())
        .issueLimit(request.getIssueLimit())
        .build();

    couponRepository.save(coupon);
    return couponMapper.toCouponResponse(coupon);
  }

  @Override
  @Transactional(readOnly = true)
  public CursorResponse<CouponSelectResponse> getCouponSelectList(Long cursor, int size) {
    List<Coupon> coupons = couponRepository.findByCursor(
        cursor == null ? 0L : cursor,
        PageRequest.of(0, size + 1)
    );

    boolean hasNext = coupons.size() > size;
    if (hasNext) {
      coupons = coupons.subList(0, size);
    }

    List<CouponSelectResponse> content = coupons.stream()
        .map(c -> CouponSelectResponse.builder()
            .id(c.getId())
            .name(c.getName())
            .code(c.getCode())
            .discountType(c.getDiscountType().name())
            .discountAmount(c.getDiscountAmount())
            .expiredAt(c.getExpiredAt())
            .build())
        .toList();

    Long nextCursor = hasNext ? coupons.get(coupons.size() - 1).getId() : null;
    return CursorResponse.of(content, nextCursor, hasNext);
  }

  private void validateCouponRequest(String code, String discountType, Integer discountAmount, Integer minOrderAmount) {
    if (!code.matches("^[A-Z0-9]+$")) {
      throw new CustomException(CouponErrorCode.INVALID_COUPON_CODE_FORMAT);
    }
    if ("RATE".equals(discountType)) {
      if (discountAmount == null || discountAmount < 1 || discountAmount > 100) {
        throw new CustomException(CouponErrorCode.INVALID_DISCOUNT_AMOUNT);
      }
    }
    if ("FIXED".equals(discountType) && minOrderAmount == null) {
      throw new CustomException(CouponErrorCode.MIN_ORDER_AMOUNT_REQUIRED);
    }
  }

  @Override
  @Transactional(readOnly = true)
  public Page<CouponResponse> getCoupons(Pageable pageable) {
    return couponRepository.findAll(pageable).map(couponMapper::toCouponResponse);
  }

  @Override
  @Transactional(readOnly = true)
  public CouponResponse getCoupon(Long couponId) {
    Coupon coupon = couponRepository.findById(couponId)
        .orElseThrow(() -> new CustomException(CouponErrorCode.COUPON_NOT_FOUND));
    return couponMapper.toCouponResponse(coupon);
  }

  @Override
  @Transactional
  public CouponResponse updateCoupon(Long couponId, UpdateCouponRequest request) {
    Coupon coupon = couponRepository.findById(couponId)
        .orElseThrow(() -> new CustomException(CouponErrorCode.COUPON_NOT_FOUND));

    validateCouponRequest(request.getCode(), request.getDiscountType(),
        request.getDiscountAmount(), request.getMinOrderAmount());

    if (!coupon.getCode().equals(request.getCode()) && couponRepository.existsByCode(request.getCode())) {
      throw new CustomException(CouponErrorCode.DUPLICATE_COUPON_CODE);
    }

    coupon.update(
        request.getName(),
        request.getCode(),
        DiscountType.valueOf(request.getDiscountType()),
        request.getDiscountAmount(),
        request.getMinOrderAmount(),
        request.getMaxDiscountAmount(),
        request.getExpiredAt(),
        request.getIssueLimit()
    );

    return couponMapper.toCouponResponse(coupon);
  }

  @Override
  @Transactional
  public void deleteCoupon(Long couponId) {
    Coupon coupon = couponRepository.findById(couponId)
        .orElseThrow(() -> new CustomException(CouponErrorCode.COUPON_NOT_FOUND));
    couponRepository.delete(coupon);
  }

  @Transactional
  private UserCouponResponse issueCoupon(Long couponId, Long userId) {
    Coupon coupon = couponRepository.findByIdWithLock(couponId)
        .orElseThrow(() -> new CustomException(CouponErrorCode.COUPON_NOT_FOUND));
    User user = userRepository.findById(userId)
        .orElseThrow(() -> new CustomException(UserErrorCode.USER_NOT_FOUND));

    if (userCouponRepository.existsByUserIdAndCouponId(userId, couponId)) {
      throw new CustomException(CouponErrorCode.ALREADY_ISSUED);
    }

    if (coupon.getIssueLimit() != null) {
      long issuedCount = userCouponRepository.countByCouponIdAndIsDuplicateFalse(couponId);
      if (issuedCount >= coupon.getIssueLimit()) {
        throw new CustomException(CouponErrorCode.ISSUE_LIMIT_EXCEEDED);
      }
    }

    UserCoupon userCoupon = UserCoupon.builder()
        .user(user)
        .coupon(coupon)
        .build();

    userCouponRepository.save(userCoupon);

    smsService.sendCouponNotification(
        user.getPhone(),
        coupon.getName(),
        coupon.getDiscountType().name(),
        coupon.getDiscountAmount()
    );

    return couponMapper.toUserCouponResponse(userCoupon);
  }

  @Override
  @Transactional(readOnly = true)
  public Page<UserCouponResponse> getUserCoupons(Long userId, String status, Pageable pageable) {
    CouponStatus couponStatus = CouponStatus.valueOf(status);
    return userCouponRepository.findByUserIdAndStatusAndIsDuplicateFalse(userId, couponStatus, pageable)
        .map(couponMapper::toUserCouponResponse);
  }

  @Override
  @Transactional(readOnly = true)
  public List<DownloadableCouponResponse> getDownloadableCoupons(Long userId) {
    List<Campaign> campaigns = campaignRepository.findActiveDownloadCampaigns(
        IssuanceMethod.DOWNLOAD, LocalDateTime.now());

    List<DownloadableCouponResponse> result = new ArrayList<>();
    for (Campaign campaign : campaigns) {
      Coupon coupon = campaign.getCoupon();

      if (userCouponRepository.existsByUserIdAndCouponId(userId, coupon.getId())) {
        continue;
      }

      if (coupon.getIssueLimit() != null) {
        long issuedCount = userCouponRepository.countByCouponIdAndIsDuplicateFalse(coupon.getId());
        if (issuedCount >= coupon.getIssueLimit()) {
          continue;
        }
      }

      result.add(DownloadableCouponResponse.builder()
          .couponId(coupon.getId())
          .name(coupon.getName())
          .discountType(coupon.getDiscountType().name())
          .discountAmount(coupon.getDiscountAmount())
          .minOrderAmount(coupon.getMinOrderAmount())
          .maxDiscountAmount(coupon.getMaxDiscountAmount())
          .expiredAt(coupon.getExpiredAt())
          .build());
    }

    return result;
  }

  @Override
  @Transactional
  public UserCouponResponse downloadCoupon(Long couponId, Long userId) {
    return issueCoupon(couponId, userId);
  }

  @Override
  @Transactional
  public UserCouponResponse useCoupon(Long userCouponId) {
    UserCoupon userCoupon = userCouponRepository.findById(userCouponId)
        .orElseThrow(() -> new CustomException(CouponErrorCode.USER_COUPON_NOT_FOUND));

    if (userCoupon.getStatus() == CouponStatus.USED) {
      throw new CustomException(CouponErrorCode.ALREADY_USED);
    }
    if (userCoupon.getStatus() == CouponStatus.EXPIRED) {
      throw new CustomException(CouponErrorCode.COUPON_EXPIRED);
    }

    userCoupon.use();
    return couponMapper.toUserCouponResponse(userCoupon);
  }
}
