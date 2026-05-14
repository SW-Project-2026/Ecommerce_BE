package com.web.ecommerce.domain.coupon.service;

import com.web.ecommerce.domain.coupon.dto.request.CreateCouponRequest;
import com.web.ecommerce.domain.coupon.dto.request.UpdateCouponRequest;
import com.web.ecommerce.domain.coupon.dto.response.CouponResponse;
import com.web.ecommerce.domain.coupon.dto.response.UserCouponResponse;
import com.web.ecommerce.domain.coupon.entity.Coupon;
import com.web.ecommerce.domain.coupon.entity.UserCoupon;
import com.web.ecommerce.domain.coupon.enums.DiscountType;
import com.web.ecommerce.domain.coupon.enums.IssuanceMethod;
import com.web.ecommerce.domain.coupon.exception.CouponErrorCode;
import com.web.ecommerce.domain.coupon.mapper.CouponMapper;
import com.web.ecommerce.domain.coupon.repository.CouponRepository;
import com.web.ecommerce.domain.coupon.repository.UserCouponRepository;
import com.web.ecommerce.domain.user.entity.User;
import com.web.ecommerce.domain.user.exception.UserErrorCode;
import com.web.ecommerce.domain.user.repository.UserRepository;
import com.web.ecommerce.global.exception.CustomException;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CouponServiceImpl implements CouponService {

  private final CouponRepository couponRepository;
  private final UserCouponRepository userCouponRepository;
  private final UserRepository userRepository;
  private final CouponMapper couponMapper;

  @Override
  @Transactional
  public CouponResponse createCoupon(CreateCouponRequest request) {
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
        .issuanceMethod(IssuanceMethod.valueOf(request.getIssuanceMethod()))
        .issueLimit(request.getIssueLimit())
        .build();

    couponRepository.save(coupon);
    return couponMapper.toCouponResponse(coupon);
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

    coupon.update(
        request.getName(),
        request.getCode(),
        DiscountType.valueOf(request.getDiscountType()),
        request.getDiscountAmount(),
        request.getMinOrderAmount(),
        request.getMaxDiscountAmount(),
        request.getExpiredAt(),
        IssuanceMethod.valueOf(request.getIssuanceMethod()),
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

  @Override
  @Transactional
  public UserCouponResponse issueCoupon(Long couponId, Long userId) {
    Coupon coupon = couponRepository.findById(couponId)
        .orElseThrow(() -> new CustomException(CouponErrorCode.COUPON_NOT_FOUND));
    User user = userRepository.findById(userId)
        .orElseThrow(() -> new CustomException(UserErrorCode.USER_NOT_FOUND));

    if (userCouponRepository.existsByUserIdAndCouponId(userId, couponId)) {
      throw new CustomException(CouponErrorCode.ALREADY_ISSUED);
    }

    if (coupon.getIssueLimit() != null) {
      long issuedCount = userCouponRepository.countByCouponId(couponId);
      if (issuedCount >= coupon.getIssueLimit()) {
        throw new CustomException(CouponErrorCode.ISSUE_LIMIT_EXCEEDED);
      }
    }

    UserCoupon userCoupon = UserCoupon.builder()
        .user(user)
        .coupon(coupon)
        .status(false)
        .build();

    userCouponRepository.save(userCoupon);
    return couponMapper.toUserCouponResponse(userCoupon);
  }

  @Override
  @Transactional(readOnly = true)
  public Page<UserCouponResponse> getUserCoupons(Long userId, Pageable pageable) {
    return userCouponRepository.findByUserId(userId, pageable).map(couponMapper::toUserCouponResponse);
  }

  @Override
  @Transactional
  public UserCouponResponse useCoupon(Long userCouponId) {
    UserCoupon userCoupon = userCouponRepository.findById(userCouponId)
        .orElseThrow(() -> new CustomException(CouponErrorCode.USER_COUPON_NOT_FOUND));

    if (Boolean.TRUE.equals(userCoupon.getStatus())) {
      throw new CustomException(CouponErrorCode.ALREADY_USED);
    }

    userCoupon.use();
    return couponMapper.toUserCouponResponse(userCoupon);
  }
}
