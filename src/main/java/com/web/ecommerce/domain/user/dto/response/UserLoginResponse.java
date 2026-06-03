package com.web.ecommerce.domain.user.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class UserLoginResponse {

  @Schema(description = "권한(사용자/관리자)")
  private String role;

}
