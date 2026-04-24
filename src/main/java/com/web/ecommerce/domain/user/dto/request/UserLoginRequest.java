package com.web.ecommerce.domain.user.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class UserLoginRequest {
  @Schema(description = "사용자 ID", example = "user123")
  @NotBlank(message = "아이디는 필수 항목입니다.")
  @Size(max = 20, message = "아이디는 20자 이하여야 합니다.")
  private String loginId;

  @Schema(description = "비밀번호", example = "abcd1234")
  @NotBlank(message = "비밀번호는 필수 항목입니다.")
  @Size(min = 8, max = 20, message = "비밀번호는 8자 이상 20자 이하이어야 합니다.")
  private String password;

}
