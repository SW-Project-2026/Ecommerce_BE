package com.web.ecommerce.domain.user.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class UserPasswordUpdateRequest {

  @NotBlank
  private String currentPassword;

  @NotBlank
  private String newPassword;

  @NotBlank
  private String newPasswordConfirm;
}
