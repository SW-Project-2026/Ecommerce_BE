package com.web.ecommerce.domain.user.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UserSignupRequest {

  @Schema(description = "사용자 이름", example = "홍길동")
  @NotBlank(message = "이름은 필수 항목입니다.")
  @Size(max = 20, message = "이름은 20자 이하여야 합니다.")
  private String name;

  @Schema(description = "사용자 ID", example = "user123")
  @NotBlank(message = "아이디는 필수 항목입니다.")
  @Size(max = 20, message = "아이디는 20자 이하여야 합니다.")
  private String loginId;

  @Schema(description = "비밀번호", example = "abcd1234")
  @NotBlank(message = "비밀번호는 필수 항목입니다.")
  @Size(min = 8, max = 20, message = "비밀번호는 8자 이상 20자 이하이어야 합니다.")
  private String password;

  @Schema(description = "비밀번호 확인")
  @NotBlank(message = "비밀번호 확인은 필수입니다.")
  private String passwordConfirm;

  @Schema(description = "이메일", example = "user1234@gmail.com")
  @NotBlank(message = "이메일은 필수입니다.")
  @Email(message = "올바른 이메일 형식이어야 합니다.")
  private String email;

  @Schema(description = "전화번호", example = "010-1234-5678")
  @NotBlank(message = "전화번호는 필수입니다.")
  @Pattern(
      regexp = "^010\\d{8}$",
      message = "전화번호는 하이픈 없이 숫자 11자리(01012345678) 형식이어야 합니다."
  )
  private String phone;

  @Schema(description ="관리자/회원 구분", example = "ADMIN/USER")
  private String role;

  @Schema(description = "마케팅 동의 여부", example = "true")
  private boolean marketingAgreed;

}
