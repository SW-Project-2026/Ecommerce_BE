package com.web.ecommerce.domain.user.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class UserUpdateRequest {

  @NotBlank
  private String name;

  private String phone;
}
