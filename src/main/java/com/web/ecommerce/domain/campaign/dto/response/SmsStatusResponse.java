package com.web.ecommerce.domain.campaign.dto.response;

import com.web.ecommerce.global.response.CursorResponse;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Schema(title = "SMS 발송 현황 DTO")
public class SmsStatusResponse {

    @Schema(description = "발송 성공 인원")
    private long sentCount;

    @Schema(description = "발송 실패 인원")
    private long failedCount;

    @Schema(description = "대상자 목록 (커서 기반)")
    private CursorResponse<SmsTargetResponse> targets;
}
