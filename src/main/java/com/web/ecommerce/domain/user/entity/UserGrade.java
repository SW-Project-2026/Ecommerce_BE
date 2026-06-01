package com.web.ecommerce.domain.user.entity;

public enum UserGrade {
    NEW,        // 가입 30일 이내
    GENERAL,    // 가입 30일 초과 & 누적 결제금액 30만원 미만
    VIP,        // 누적 결제금액 30만원 이상
    DORMANT     // 마지막 로그인 후 6개월 이상 미접속
}
