package com.web.ecommerce.domain.user.service;

import com.web.ecommerce.domain.user.dto.request.UserPasswordUpdateRequest;
import com.web.ecommerce.domain.user.dto.request.UserSignupRequest;
import com.web.ecommerce.domain.user.dto.request.UserUpdateRequest;
import com.web.ecommerce.domain.user.dto.response.AuthResult;
import com.web.ecommerce.domain.user.dto.response.UserAdminResponse;
import com.web.ecommerce.domain.user.dto.response.UserLoginResponse;
import com.web.ecommerce.domain.user.dto.response.UserProfileResponse;
import com.web.ecommerce.domain.user.entity.Role;
import com.web.ecommerce.domain.user.entity.User;
import com.web.ecommerce.domain.user.exception.UserErrorCode;
import com.web.ecommerce.domain.user.mapper.UserMapper;
import com.web.ecommerce.domain.user.repository.UserRepository;
import com.web.ecommerce.global.exception.CustomException;
import com.web.ecommerce.global.security.JwtProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtProvider jwtProvider;
    private final UserMapper userMapper;

    @Override
    @Transactional
    public AuthResult signup(UserSignupRequest request, Role role) {
        if (!request.getPassword().equals(request.getPasswordConfirm())) {
            throw new CustomException(UserErrorCode.PASSWORD_CONFIRM_MISMATCH);
        }
        if (userRepository.existsByLoginId(request.getLoginId())) {
            throw new CustomException(UserErrorCode.USER_ALREADY_EXISTS);
        }
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new CustomException(UserErrorCode.EMAIL_ALREADY_EXISTS);
        }

        User user = userMapper.toEntity(request, role, passwordEncoder.encode(request.getPassword()));
        User saved = userRepository.save(user);
        return toAuthResult(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public AuthResult loginById(String loginId, String password) {
        User user = userRepository.findByLoginIdAndIsActive(loginId, 1)
                .orElseThrow(() -> new CustomException(UserErrorCode.USER_NOT_FOUND));

        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new CustomException(UserErrorCode.INVALID_PASSWORD);
        }

        return toAuthResult(user);
    }

    @Override
    @Transactional(readOnly = true)
    public UserProfileResponse getMyProfile(Long userId) {
        User user = userRepository.findByIdAndIsActive(userId, 1)
                .orElseThrow(() -> new CustomException(UserErrorCode.USER_NOT_FOUND));
        return userMapper.toProfileResponse(user);
    }

    @Override
    @Transactional
    public void updateProfile(Long userId, UserUpdateRequest request) {
        User user = userRepository.findByIdAndIsActive(userId, 1)
                .orElseThrow(() -> new CustomException(UserErrorCode.USER_NOT_FOUND));
        user.updateProfile(request.getName(), request.getPhone());
    }

    @Override
    @Transactional
    public void updatePassword(Long userId, UserPasswordUpdateRequest request) {
        User user = userRepository.findByIdAndIsActive(userId, 1)
                .orElseThrow(() -> new CustomException(UserErrorCode.USER_NOT_FOUND));
        if (!passwordEncoder.matches(request.getCurrentPassword(), user.getPassword())) {
            throw new CustomException(UserErrorCode.INVALID_PASSWORD);
        }
        if (!request.getNewPassword().equals(request.getNewPasswordConfirm())) {
            throw new CustomException(UserErrorCode.PASSWORD_CONFIRM_MISMATCH);
        }
        user.updatePassword(passwordEncoder.encode(request.getNewPassword()));
    }

    @Override
    @Transactional
    public void withdraw(Long userId) {
        User user = userRepository.findByIdAndIsActive(userId, 1)
                .orElseThrow(() -> new CustomException(UserErrorCode.USER_NOT_FOUND));
        user.withdraw();
    }

    @Override
    @Transactional(readOnly = true)
    public Page<UserAdminResponse> getUserList(Pageable pageable) {
        return userRepository.findAllByRole(Role.USER, pageable)
                .map(userMapper::toAdminResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public UserAdminResponse getUserDetail(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(UserErrorCode.USER_NOT_FOUND));
        return userMapper.toAdminResponse(user);
    }

    private AuthResult toAuthResult(User user) {
        String accessToken = jwtProvider.generateAccessToken(user.getId(), user.getRole().name());
        String refreshToken = jwtProvider.generateRefreshToken(user.getId());
        UserLoginResponse response = userMapper.toLoginResponse(user, accessToken);
        return new AuthResult(response, refreshToken);
    }
}
