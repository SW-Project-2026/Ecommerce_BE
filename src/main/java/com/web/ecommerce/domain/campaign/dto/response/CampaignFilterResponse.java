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
@Schema(title = "CampaignFilter 응답 DTO")
public class CampaignFilterResponse {

  private Long filterId;
  private String eventName;
  private String fieldName;
  private String fieldType;
  private String operator;
  private String value;
  private Integer periodDays;
}
