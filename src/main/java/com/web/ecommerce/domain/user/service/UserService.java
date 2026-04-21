package com.web.ecommerce.domain.user.service;

import com.web.ecommerce.domain.user.dto.request.UserPasswordUpdateRequest;
import com.web.ecommerce.domain.user.dto.request.UserSignupRequest;
import com.web.ecommerce.domain.user.dto.request.UserUpdateRequest;
import com.web.ecommerce.domain.user.dto.response.AuthResult;
import com.web.ecommerce.domain.user.dto.response.UserAdminResponse;
import com.web.ecommerce.domain.user.dto.response.UserProfileResponse;
import com.web.ecommerce.domain.user.entity.Role;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UserService {

    /**
     * 회원가입 후 자동 로그인 처리
     * @param request 회원가입 요청 정보 (아이디, 비밀번호, 이름 등)
     * @param role 부여할 권한 (USER 또는 ADMIN)
     * @return 액세스 토큰 및 리프레시 토큰 포함 인증 결과
     */
    AuthResult signup(UserSignupRequest request, Role role);

    /**
     * 아이디/비밀번호로 로그인
     * @param loginId 로그인 아이디
     * @param password 비밀번호
     * @return 액세스 토큰 및 리프레시 토큰 포함 인증 결과
     */
    AuthResult loginById(String loginId, String password);

    /**
     * 내 정보 조회
     * @param userId 로그인한 사용자 ID
     * @return 사용자 프로필 정보
     */
    UserProfileResponse getMyProfile(Long userId);

    /**
     * 내 정보 수정 (이름, 전화번호)
     * @param userId 로그인한 사용자 ID
     * @param request 수정할 이름, 전화번호
     */
    void updateProfile(Long userId, UserUpdateRequest request);

    /**
     * 비밀번호 변경
     * @param userId 로그인한 사용자 ID
     * @param request 현재 비밀번호, 새 비밀번호, 새 비밀번호 확인
     */
    void updatePassword(Long userId, UserPasswordUpdateRequest request);

    /**
     * 회원 탈퇴 (soft delete)
     * @param userId 로그인한 사용자 ID
     */
    void withdraw(Long userId);

    /**
     * 전체 회원 목록 조회 (관리자 전용)
     * @param pageable 페이징 정보
     * @return 회원 목록 (페이징)
     */
    Page<UserAdminResponse> getUserList(Pageable pageable);

    /**
     * 특정 회원 상세 조회 (관리자 전용)
     * @param userId 조회할 사용자 ID
     * @return 회원 상세 정보
     */
    UserAdminResponse getUserDetail(Long userId);
}
