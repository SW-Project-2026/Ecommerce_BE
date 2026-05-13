package com.web.ecommerce.domain.ad.repository;

import com.web.ecommerce.domain.ad.entity.AdExposure;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AdExposureRepository extends JpaRepository<AdExposure, Long> {

  List<AdExposure> findByUser_Id(Long userId);

  Optional<AdExposure> findTopByAd_AdIdAndUser_IdOrderByExposedAtDesc(Long adId, Long userId);
}
