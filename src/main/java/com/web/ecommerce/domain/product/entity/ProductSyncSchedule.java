package com.web.ecommerce.domain.product.entity;

import com.web.ecommerce.domain.campaign.enums.BatchCycle;
import com.web.ecommerce.global.common.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "product_sync_schedules")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class ProductSyncSchedule extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private BatchCycle cycle;

    @Column(name = "scheduled_time", nullable = false, length = 5)
    private String scheduledTime;

    @Column(nullable = false)
    private boolean active;

    @Column(name = "last_run_at")
    private LocalDateTime lastRunAt;

    @Column(name = "next_run_at")
    private LocalDateTime nextRunAt;

    public void deactivate() {
        this.active = false;
    }

    public void updateLastRun(LocalDateTime lastRunAt, LocalDateTime nextRunAt) {
        this.lastRunAt = lastRunAt;
        this.nextRunAt = nextRunAt;
    }

    public void updateNextRunAt(LocalDateTime nextRunAt) {
        this.nextRunAt = nextRunAt;
    }
}
