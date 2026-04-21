package com.web.ecommerce.domain.user.dto.response;

public record UserProfileResponse(Long id, String name, String loginId, String email, String phone) {}
