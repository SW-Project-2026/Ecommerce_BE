package com.web.ecommerce.domain.user.dto.response;

public record UserAdminResponse(Long id, String name, String loginId, String email, String phone, String role, Integer isActive) {}
