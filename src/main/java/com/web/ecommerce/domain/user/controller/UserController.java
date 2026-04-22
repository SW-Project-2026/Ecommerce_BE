package com.web.ecommerce.domain.user.controller;

import com.web.ecommerce.domain.user.dto.request.UserLoginRequest;
import com.web.ecommerce.domain.user.dto.request.UserPasswordUpdateRequest;
import com.web.ecommerce.domain.user.dto.request.UserSignupRequest;
import com.web.ecommerce.domain.user.dto.request.UserUpdateRequest;
import com.web.ecommerce.domain.user.dto.response.UserAdminResponse;
import com.web.ecommerce.domain.user.dto.response.UserLoginResponse;
import com.web.ecommerce.domain.user.dto.response.UserProfileResponse;
import com.web.ecommerce.global.page.response.PageResponse;
import com.web.ecommerce.global.response.BaseResponse;
import com.web.ecommerce.global.security.CustomUserDetails;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

@Tag(name = "User", description = "사용자 API")
public interface UserController {

    /**
     * 회원가입 후 자동 로그인
     * @param request 회원가입 요청 정보
     * @param response HttpOnly 쿠키에 리프레시 토큰 저장
     * @return 액세스 토큰 및 사용자 정보
     */
    @Operation(summary = "사용자 회원가입", description = "회원가입 후 자동 로그인하는 API")
    ResponseEntity<BaseResponse<UserLoginResponse>> signup(
            @Valid @RequestBody UserSignupRequest request,
            HttpServletResponse response
    );

    /**
     * 아이디/비밀번호로 로그인
     * @param request 로그인 아이디, 비밀번호
     * @param response HttpOnly 쿠키에 리프레시 토큰 저장
     * @return 액세스 토큰 및 사용자 정보
     */
    @Operation(summary = "로그인", description = "아이디/비밀번호로 로그인하는 API")
    ResponseEntity<BaseResponse<UserLoginResponse>> login(
            @Valid @RequestBody UserLoginRequest request,
            HttpServletResponse response
    );

    /**
     * 관리자 계정 생성 (X-Admin-Secret 헤더 검증)
     * @param secretKey 관리자 시크릿 키
     * @param request 회원가입 요청 정보
     * @param response HttpOnly 쿠키에 리프레시 토큰 저장
     * @return 액세스 토큰 및 관리자 정보
     */
    @Operation(summary = "관리자 회원가입", description = "관리자 회원가입 API")
    ResponseEntity<BaseResponse<UserLoginResponse>> adminSignup(
            @RequestHeader("x-Admin-Secret") String secretKey,
            @Valid @RequestBody UserSignupRequest request,
            HttpServletResponse response
    );

    /**
     * 로그인한 사용자 본인 정보 조회
     * @param userDetails 로그인한 사용자 정보
     * @return 사용자 프로필 정보
     */
    @Operation(summary = "내 정보 조회")
    ResponseEntity<BaseResponse<UserProfileResponse>> getMyProfile(
            @AuthenticationPrincipal CustomUserDetails userDetails
    );

    /**
     * 이름, 전화번호 수정
     * @param userDetails 로그인한 사용자 정보
     * @param request 수정할 이름, 전화번호
     */
    @Operation(summary = "내 정보 수정", description = "이름, 전화번호 수정")
    ResponseEntity<BaseResponse<Void>> updateProfile(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @Valid @RequestBody UserUpdateRequest request
    );

    /**
     * 현재 비밀번호 확인 후 새 비밀번호로 변경
     * @param userDetails 로그인한 사용자 정보
     * @param request 현재 비밀번호, 새 비밀번호, 새 비밀번호 확인
     */
    @Operation(summary = "비밀번호 변경")
    ResponseEntity<BaseResponse<Void>> updatePassword(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @Valid @RequestBody UserPasswordUpdateRequest request
    );

    /**
     * 회원 탈퇴 (Soft Delete, is_active = 0)
     * @param userDetails 로그인한 사용자 정보
     */
    @Operation(summary = "회원 탈퇴")
    ResponseEntity<BaseResponse<Void>> withdraw(
            @AuthenticationPrincipal CustomUserDetails userDetails
    );

    /**
     * 전체 회원 목록 조회 (관리자 전용, 페이징)
     * @param pageable 페이징 정보 (기본 20건)
     * @return 회원 목록
     */
    @Operation(summary = "전체 회원 목록 조회", description = "관리자 전용")
    ResponseEntity<BaseResponse<PageResponse<UserAdminResponse>>> getUserList(Pageable pageable);

    /**
     * 특정 회원 상세 조회 (관리자 전용)
     * @param userId 조회할 사용자 ID
     * @return 회원 상세 정보
     */
    @Operation(summary = "특정 회원 조회", description = "관리자 전용")
    ResponseEntity<BaseResponse<UserAdminResponse>> getUserDetail(Long userId);
}
