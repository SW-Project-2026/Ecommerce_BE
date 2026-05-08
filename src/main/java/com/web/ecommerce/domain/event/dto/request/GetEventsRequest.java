package com.web.ecommerce.domain.event.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class GetEventsRequest {

  private Boolean isActive;
}
