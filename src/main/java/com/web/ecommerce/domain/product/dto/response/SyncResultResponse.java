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

    @Schema(description = "수집 완료 시각")
    private LocalDateTime syncedAt;

    public static SyncResultResponse of(int savedCount) {
        return SyncResultResponse.builder()
                .savedCount(savedCount)
                .syncedAt(LocalDateTime.now())
                .build();
    }
}
