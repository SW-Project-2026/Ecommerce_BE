package com.web.ecommerce.domain.product.dto.request;

import com.web.ecommerce.domain.campaign.enums.BatchCycle;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class SetScheduleRequest {

    @NotNull(message = "수집 주기를 선택해주세요. (DAILY, WEEKLY, MONTHLY)")
    @Schema(description = "수집 주기", example = "DAILY", allowableValues = {"DAILY", "WEEKLY", "MONTHLY"})
    private BatchCycle cycle;

    @NotBlank(message = "수집 시간을 입력해주세요.")
    @Pattern(regexp = "^([01]\\d|2[0-3]):[0-5]\\d$", message = "시간 형식은 HH:mm 이어야 합니다. (예: 03:00)")
    @Schema(description = "수집 시각 (HH:mm)", example = "03:00")
    private String time;
}
