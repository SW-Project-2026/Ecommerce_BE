package com.web.ecommerce.domain.user.controller;

import com.web.ecommerce.domain.user.dto.request.UserLoginRequest;
import com.web.ecommerce.domain.user.dto.request.UserPasswordUpdateRequest;
import com.web.ecommerce.domain.user.dto.request.UserSignupRequest;
import com.web.ecommerce.domain.user.dto.request.UserUpdateRequest;
import com.web.ecommerce.domain.user.dto.response.AuthResult;
import com.web.ecommerce.domain.user.dto.response.UserAdminResponse;
import com.web.ecommerce.domain.user.dto.response.UserLoginResponse;
import com.web.ecommerce.domain.user.dto.response.UserProfileResponse;
import com.web.ecommerce.domain.user.entity.Role;
import com.web.ecommerce.domain.user.service.UserService;
import com.web.ecommerce.global.response.BaseResponse;
import com.web.ecommerce.global.security.CustomUserDetails;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@Tag(name = "User", description = "사용자 API")
public class UserController {

  private final UserService userService;

  @Value("${admin.secret-key}")
  private String adminSecretKey;

  @Operation(summary = "사용자 회원가입", description = "회원가입 후 자동 로그인하는 API")
  @PostMapping("/signup")
  public ResponseEntity<BaseResponse<UserLoginResponse>> signup(
      @Valid @RequestBody UserSignupRequest request,
      HttpServletResponse response
  ) {
    AuthResult result = userService.signup(request, Role.USER);
    setRefreshTokenCookie(response, result.refreshToken());
    return ResponseEntity.status(HttpStatus.CREATED)
        .body(BaseResponse.success(201, "회원가입이 완료되었습니다.", result.loginResponse()));
  }

  @Operation(summary = "로그인", description = "아이디/비밀번호로 로그인하는 API")
  @PostMapping("/login")
  public ResponseEntity<BaseResponse<UserLoginResponse>> login(
      @Valid @RequestBody UserLoginRequest request,
      HttpServletResponse response
  ) {
    AuthResult result = userService.loginById(request.getLoginId(), request.getPassword());
    setRefreshTokenCookie(response, result.refreshToken());
    return ResponseEntity.ok(BaseResponse.success(result.loginResponse()));
  }

  @Operation(summary = "관리자 회원가입", description = "관리자 회원가입 API")
  @PostMapping("/admin/signup")
  public ResponseEntity<BaseResponse<UserLoginResponse>> adminSignup(
      @RequestHeader("x-Admin-Secret") String secretKey,
      @Valid @RequestBody UserSignupRequest request,
      HttpServletResponse response
  ) {
    if (!adminSecretKey.equals(secretKey)) {
      return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }
    AuthResult result = userService.signup(request, Role.ADMIN);
    setRefreshTokenCookie(response, result.refreshToken());
    return ResponseEntity.status(HttpStatus.CREATED)
        .body(BaseResponse.success(201, "관리자 계정이 생성되었습니다.", result.loginResponse()));
  }

  @Operation(summary = "내 정보 조회")
  @GetMapping("/me")
  public ResponseEntity<BaseResponse<UserProfileResponse>> getMyProfile(
      @AuthenticationPrincipal CustomUserDetails userDetails
  ) {
    return ResponseEntity.ok(BaseResponse.success(userService.getMyProfile(userDetails.getId())));
  }

  @Operation(summary = "내 정보 수정", description = "이름, 전화번호 수정")
  @PutMapping("/me")
  public ResponseEntity<BaseResponse<Void>> updateProfile(
      @AuthenticationPrincipal CustomUserDetails userDetails,
      @Valid @RequestBody UserUpdateRequest request
  ) {
    userService.updateProfile(userDetails.getId(), request);
    return ResponseEntity.ok(BaseResponse.success(200, "정보가 수정되었습니다.", null));
  }

  @Operation(summary = "비밀번호 변경")
  @PatchMapping("/me/password")
  public ResponseEntity<BaseResponse<Void>> updatePassword(
      @AuthenticationPrincipal CustomUserDetails userDetails,
      @Valid @RequestBody UserPasswordUpdateRequest request
  ) {
    userService.updatePassword(userDetails.getId(), request);
    return ResponseEntity.ok(BaseResponse.success(200, "비밀번호가 변경되었습니다.", null));
  }

  @Operation(summary = "회원 탈퇴")
  @DeleteMapping("/me")
  public ResponseEntity<BaseResponse<Void>> withdraw(
      @AuthenticationPrincipal CustomUserDetails userDetails
  ) {
    userService.withdraw(userDetails.getId());
    return ResponseEntity.ok(BaseResponse.success(200, "탈퇴가 완료되었습니다.", null));
  }

  @Operation(summary = "전체 회원 목록 조회", description = "관리자 전용")
  @GetMapping("/admin/list")
  public ResponseEntity<BaseResponse<Page<UserAdminResponse>>> getUserList(
      @PageableDefault(size = 20) Pageable pageable
  ) {
    return ResponseEntity.ok(BaseResponse.success(userService.getUserList(pageable)));
  }

  @Operation(summary = "특정 회원 조회", description = "관리자 전용")
  @GetMapping("/admin/{userId}")
  public ResponseEntity<BaseResponse<UserAdminResponse>> getUserDetail(
      @PathVariable Long userId
  ) {
    return ResponseEntity.ok(BaseResponse.success(userService.getUserDetail(userId)));
  }

  private void setRefreshTokenCookie(HttpServletResponse response, String refreshToken) {
    Cookie cookie = new Cookie("refreshToken", refreshToken);
    cookie.setHttpOnly(true);
    cookie.setSecure(true);
    cookie.setPath("/");
    cookie.setMaxAge(7 * 24 * 60 * 60);
    response.addCookie(cookie);
  }
}
