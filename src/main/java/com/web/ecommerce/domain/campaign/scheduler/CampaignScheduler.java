package com.web.ecommerce.domain.campaign.scheduler;

import com.web.ecommerce.domain.campaign.entity.Campaign;
import com.web.ecommerce.domain.campaign.enums.BatchCycle;
import com.web.ecommerce.domain.campaign.repository.CampaignRepository;
import com.web.ecommerce.domain.campaign.service.CampaignService;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class CampaignScheduler {

    private final CampaignRepository campaignRepository;
    private final CampaignService campaignService;

    @Scheduled(cron = "0 * * * * *")
    public void executeBatchCampaigns() {
        LocalDateTime now = LocalDateTime.now();
        List<Campaign> candidates = campaignRepository.findSchedulableBatchCampaigns(now);

        for (Campaign campaign : candidates) {
            if (!matchesSchedule(campaign, now)) {
                continue;
            }
            try {
                log.info("배치 캠페인 발송 시작. campaignId={}", campaign.getId());
                campaignService.executeBatchSend(campaign.getId());
                log.info("배치 캠페인 발송 완료. campaignId={}", campaign.getId());
            } catch (Exception e) {
                log.error("배치 캠페인 발송 실패. campaignId={}", campaign.getId(), e);
            }
        }
    }

    private boolean matchesSchedule(Campaign campaign, LocalDateTime now) {
        if (campaign.getBatchCycle() == null || campaign.getBatchTime() == null) {
            return false;
        }

        boolean timeMatches = campaign.getBatchTime().getHour() == now.getHour()
            && campaign.getBatchTime().getMinute() == now.getMinute();

        if (!timeMatches) {
            return false;
        }

        return switch (campaign.getBatchCycle()) {
            case DAILY -> true;
            case WEEKLY -> campaign.getBatchDayOfWeek() != null
                && campaign.getBatchDayOfWeek() == now.getDayOfWeek();
            case MONTHLY -> campaign.getBatchDayOfMonth() != null
                && campaign.getBatchDayOfMonth() == now.getDayOfMonth();
        };
    }
}
