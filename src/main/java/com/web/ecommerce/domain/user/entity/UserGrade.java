package com.web.ecommerce.domain.user.entity;

public enum UserGrade {
    NEW,        // 가입 30일 이내
    GENERAL,    // 가입 30일 초과 & 누적 결제금액 30만원 미만
    VIP         // 누적 결제금액 30만원 이상
}
