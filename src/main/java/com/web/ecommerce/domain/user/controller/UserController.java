package com.web.ecommerce.domain.user.controller;

import com.web.ecommerce.domain.user.dto.request.UserLoginRequest;
import com.web.ecommerce.domain.user.dto.request.UserPasswordUpdateRequest;
import com.web.ecommerce.domain.user.dto.request.UserSignupRequest;
import com.web.ecommerce.domain.user.dto.request.UserUpdateRequest;
import com.web.ecommerce.domain.user.dto.response.UserAdminResponse;
import com.web.ecommerce.domain.user.dto.response.UserLoginResponse;
import com.web.ecommerce.domain.user.dto.response.UserProfileResponse;
import com.web.ecommerce.global.response.BaseResponse;
import com.web.ecommerce.global.security.CustomUserDetails;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

@Tag(name = "User", description = "사용자 API")
public interface UserController {

    @Operation(summary = "사용자 회원가입", description = "회원가입 후 자동 로그인하는 API")
    ResponseEntity<BaseResponse<UserLoginResponse>> signup(
            @Valid @RequestBody UserSignupRequest request,
            HttpServletResponse response
    );

    @Operation(summary = "로그인", description = "아이디/비밀번호로 로그인하는 API")
    ResponseEntity<BaseResponse<UserLoginResponse>> login(
            @Valid @RequestBody UserLoginRequest request,
            HttpServletResponse response
    );

    @Operation(summary = "관리자 회원가입", description = "관리자 회원가입 API")
    ResponseEntity<BaseResponse<UserLoginResponse>> adminSignup(
            @RequestHeader("x-Admin-Secret") String secretKey,
            @Valid @RequestBody UserSignupRequest request,
            HttpServletResponse response
    );

    @Operation(summary = "내 정보 조회")
    ResponseEntity<BaseResponse<UserProfileResponse>> getMyProfile(
            @AuthenticationPrincipal CustomUserDetails userDetails
    );

    @Operation(summary = "내 정보 수정", description = "이름, 전화번호 수정")
    ResponseEntity<BaseResponse<Void>> updateProfile(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @Valid @RequestBody UserUpdateRequest request
    );

    @Operation(summary = "비밀번호 변경")
    ResponseEntity<BaseResponse<Void>> updatePassword(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @Valid @RequestBody UserPasswordUpdateRequest request
    );

    @Operation(summary = "회원 탈퇴")
    ResponseEntity<BaseResponse<Void>> withdraw(
            @AuthenticationPrincipal CustomUserDetails userDetails
    );

    @Operation(summary = "전체 회원 목록 조회", description = "관리자 전용")
    ResponseEntity<BaseResponse<Page<UserAdminResponse>>> getUserList(Pageable pageable);

    @Operation(summary = "특정 회원 조회", description = "관리자 전용")
    ResponseEntity<BaseResponse<UserAdminResponse>> getUserDetail(Long userId);
}
