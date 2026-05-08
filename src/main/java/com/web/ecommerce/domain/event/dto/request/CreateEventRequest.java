package com.web.ecommerce.domain.event.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Schema(title = "Event 생성 요청 DTO")
public class CreateEventRequest {

  @Schema(description = "이벤트 키 (영문, 고유값)")
  private String eventKey;

  @Schema(description = "이벤트 이름")
  private String eventName;

  @Schema(description = "이벤트 설명")
  private String description;

  @Schema(description = "활성화 여부")
  private Boolean isActive;
}
