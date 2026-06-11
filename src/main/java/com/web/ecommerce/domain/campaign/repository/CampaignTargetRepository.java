package com.web.ecommerce.domain.campaign.repository;

import com.web.ecommerce.domain.campaign.entity.CampaignTarget;
import com.web.ecommerce.domain.campaign.enums.SendStatus;
import java.time.LocalDateTime;
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

  @Query("SELECT COUNT(ct) FROM CampaignTarget ct WHERE ct.campaign.id = :campaignId " +
      "AND ct.status = :status AND ct.sentAt >= :from AND ct.sentAt < :to")
  long countByCampaignIdAndStatusAndSentAtBetween(
      @Param("campaignId") Long campaignId,
      @Param("status") SendStatus status,
      @Param("from") LocalDateTime from,
      @Param("to") LocalDateTime to);

  @Query("SELECT ct FROM CampaignTarget ct JOIN FETCH ct.user WHERE ct.campaign.id = :campaignId " +
      "AND ct.id > :cursor AND ct.sentAt >= :from AND ct.sentAt < :to ORDER BY ct.id ASC")
  List<CampaignTarget> findByCampaignIdWithCursorAndFilter(
      @Param("campaignId") Long campaignId,
      @Param("cursor") Long cursor,
      @Param("from") LocalDateTime from,
      @Param("to") LocalDateTime to,
      Pageable pageable);

  void deleteByCampaignId(Long campaignId);

  @Query("SELECT ct.campaign.ad FROM CampaignTarget ct " +
      "WHERE ct.user.id = :userId " +
      "AND ct.campaign.status = 'IN_PROGRESS' " +
      "AND ct.campaign.ad IS NOT NULL " +
      "ORDER BY ct.campaign.startedAt DESC")
  List<com.web.ecommerce.domain.ad.entity.AD> findTargetedAdsByUserId(
      @Param("userId") Long userId, Pageable pageable);

  @Query("SELECT ct FROM CampaignTarget ct JOIN FETCH ct.user WHERE ct.campaign.id = :campaignId AND ct.user.id IN :userIds")
  List<CampaignTarget> findByCampaignIdAndUserIdInWithUser(@Param("campaignId") Long campaignId, @Param("userIds") List<Long> userIds);
}
