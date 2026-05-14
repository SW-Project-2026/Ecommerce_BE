package com.web.ecommerce.domain.product.service;

import com.web.ecommerce.domain.campaign.enums.BatchCycle;
import com.web.ecommerce.domain.product.dto.request.SetScheduleRequest;
import com.web.ecommerce.domain.product.dto.response.SyncScheduleResponse;
import com.web.ecommerce.domain.product.entity.ProductSyncSchedule;
import com.web.ecommerce.domain.product.exception.ProductErrorCode;
import com.web.ecommerce.domain.product.repository.ProductRepository;
import com.web.ecommerce.domain.product.repository.ProductSyncScheduleRepository;
import com.web.ecommerce.global.exception.CustomException;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.support.CronExpression;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.atomic.AtomicBoolean;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductSyncScheduleService {

    private final TaskScheduler taskScheduler;
    private final ProductSyncScheduleRepository scheduleRepository;
    private final ProductSyncService productSyncService;
    private final ProductRepository productRepository;

    private ScheduledFuture<?> scheduledFuture;
    private final AtomicBoolean syncRunning = new AtomicBoolean(false);

    @PostConstruct
    public void restoreSchedule() {
        scheduleRepository.findByActiveTrue().ifPresent(schedule -> {
            log.info("저장된 스케줄 복원: cycle={}, time={}", schedule.getCycle(), schedule.getScheduledTime());
            registerTask(schedule);
        });
    }

    @Transactional
    public SyncScheduleResponse setSchedule(SetScheduleRequest request) {
        scheduleRepository.findByActiveTrue().ifPresent(existing -> {
            existing.deactivate();
            scheduleRepository.save(existing);
        });

        cancelTask();

        String cron = buildCron(request.getCycle(), request.getTime());
        LocalDateTime nextRunAt = computeNextRunAt(cron);

        ProductSyncSchedule schedule = ProductSyncSchedule.builder()
                .cycle(request.getCycle())
                .scheduledTime(request.getTime())
                .active(true)
                .nextRunAt(nextRunAt)
                .build();

        schedule = scheduleRepository.save(schedule);
        registerTask(schedule);

        long totalProductCount = productRepository.count();
        return SyncScheduleResponse.of(schedule, totalProductCount, "IDLE");
    }

    @Transactional
    public void cancelSchedule() {
        ProductSyncSchedule schedule = scheduleRepository.findByActiveTrue()
                .orElseThrow(() -> new CustomException(ProductErrorCode.SCHEDULE_NOT_FOUND));

        schedule.deactivate();
        scheduleRepository.save(schedule);
        cancelTask();

        log.info("자동 수집 스케줄 취소됨");
    }

    @Transactional(readOnly = true)
    public SyncScheduleResponse getSchedule() {
        ProductSyncSchedule schedule = scheduleRepository.findByActiveTrue()
                .orElseThrow(() -> new CustomException(ProductErrorCode.SCHEDULE_NOT_FOUND));
        long totalProductCount = productRepository.count();
        String status = syncRunning.get() ? "RUNNING" : "IDLE";
        return SyncScheduleResponse.of(schedule, totalProductCount, status);
    }

    private void registerTask(ProductSyncSchedule schedule) {
        String cron = buildCron(schedule.getCycle(), schedule.getScheduledTime());
        Long scheduleId = schedule.getId();

        scheduledFuture = taskScheduler.schedule(
                () -> executeSync(scheduleId, cron),
                new CronTrigger(cron)
        );

        log.info("스케줄 등록 완료: cron={}, nextRunAt={}", cron, schedule.getNextRunAt());
    }

    private void executeSync(Long scheduleId, String cron) {
        syncRunning.set(true);
        log.info("자동 상품 수집 시작 (scheduleId={})", scheduleId);
        try {
            int saved = productSyncService.sync();
            log.info("자동 상품 수집 완료: 신규 {}개 저장", saved);
        } finally {
            syncRunning.set(false);
        }

        scheduleRepository.findById(scheduleId).ifPresent(s -> {
            LocalDateTime nextRunAt = computeNextRunAt(cron);
            s.updateLastRun(LocalDateTime.now(), nextRunAt);
            scheduleRepository.save(s);
        });
    }

    private void cancelTask() {
        if (scheduledFuture != null && !scheduledFuture.isCancelled()) {
            scheduledFuture.cancel(false);
            scheduledFuture = null;
        }
    }

    private String buildCron(BatchCycle cycle, String time) {
        String[] parts = time.split(":");
        String hour = parts[0];
        String minute = parts[1];
        return switch (cycle) {
            case DAILY -> String.format("0 %s %s * * *", minute, hour);
            case WEEKLY -> String.format("0 %s %s * * MON", minute, hour);
            case MONTHLY -> String.format("0 %s %s 1 * *", minute, hour);
        };
    }

    private LocalDateTime computeNextRunAt(String cron) {
        ZonedDateTime next = CronExpression.parse(cron).next(ZonedDateTime.now(ZoneId.systemDefault()));
        return next != null ? next.toLocalDateTime() : null;
    }
}
