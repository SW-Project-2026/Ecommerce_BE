package com.web.ecommerce.domain.ad.controller;

import com.web.ecommerce.domain.ad.dto.request.CreateAdRequest;
import com.web.ecommerce.domain.ad.dto.request.UpdateAdRequest;
import com.web.ecommerce.domain.ad.dto.response.AdExposureResponse;
import com.web.ecommerce.domain.ad.dto.response.AdResponse;
import com.web.ecommerce.domain.ad.service.AdService;
import com.web.ecommerce.global.response.BaseResponse;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import com.web.ecommerce.global.security.UserPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AdControllerImpl implements AdController {

  private final AdService adService;

  @Override
  @PostMapping("/api/ads")
  public ResponseEntity<BaseResponse<AdResponse>> createAd(@RequestBody CreateAdRequest request) {
    return ResponseEntity.status(HttpStatus.CREATED)
        .body(BaseResponse.success(201, "광고가 생성되었습니다.", adService.createAd(request)));
  }

  @Override
  @GetMapping("/api/ads")
  public ResponseEntity<BaseResponse<List<AdResponse>>> getAds() {
    return ResponseEntity.ok(BaseResponse.success(adService.getAds()));
  }

  @Override
  @GetMapping("/api/ads/{adId}")
  public ResponseEntity<BaseResponse<AdResponse>> getAd(@PathVariable Long adId) {
    return ResponseEntity.ok(BaseResponse.success(adService.getAd(adId)));
  }

  @Override
  @PutMapping("/api/ads/{adId}")
  public ResponseEntity<BaseResponse<AdResponse>> updateAd(@PathVariable Long adId,
      @RequestBody UpdateAdRequest request) {
    return ResponseEntity.ok(BaseResponse.success(adService.updateAd(adId, request)));
  }

  @Override
  @DeleteMapping("/api/ads/{adId}")
  public ResponseEntity<Void> deleteAd(@PathVariable Long adId) {
    adService.deleteAd(adId);
    return ResponseEntity.noContent().build();
  }

  @Override
  @PostMapping("/api/ads/{adId}/expose")
  public ResponseEntity<BaseResponse<AdExposureResponse>> recordExposure(@PathVariable Long adId) {
    Long currentUserId = ((UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).id();
    return ResponseEntity.status(HttpStatus.CREATED)
        .body(BaseResponse.success(201, "광고 노출이 기록되었습니다.", adService.recordExposure(adId, currentUserId)));
  }

  @Override
  @PatchMapping("/api/ads/{adId}/click")
  public ResponseEntity<BaseResponse<AdExposureResponse>> recordClick(@PathVariable Long adId) {
    Long currentUserId = ((UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).id();
    return ResponseEntity.ok(BaseResponse.success(adService.recordClick(adId, currentUserId)));
  }

  @Override
  @GetMapping("/api/users/{userId}/ads")
  public ResponseEntity<BaseResponse<List<AdExposureResponse>>> getUserExposures(@PathVariable Long userId) {
    return ResponseEntity.ok(BaseResponse.success(adService.getUserExposures(userId)));
  }
}
