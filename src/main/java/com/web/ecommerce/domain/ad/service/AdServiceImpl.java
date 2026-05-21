package com.web.ecommerce.domain.ad.service;

import com.web.ecommerce.domain.ad.dto.request.CreateAdRequest;
import com.web.ecommerce.domain.ad.dto.request.UpdateAdRequest;
import com.web.ecommerce.domain.ad.dto.response.AdExposureResponse;
import com.web.ecommerce.domain.ad.dto.response.AdResponse;
import com.web.ecommerce.domain.ad.dto.response.AdSelectResponse;
import com.web.ecommerce.domain.ad.entity.AD;
import com.web.ecommerce.domain.ad.entity.AdExposure;
import com.web.ecommerce.domain.ad.enums.AdCategory;
import com.web.ecommerce.domain.ad.enums.AdTargetType;
import com.web.ecommerce.domain.ad.exception.AdErrorCode;
import com.web.ecommerce.domain.ad.mapper.AdMapper;
import com.web.ecommerce.domain.ad.repository.AdExposureRepository;
import com.web.ecommerce.domain.ad.repository.AdRepository;
import com.web.ecommerce.domain.product.entity.Product;
import com.web.ecommerce.domain.product.repository.ProductRepository;
import com.web.ecommerce.domain.user.entity.User;
import com.web.ecommerce.domain.user.exception.UserErrorCode;
import com.web.ecommerce.domain.user.repository.UserRepository;
import com.web.ecommerce.global.exception.CustomException;
import com.web.ecommerce.global.exception.GlobalErrorCode;
import com.web.ecommerce.global.response.CursorResponse;
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
public class AdServiceImpl implements AdService {

  private final AdRepository adRepository;
  private final AdExposureRepository adExposureRepository;
  private final ProductRepository productRepository;
  private final UserRepository userRepository;
  private final AdMapper adMapper;

  @Override
  @Transactional
  public AdResponse createAd(CreateAdRequest request) {
    Product product = null;
    if (request.getProductId() != null) {
      product = productRepository.findById(request.getProductId())
          .orElseThrow(() -> new CustomException(GlobalErrorCode.RESOURCE_NOT_FOUND));
    }

    AD ad = AD.builder()
        .adName(request.getAdName())
        .targetType(AdTargetType.valueOf(request.getTargetType()))
        .product(product)
        .category(request.getCategory() != null ? AdCategory.valueOf(request.getCategory()) : null)
        .keyword(request.getKeyword())
        .build();

    adRepository.save(ad);
    return adMapper.toAdResponse(ad);
  }

  @Override
  @Transactional(readOnly = true)
  public Page<AdResponse> getAds(Pageable pageable) {
    return adRepository.findAll(pageable).map(adMapper::toAdResponse);
  }

  @Override
  @Transactional(readOnly = true)
  public AdResponse getAd(Long adId) {
    AD ad = adRepository.findById(adId)
        .orElseThrow(() -> new CustomException(AdErrorCode.AD_NOT_FOUND));
    return adMapper.toAdResponse(ad);
  }

  @Override
  @Transactional
  public AdResponse updateAd(Long adId, UpdateAdRequest request) {
    AD ad = adRepository.findById(adId)
        .orElseThrow(() -> new CustomException(AdErrorCode.AD_NOT_FOUND));

    Product product = null;
    if (request.getProductId() != null) {
      product = productRepository.findById(request.getProductId())
          .orElseThrow(() -> new CustomException(GlobalErrorCode.RESOURCE_NOT_FOUND));
    }

    ad.update(
        request.getAdName(),
        AdTargetType.valueOf(request.getTargetType()),
        product,
        request.getCategory() != null ? AdCategory.valueOf(request.getCategory()) : null,
        request.getKeyword()
    );

    return adMapper.toAdResponse(ad);
  }

  @Override
  @Transactional
  public void deleteAd(Long adId) {
    AD ad = adRepository.findById(adId)
        .orElseThrow(() -> new CustomException(AdErrorCode.AD_NOT_FOUND));
    adRepository.delete(ad);
  }

  @Override
  @Transactional
  public AdExposureResponse recordExposure(Long adId, Long userId) {
    AD ad = adRepository.findById(adId)
        .orElseThrow(() -> new CustomException(AdErrorCode.AD_NOT_FOUND));
    User user = userRepository.findById(userId)
        .orElseThrow(() -> new CustomException(UserErrorCode.USER_NOT_FOUND));

    AdExposure exposure = AdExposure.builder()
        .ad(ad)
        .user(user)
        .exposedAt(LocalDateTime.now())
        .build();

    adExposureRepository.save(exposure);
    return adMapper.toAdExposureResponse(exposure);
  }

  @Override
  @Transactional
  public AdExposureResponse recordClick(Long adId, Long userId) {
    AdExposure exposure = adExposureRepository.findTopByAd_AdIdAndUser_IdOrderByExposedAtDesc(adId, userId)
        .orElseThrow(() -> new CustomException(AdErrorCode.AD_EXPOSURE_NOT_FOUND));

    exposure.click();
    return adMapper.toAdExposureResponse(exposure);
  }

  @Override
  @Transactional(readOnly = true)
  public Page<AdExposureResponse> getUserExposures(Long userId, Pageable pageable) {
    return adExposureRepository.findByUser_Id(userId, pageable).map(adMapper::toAdExposureResponse);
  }

  @Override
  @Transactional(readOnly = true)
  public CursorResponse<AdSelectResponse> getAdSelectList(Long cursor, int size) {
    List<AD> ads = adRepository.findByCursor(
        cursor == null ? 0L : cursor,
        PageRequest.of(0, size + 1)
    );

    boolean hasNext = ads.size() > size;
    if (hasNext) {
      ads = ads.subList(0, size);
    }

    List<AdSelectResponse> content = ads.stream()
        .map(a -> AdSelectResponse.builder()
            .id(a.getAdId())
            .adName(a.getAdName())
            .targetType(a.getTargetType().name())
            .category(a.getCategory() != null ? a.getCategory().name() : null)
            .build())
        .toList();

    Long nextCursor = hasNext ? ads.get(ads.size() - 1).getAdId() : null;
    return CursorResponse.of(content, nextCursor, hasNext);
  }
}
