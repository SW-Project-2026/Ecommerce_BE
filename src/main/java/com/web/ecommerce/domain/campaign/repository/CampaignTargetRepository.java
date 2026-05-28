package com.web.ecommerce.domain.campaign.repository;

import com.web.ecommerce.domain.campaign.entity.CampaignTarget;
import com.web.ecommerce.domain.campaign.enums.SendStatus;
import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CampaignTargetRepository extends JpaRepository<CampaignTarget, Long> {

  @Query("SELECT ct FROM CampaignTarget ct JOIN FETCH ct.user WHERE ct.campaign.id = :campaignId")
  List<CampaignTarget> findByCampaignIdWithUser(@Param("campaignId") Long campaignId);

  @Query("SELECT ct FROM CampaignTarget ct JOIN FETCH ct.user WHERE ct.campaign.id = :campaignId AND ct.status = :status")
  List<CampaignTarget> findByCampaignIdAndStatusWithUser(@Param("campaignId") Long campaignId, @Param("status") SendStatus status);

  @Query("SELECT ct FROM CampaignTarget ct JOIN FETCH ct.user WHERE ct.campaign.id = :campaignId " +
      "AND (:cursor IS NULL OR ct.id > :cursor) ORDER BY ct.id ASC")
  List<CampaignTarget> findByCampaignIdWithCursor(
      @Param("campaignId") Long campaignId,
      @Param("cursor") Long cursor,
      Pageable pageable);

  long countByCampaignIdAndStatus(Long campaignId, SendStatus status);

  void deleteByCampaignId(Long campaignId);
}
