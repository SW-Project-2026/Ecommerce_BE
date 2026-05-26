package com.web.ecommerce.domain.campaign.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Schema(title = "문자 발송 결과 DTO")
public class SmsSendResponse {

    @Schema(description = "발송 대상 총 인원")
    private int totalCount;

    @Schema(description = "발송 성공 인원")
    private int successCount;

    @Schema(description = "발송 실패 인원 (전화번호 없음 포함)")
    private int failCount;
}
