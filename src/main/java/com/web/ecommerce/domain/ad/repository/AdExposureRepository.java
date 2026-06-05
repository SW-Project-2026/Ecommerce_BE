package com.web.ecommerce.domain.ad.repository;

import com.web.ecommerce.domain.ad.entity.AdExposure;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface AdExposureRepository extends JpaRepository<AdExposure, Long> {

  Page<AdExposure> findByUser_Id(Long userId, Pageable pageable);

  Optional<AdExposure> findTopByAd_AdIdAndUser_IdOrderByExposedAtDesc(Long adId, Long userId);

  long countByUser_Id(Long userId);

  long countByUser_IdAndClicked(Long userId, boolean clicked);

  long countByClicked(boolean clicked);

  @Query("SELECT a.user.id, COUNT(a), SUM(CASE WHEN a.clicked = true THEN 1 ELSE 0 END) FROM AdExposure a WHERE a.user.id IN :userIds GROUP BY a.user.id")
  List<Object[]> findAdStatsByUserIds(@Param("userIds") List<Long> userIds);
}
