package com.web.ecommerce.domain.event.dto.request;

import com.web.ecommerce.domain.event.enums.FieldType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Schema(title = "EventField 수정 요청 DTO")
public class UpdateEventFieldRequest {

  @Schema(description = "필드 이름")
  private String fieldName;

  @Schema(description = "필드 타입 (STRING/NUMBER/DATETIME)")
  private FieldType fieldType;

  @Schema(description = "필수 여부")
  private Boolean isRequired;

  @Schema(description = "필드 설명")
  private String description;
}
