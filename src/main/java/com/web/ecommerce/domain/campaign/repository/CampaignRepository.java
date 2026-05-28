package com.web.ecommerce.domain.campaign.repository;

import com.web.ecommerce.domain.campaign.entity.Campaign;
import com.web.ecommerce.domain.campaign.enums.CampaignGoalType;
import com.web.ecommerce.domain.campaign.enums.CollectionType;
import com.web.ecommerce.domain.campaign.enums.CustomerSegment;
import com.web.ecommerce.domain.campaign.enums.Status;
import com.web.ecommerce.domain.coupon.enums.IssuanceMethod;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CampaignRepository extends JpaRepository<Campaign, Long> {

  @Query("SELECT c FROM Campaign c WHERE " +
      "(:status IS NULL OR c.status = :status) AND " +
      "(:campaignGoalType IS NULL OR c.campaignGoalType = :campaignGoalType) AND " +
      "(:customerSegment IS NULL OR c.customerSegment = :customerSegment) AND " +
      "(:collectionType IS NULL OR c.collectionType = :collectionType)")
  Page<Campaign> findByFilters(
      @Param("status") Status status,
      @Param("campaignGoalType") CampaignGoalType campaignGoalType,
      @Param("customerSegment") CustomerSegment customerSegment,
      @Param("collectionType") CollectionType collectionType,
      Pageable pageable);

  @Query("SELECT c FROM Campaign c WHERE c.issueType = :issueType AND c.status = 'IN_PROGRESS' " +
      "AND c.startedAt <= :now AND c.endedAt >= :now AND c.coupon IS NOT NULL")
  List<Campaign> findActiveDownloadCampaigns(
      @Param("issueType") IssuanceMethod issueType,
      @Param("now") LocalDateTime now);

  @Query("SELECT c FROM Campaign c WHERE c.collectionType = 'BATCH' AND c.status = 'IN_PROGRESS' " +
      "AND c.startedAt <= :now AND c.endedAt >= :now AND c.messageContent IS NOT NULL")
  List<Campaign> findSchedulableBatchCampaigns(@Param("now") LocalDateTime now);
}
