package com.web.ecommerce.domain.event.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Schema(title = "Event 응답 DTO")
public class EventResponse {

  private Long eventId;
  private String eventName;
  private String description;
  private Boolean isActive;
  private String createdAt;
  private List<EventFieldResponse> fields;

  @Builder
  @Getter
  @AllArgsConstructor
  @NoArgsConstructor
  public static class EventFieldResponse {
    private Long fieldId;
    private String fieldName;
    private String fieldType;
    private Boolean isRequired;
    private String description;
  }
}
