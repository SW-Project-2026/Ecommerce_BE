package com.web.ecommerce.domain.campaign.dto.reqeust;

import com.web.ecommerce.domain.campaign.enums.DuplicatePolicy;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Schema(title = "문자 발송 요청 DTO")
public class SendSmsRequest {

    @NotBlank
    @Pattern(regexp = "SMS|LMS", message = "messageType은 SMS 또는 LMS여야 합니다.")
    @Schema(description = "메시지 타입 (SMS / LMS)", example = "LMS")
    private String messageType;

    @Schema(description = "제목 (LMS 전용)", example = "특별 쿠폰 안내")
    private String subject;

    @NotBlank
    @Schema(description = "메시지 내용", example = "안녕하세요. 특별 쿠폰을 발급해 드립니다.")
    private String content;

    @NotNull
    @Schema(description = "중복 발송 정책 (CHECK: N일 내 발송 이력 있으면 스킵 / IGNORE: 이력 무시하고 발송)", example = "CHECK")
    private DuplicatePolicy duplicatePolicy;
}
