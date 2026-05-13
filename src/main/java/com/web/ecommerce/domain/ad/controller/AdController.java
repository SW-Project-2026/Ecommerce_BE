package com.web.ecommerce.domain.ad.controller;

import com.web.ecommerce.domain.ad.dto.request.CreateAdRequest;
import com.web.ecommerce.domain.ad.dto.request.UpdateAdRequest;
import com.web.ecommerce.domain.ad.dto.response.AdExposureResponse;
import com.web.ecommerce.domain.ad.dto.response.AdResponse;
import com.web.ecommerce.global.response.BaseResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import org.springframework.http.ResponseEntity;

@Tag(name = "Ad", description = "광고 API")
public interface AdController {

  @Operation(summary = "광고 생성 (ADMIN)")
  ResponseEntity<BaseResponse<AdResponse>> createAd(CreateAdRequest request);

  @Operation(summary = "광고 목록 조회 (ADMIN)")
  ResponseEntity<BaseResponse<List<AdResponse>>> getAds();

  @Operation(summary = "광고 단건 조회 (ADMIN)")
  ResponseEntity<BaseResponse<AdResponse>> getAd(Long adId);

  @Operation(summary = "광고 수정 (ADMIN)")
  ResponseEntity<BaseResponse<AdResponse>> updateAd(Long adId, UpdateAdRequest request);

  @Operation(summary = "광고 삭제 (ADMIN)")
  ResponseEntity<Void> deleteAd(Long adId);

  @Operation(summary = "광고 노출 기록")
  ResponseEntity<BaseResponse<AdExposureResponse>> recordExposure(Long adId);

  @Operation(summary = "광고 클릭 기록")
  ResponseEntity<BaseResponse<AdExposureResponse>> recordClick(Long adId);

  @Operation(summary = "회원 광고 노출 목록 조회")
  ResponseEntity<BaseResponse<List<AdExposureResponse>>> getUserExposures(Long userId);
}
