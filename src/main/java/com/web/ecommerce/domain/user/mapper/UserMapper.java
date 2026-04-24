package com.web.ecommerce.domain.user.mapper;

import com.web.ecommerce.domain.user.dto.request.UserSignupRequest;
import com.web.ecommerce.domain.user.dto.response.UserAdminResponse;
import com.web.ecommerce.domain.user.dto.response.UserLoginResponse;
import com.web.ecommerce.domain.user.dto.response.UserProfileResponse;
import com.web.ecommerce.domain.user.entity.Role;
import com.web.ecommerce.domain.user.entity.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public User toEntity(UserSignupRequest request, Role role, String encodedPassword) {
        return User.builder()
                .name(request.getName())
                .loginId(request.getLoginId())
                .password(encodedPassword)
                .email(request.getEmail())
                .phone(request.getPhone())
                .role(role)
                .isActive(1)
                .marketingAgreed(request.isMarketingAgreed())
                .build();
    }

    public UserLoginResponse toLoginResponse(User user, String accessToken) {
        return UserLoginResponse.builder()
                .accessToken(accessToken)
                .role(user.getRole().name())
                .build();
    }

    public UserProfileResponse toProfileResponse(User user) {
        return new UserProfileResponse(user.getId(), user.getName(), user.getLoginId(), user.getEmail(), user.getPhone(), user.isMarketingAgreed());
    }

    public UserAdminResponse toAdminResponse(User user) {
        return new UserAdminResponse(user.getId(), user.getName(), user.getLoginId(), user.getEmail(), user.getPhone(), user.getRole().name(), user.getIsActive());
    }
}
