package com.web.ecommerce.domain.campaign.enums;

public enum DuplicatePolicy {
    CHECK,  // couponRestrictionDays 내 발송 이력 있으면 스킵
    IGNORE  // 이력 무시하고 무조건 발송
}