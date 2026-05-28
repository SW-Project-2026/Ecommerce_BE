package com.web.ecommerce.domain.campaign.dto.response;

import com.web.ecommerce.domain.campaign.enums.SendStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Schema(title = "SMS 발송 대상자 상태 DTO")
public class SmsTargetResponse {

    @Schema(description = "로그인 ID")
    private String loginId;

    @Schema(description = "발송 상태 (SENT / FAILED)")
    private SendStatus status;

    @Schema(description = "발송 시각")
    private LocalDateTime sentAt;
}
