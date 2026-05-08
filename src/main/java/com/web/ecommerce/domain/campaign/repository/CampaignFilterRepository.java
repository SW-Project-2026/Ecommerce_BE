package com.web.ecommerce.domain.campaign.repository;

import com.web.ecommerce.domain.campaign.entity.CampaignFilter;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CampaignFilterRepository extends JpaRepository<CampaignFilter, Long> {

  List<CampaignFilter> findByCampaignId(Long campaignId);

  void deleteByCampaignId(Long campaignId);
}
