package com.web.ecommerce.domain.event.dto.request;

import com.web.ecommerce.domain.event.enums.FieldType;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Schema(title = "Event 수정 요청 DTO")
public class UpdateEventRequest {

  @Schema(description = "이벤트 키 (영문, 고유값)")
  private String eventKey;

  @Schema(description = "이벤트 이름")
  private String eventName;

  @Schema(description = "이벤트 설명")
  private String description;

  @Schema(description = "활성화 여부")
  private Boolean isActive;

  @Schema(description = "이벤트 필드 목록")
  private List<UpdateEventFieldRequest> fields;

  @Getter
  @AllArgsConstructor
  @NoArgsConstructor
  public static class UpdateEventFieldRequest {

    @Schema(description = "필드 ID (null이면 신규 추가)")
    private Long fieldId;

    @Schema(description = "필드 이름")
    private String fieldName;

    @Schema(description = "필드 타입 (STRING/NUMBER/DATETIME)")
    private FieldType fieldType;

    @Schema(description = "필수 여부")
    private Boolean isRequired;

    @Schema(description = "필드 설명")
    private String description;
  }
}
