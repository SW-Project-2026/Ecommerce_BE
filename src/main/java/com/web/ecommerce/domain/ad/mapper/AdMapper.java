package com.web.ecommerce.domain.ad.mapper;

import com.web.ecommerce.domain.ad.dto.response.AdExposureResponse;
import com.web.ecommerce.domain.ad.dto.response.AdResponse;
import com.web.ecommerce.domain.ad.entity.AD;
import com.web.ecommerce.domain.ad.entity.AdExposure;
import org.springframework.stereotype.Component;

@Component
public class AdMapper {

  public AdResponse toAdResponse(AD ad) {
    return AdResponse.builder()
        .adId(ad.getAdId())
        .adName(ad.getAdName())
        .targetType(ad.getTargetType().name())
        .productId(ad.getProduct() != null ? ad.getProduct().getProductId() : null)
        .category(ad.getCategory() != null ? ad.getCategory().name() : null)
        .keyword(ad.getKeyword())
        .createdAt(ad.getCreatedAt() != null ? ad.getCreatedAt().toString() : null)
        .build();
  }

  public AdExposureResponse toAdExposureResponse(AdExposure exposure) {
    return AdExposureResponse.builder()
        .adExposureId(exposure.getId())
        .adId(exposure.getAd().getAdId())
        .adName(exposure.getAd().getAdName())
        .userId(exposure.getUser().getId())
        .clicked(exposure.getClicked())
        .exposedAt(exposure.getExposedAt().toString())
        .clickedAt(exposure.getClickedAt() != null ? exposure.getClickedAt().toString() : null)
        .build();
  }
}
