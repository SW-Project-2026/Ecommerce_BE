package com.web.ecommerce.domain.user.mapper;

import com.web.ecommerce.domain.user.dto.request.UserSignupRequest;
import com.web.ecommerce.domain.user.dto.response.UserLoginResponse;
import com.web.ecommerce.domain.user.entity.Role;
import com.web.ecommerce.domain.user.entity.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    // SignupRequest → User 엔티티
    public User toEntity(UserSignupRequest request, String encodedPassword) {
        return User.builder()
                .name(request.getName())
                .loginId(request.getLoginId())
                .password(encodedPassword)
                .email(request.getEmail())
                .phone(request.getPhone())
                .role(Role.USER)
                .isActive(1)
                .build();
    }

    // User + AccessToken → LoginResponse
    public UserLoginResponse toLoginResponse(User user, String accessToken) {
        return UserLoginResponse.builder()
                .accessToken(accessToken)
                .role(user.getRole().name())
                .build();
    }
}
