package com.web.ecommerce.domain.ad.service;

import com.web.ecommerce.domain.ad.dto.request.CreateAdRequest;
import com.web.ecommerce.domain.ad.dto.request.UpdateAdRequest;
import com.web.ecommerce.domain.ad.dto.response.AdExposureResponse;
import com.web.ecommerce.domain.ad.dto.response.AdResponse;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface AdService {

  AdResponse createAd(CreateAdRequest request);

  Page<AdResponse> getAds(Pageable pageable);

  AdResponse getAd(Long adId);

  AdResponse updateAd(Long adId, UpdateAdRequest request);

  void deleteAd(Long adId);

  AdExposureResponse recordExposure(Long adId, Long userId);

  AdExposureResponse recordClick(Long adId, Long userId);

  Page<AdExposureResponse> getUserExposures(Long userId, Pageable pageable);
}
