package com.web.ecommerce.domain.product.dto.response;

import com.web.ecommerce.domain.campaign.enums.BatchCycle;
import com.web.ecommerce.domain.product.entity.ProductSyncSchedule;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class SyncScheduleResponse {

    @Schema(description = "스케줄 ID")
    private Long id;

    @Schema(description = "수집 주기", example = "DAILY")
    private BatchCycle cycle;

    @Schema(description = "수집 시각 (HH:mm)", example = "03:00")
    private String scheduledTime;

    @Schema(description = "활성화 여부")
    private boolean active;

    @Schema(description = "마지막 수집 시각")
    private LocalDateTime lastSyncedAt;

    @Schema(description = "다음 실행 예정 시각")
    private LocalDateTime nextRunAt;

    @Schema(description = "DB에 저장된 총 상품 수")
    private long totalProductCount;

    @Schema(description = "수집 상태", example = "IDLE", allowableValues = {"IDLE", "RUNNING"})
    private String syncStatus;

    public static SyncScheduleResponse of(ProductSyncSchedule schedule, long totalProductCount, String syncStatus) {
        return SyncScheduleResponse.builder()
                .id(schedule.getId())
                .cycle(schedule.getCycle())
                .scheduledTime(schedule.getScheduledTime())
                .active(schedule.isActive())
                .lastSyncedAt(schedule.getLastRunAt())
                .nextRunAt(schedule.getNextRunAt())
                .totalProductCount(totalProductCount)
                .syncStatus(syncStatus)
                .build();
    }
}
