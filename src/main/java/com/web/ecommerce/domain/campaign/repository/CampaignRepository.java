package com.web.ecommerce.domain.campaign.repository;

import com.web.ecommerce.domain.campaign.entity.Campaign;
import com.web.ecommerce.domain.campaign.enums.Status;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CampaignRepository extends JpaRepository<Campaign, Long> {

  List<Campaign> findByStatus(Status status);
}
