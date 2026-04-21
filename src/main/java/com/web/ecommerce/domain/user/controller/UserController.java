package com.web.ecommerce.domain.user.controller;

import com.web.ecommerce.domain.user.dto.request.UserLoginRequest;
import com.web.ecommerce.domain.user.dto.request.UserSignupRequest;
import com.web.ecommerce.domain.user.dto.response.AuthResult;
import com.web.ecommerce.domain.user.dto.response.UserLoginResponse;
import com.web.ecommerce.domain.user.service.UserService;
import com.web.ecommerce.global.response.BaseResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@Tag(name = "User", description = "사용자 API")
public class UserController {

  private final UserService userService;

  @Operation(summary = "회원가입", description = "회원가입 후 자동 로그인하는 API")
  @PostMapping("/signup")
  public ResponseEntity<BaseResponse<UserLoginResponse>> signup(
      @Valid @RequestBody UserSignupRequest request,
      HttpServletResponse response
  ) {
    AuthResult result = userService.signup(request);
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

  // RefreshToken을 HttpOnly 쿠키에 저장
  private void setRefreshTokenCookie(HttpServletResponse response, String refreshToken) {
    Cookie cookie = new Cookie("refreshToken", refreshToken);
    cookie.setHttpOnly(true);
    cookie.setSecure(true);
    cookie.setPath("/");
    cookie.setMaxAge(7 * 24 * 60 * 60); // 7일
    response.addCookie(cookie);
  }

}
