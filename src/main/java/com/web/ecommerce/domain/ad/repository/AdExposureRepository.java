package com.web.ecommerce.domain.ad.repository;

import com.web.ecommerce.domain.ad.entity.AdExposure;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AdExposureRepository extends JpaRepository<AdExposure, Long> {

  Page<AdExposure> findByUser_Id(Long userId, Pageable pageable);

  Optional<AdExposure> findTopByAd_AdIdAndUser_IdOrderByExposedAtDesc(Long adId, Long userId);
}
