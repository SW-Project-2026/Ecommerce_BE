package com.web.ecommerce.domain.product.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class SyncResultResponse {

    @Schema(description = "신규 저장된 상품 수")
    private int savedCount;

    @Schema(description = "마지막 수집 시각")
    private LocalDateTime lastSyncedAt;

    @Schema(description = "수집 상태", example = "정상")
    private String syncStatus;

    public static SyncResultResponse of(int savedCount) {
        return SyncResultResponse.builder()
                .savedCount(savedCount)
                .lastSyncedAt(LocalDateTime.now())
                .syncStatus("정상")
                .build();
    }

    public static SyncResultResponse ofStatus(long totalCount, LocalDateTime lastSyncedAt) {
        return SyncResultResponse.builder()
                .savedCount((int) totalCount)
                .lastSyncedAt(lastSyncedAt)
                .syncStatus(lastSyncedAt != null ? "정상" : "미수집")
                .build();
    }
}
