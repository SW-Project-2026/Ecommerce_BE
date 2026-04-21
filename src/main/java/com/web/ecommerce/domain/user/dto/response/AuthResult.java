package com.web.ecommerce.domain.user.dto.response;

public record AuthResult(UserLoginResponse loginResponse, String refreshToken) {}
