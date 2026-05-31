package com.web.ecommerce.domain.user.service;

import com.web.ecommerce.domain.order.enums.OrderStatus;
import com.web.ecommerce.domain.order.repository.OrderRepository;
import com.web.ecommerce.domain.user.entity.User;
import com.web.ecommerce.domain.user.entity.UserGrade;
import com.web.ecommerce.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserGradeBatchService {

    private static final int VIP_THRESHOLD = 300_000;   // 30만원
    private static final int NEW_DAYS = 30;             // 가입 30일 이내

    private final UserRepository userRepository;
    private final OrderRepository orderRepository;

    // 매일 새벽 2시 실행
    @Scheduled(cron = "0 0 2 * * *")
    @Transactional
    public void updateAllUserGrades() {
        log.info("[UserGrade 배치] 등급 갱신 시작");

        List<User> users = userRepository.findAll();
        int updated = 0;

        for (User user : users) {
            UserGrade newGrade = calculateGrade(user);
            if (user.getGrade() != newGrade) {
                user.updateGrade(newGrade);
                updated++;
            }
        }

        log.info("[UserGrade 배치] 완료 — 갱신된 유저 수: {}", updated);
    }

    private UserGrade calculateGrade(User user) {
        if (user.getCreatedAt() != null &&
                user.getCreatedAt().isAfter(LocalDateTime.now().minusDays(NEW_DAYS))) {
            return UserGrade.NEW;
        }

        int totalPaid = orderRepository.sumFinalAmountByUserIdExcludingStatus(
                user.getId(), OrderStatus.CANCELLED);

        return totalPaid >= VIP_THRESHOLD ? UserGrade.VIP : UserGrade.GENERAL;
    }
}
