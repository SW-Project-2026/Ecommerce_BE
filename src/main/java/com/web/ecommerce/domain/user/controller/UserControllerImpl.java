package com.web.ecommerce.domain.user.controller;

import com.web.ecommerce.domain.user.dto.request.UserLoginRequest;
import com.web.ecommerce.domain.user.dto.request.UserPasswordUpdateRequest;
import com.web.ecommerce.domain.user.dto.request.UserSignupRequest;
import com.web.ecommerce.domain.user.dto.request.UserUpdateRequest;
import com.web.ecommerce.domain.user.dto.request.WithdrawRequest;
import com.web.ecommerce.domain.user.dto.response.AuthResult;
import com.web.ecommerce.domain.user.dto.response.UserAdminResponse;
import com.web.ecommerce.domain.user.dto.response.UserLoginResponse;
import com.web.ecommerce.domain.user.dto.response.UserProfileResponse;
import com.web.ecommerce.domain.user.entity.Role;
import com.web.ecommerce.domain.user.service.UserService;
import com.web.ecommerce.global.response.BaseResponse;
import com.web.ecommerce.global.security.UserPrincipal;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import com.web.ecommerce.global.page.response.PageResponse;
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
public class UserControllerImpl implements UserController {

    private final UserService userService;

    @Value("${admin.secret-key}")
    private String adminSecretKey;

    @Override
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

    @Override
    @PostMapping("/login")
    public ResponseEntity<BaseResponse<UserLoginResponse>> login(
            @Valid @RequestBody UserLoginRequest request,
            HttpServletResponse response
    ) {
        AuthResult result = userService.loginById(request.getLoginId(), request.getPassword());
        setRefreshTokenCookie(response, result.refreshToken());
        return ResponseEntity.ok(BaseResponse.success(result.loginResponse()));
    }

    @Override
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

    @Override
    @GetMapping("/me")
    public ResponseEntity<BaseResponse<UserProfileResponse>> getMyProfile(
            @AuthenticationPrincipal UserPrincipal userDetails
    ) {
        return ResponseEntity.ok(BaseResponse.success(userService.getMyProfile(userDetails.id())));
    }

    @Override
    @PutMapping("/me")
    public ResponseEntity<BaseResponse<Void>> updateProfile(
            @AuthenticationPrincipal UserPrincipal userDetails,
            @Valid @RequestBody UserUpdateRequest request
    ) {
        userService.updateProfile(userDetails.id(), request);
        return ResponseEntity.ok(BaseResponse.success(200, "정보가 수정되었습니다.", null));
    }

    @Override
    @PatchMapping("/me/password")
    public ResponseEntity<BaseResponse<Void>> updatePassword(
            @AuthenticationPrincipal UserPrincipal userDetails,
            @Valid @RequestBody UserPasswordUpdateRequest request
    ) {
        userService.updatePassword(userDetails.id(), request);
        return ResponseEntity.ok(BaseResponse.success(200, "비밀번호가 변경되었습니다.", null));
    }

    @Override
    @DeleteMapping("/me")
    public ResponseEntity<BaseResponse<Void>> withdraw(
            @AuthenticationPrincipal UserPrincipal userDetails,
            @Valid @RequestBody WithdrawRequest request
    ) {
        userService.withdraw(userDetails.id(), request.password());
        return ResponseEntity.ok(BaseResponse.success(200, "탈퇴가 완료되었습니다.", null));
    }

    @Override
    @GetMapping("/admin/list")
    public ResponseEntity<BaseResponse<PageResponse<UserAdminResponse>>> getUserList(
            @PageableDefault(size = 20) Pageable pageable
    ) {
        return ResponseEntity.ok(BaseResponse.success(userService.getUserList(pageable)));
    }

    @Override
    @GetMapping("/admin/{userId}")
    public ResponseEntity<BaseResponse<UserAdminResponse>> getUserDetail(
            @PathVariable Long userId
    ) {
        return ResponseEntity.ok(BaseResponse.success(userService.getUserDetail(userId)));
    }

    @Override
    @PostMapping("/refresh")
    public ResponseEntity<BaseResponse<UserLoginResponse>> refresh(HttpServletRequest request) {
        String refreshToken = null;
        if (request.getCookies() != null) {
            for (Cookie cookie : request.getCookies()) {
                if ("refreshToken".equals(cookie.getName())) {
                    refreshToken = cookie.getValue();
                    break;
                }
            }
        }
        if (refreshToken == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        return ResponseEntity.ok(BaseResponse.success(userService.refreshAccessToken(refreshToken)));
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
