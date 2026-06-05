package com.web.ecommerce.domain.dashboard.repository;

import com.web.ecommerce.domain.dashboard.dto.AdminDashboardResponse.DailySales;
import com.web.ecommerce.domain.dashboard.dto.AdminDashboardResponse.DailyUsers;
import com.web.ecommerce.domain.dashboard.dto.AdminDashboardResponse.EventCount;
import com.web.ecommerce.domain.dashboard.dto.AdminDashboardResponse.TopCategory;
import com.web.ecommerce.domain.dashboard.dto.AdminDashboardResponse.TopProduct;
import com.web.ecommerce.domain.dashboard.dto.DashboardShared.AdStats;
import com.web.ecommerce.domain.dashboard.dto.UserDashboardResponse.TimeSlotCount;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class EventLogRepository {

    @Qualifier("kafkaJdbcTemplate")
    private final JdbcTemplate jdbc;

    @Qualifier("kafkaNamedJdbcTemplate")
    private final NamedParameterJdbcTemplate namedJdbc;

    // ──────────────── Admin ────────────────

    public List<DailySales> findDailySales(int days) {
        String sql = """
                SELECT TO_CHAR(DATE(event_timestamp), 'YYYY-MM-DD') AS date,
                       COALESCE(SUM(approved_amount), 0)            AS total_amount,
                       COUNT(*)                                      AS order_count
                FROM event_log
                WHERE event_name = 'purchase_button_click'
                  AND event_timestamp >= NOW() - MAKE_INTERVAL(days => ?)
                GROUP BY DATE(event_timestamp)
                ORDER BY date
                """;
        return jdbc.query(sql, (rs, i) -> new DailySales(
                rs.getString("date"),
                rs.getLong("total_amount"),
                rs.getLong("order_count")
        ), days);
    }

    public List<DailyUsers> findDau(int days) {
        String sql = """
                SELECT TO_CHAR(DATE(event_timestamp), 'YYYY-MM-DD') AS date,
                       COUNT(DISTINCT user_id)                       AS count
                FROM event_log
                WHERE event_name = 'login'
                  AND event_timestamp >= NOW() - MAKE_INTERVAL(days => ?)
                GROUP BY DATE(event_timestamp)
                ORDER BY date
                """;
        return jdbc.query(sql, (rs, i) -> new DailyUsers(
                rs.getString("date"),
                rs.getLong("count")
        ), days);
    }

    public List<TopProduct> findTopProducts(int limit) {
        String sql = """
                SELECT product_name,
                       product_category                  AS category,
                       COUNT(*)                          AS count,
                       COALESCE(SUM(approved_amount), 0) AS total_amount
                FROM event_log
                WHERE event_name = 'purchase_button_click'
                  AND product_name IS NOT NULL
                GROUP BY product_name, product_category
                ORDER BY count DESC
                LIMIT ?
                """;
        return jdbc.query(sql, (rs, i) -> new TopProduct(
                rs.getString("product_name"),
                rs.getString("category"),
                rs.getLong("count"),
                rs.getLong("total_amount")
        ), limit);
    }

    public List<TopCategory> findTopCategories(int limit) {
        String sql = """
                SELECT product_category                   AS category,
                       COUNT(*)                           AS count,
                       COALESCE(SUM(approved_amount), 0)  AS total_amount
                FROM event_log
                WHERE event_name = 'purchase_button_click'
                  AND product_category IS NOT NULL
                GROUP BY product_category
                ORDER BY count DESC
                LIMIT ?
                """;
        return jdbc.query(sql, (rs, i) -> new TopCategory(
                rs.getString("category"),
                rs.getLong("count"),
                rs.getLong("total_amount")
        ), limit);
    }

    public List<EventCount> findTodayEventCounts() {
        String sql = """
                SELECT event_name, COUNT(*) AS count
                FROM event_log
                WHERE DATE(event_timestamp) = CURRENT_DATE
                GROUP BY event_name
                ORDER BY count DESC
                """;
        return jdbc.query(sql, (rs, i) -> new EventCount(
                rs.getString("event_name"),
                rs.getLong("count")
        ));
    }

    public AdStats findAdStatsAll() {
        String sql = """
                SELECT COUNT(CASE WHEN event_name = 'ad_impression' THEN 1 END) AS impressions,
                       COUNT(CASE WHEN event_name = 'ad_click' THEN 1 END)      AS clicks
                FROM event_log
                """;
        return jdbc.queryForObject(sql, (rs, i) -> {
            long impressions = rs.getLong("impressions");
            long clicks = rs.getLong("clicks");
            return new AdStats(impressions, clicks, impressions > 0 ? (double) clicks / impressions * 100 : 0);
        });
    }

    // ──────────────── User ────────────────

    public boolean hasWithdrawalPageVisit(Long userId, int days) {
        String sql = """
                SELECT COUNT(*) FROM event_log
                WHERE user_id = ?
                  AND event_name = 'withdrawal_page_visit'
                  AND event_timestamp >= NOW() - MAKE_INTERVAL(days => ?)
                """;
        Long count = jdbc.queryForObject(sql, Long.class, userId, days);
        return count != null && count > 0;
    }

    public AdStats findAdStats(Long userId) {
        String sql = """
                SELECT COUNT(CASE WHEN event_name = 'ad_impression' THEN 1 END) AS impressions,
                       COUNT(CASE WHEN event_name = 'ad_click' THEN 1 END)      AS clicks
                FROM event_log
                WHERE user_id = ?
                """;
        return jdbc.queryForObject(sql, (rs, i) -> {
            long impressions = rs.getLong("impressions");
            long clicks = rs.getLong("clicks");
            return new AdStats(impressions, clicks, impressions > 0 ? (double) clicks / impressions * 100 : 0);
        }, userId);
    }

    public double findAdConversionRate(Long userId) {
        String sql = """
                SELECT COUNT(CASE WHEN event_name = 'ad_click' THEN 1 END)             AS clicks,
                       COUNT(CASE WHEN event_name = 'purchase_button_click' THEN 1 END) AS purchases
                FROM event_log
                WHERE user_id = ?
                """;
        return jdbc.queryForObject(sql, (rs, i) -> {
            long clicks = rs.getLong("clicks");
            long purchases = rs.getLong("purchases");
            return clicks > 0 ? (double) purchases / clicks * 100 : 0.0;
        }, userId);
    }

    public List<TimeSlotCount> findPeakHours(Long userId) {
        String sql = """
                SELECT slot, COUNT(*) AS count
                FROM (
                    SELECT CASE
                        WHEN EXTRACT(HOUR FROM event_timestamp) < 3  THEN '00-03'
                        WHEN EXTRACT(HOUR FROM event_timestamp) < 6  THEN '03-06'
                        WHEN EXTRACT(HOUR FROM event_timestamp) < 9  THEN '06-09'
                        WHEN EXTRACT(HOUR FROM event_timestamp) < 12 THEN '09-12'
                        WHEN EXTRACT(HOUR FROM event_timestamp) < 15 THEN '12-15'
                        WHEN EXTRACT(HOUR FROM event_timestamp) < 18 THEN '15-18'
                        WHEN EXTRACT(HOUR FROM event_timestamp) < 21 THEN '18-21'
                        ELSE '21-24'
                    END AS slot
                    FROM event_log
                    WHERE user_id = ?
                      AND event_name = 'login'
                      AND event_timestamp >= NOW() - INTERVAL '30 days'
                ) t
                GROUP BY slot
                ORDER BY slot
                """;
        return jdbc.query(sql, (rs, i) -> new TimeSlotCount(
                rs.getString("slot"),
                rs.getLong("count")
        ), userId);
    }

    public List<String> findRecentKeywords(Long userId, int limit) {
        String sql = """
                SELECT DISTINCT ON (search_keyword) search_keyword
                FROM event_log
                WHERE user_id = ?
                  AND event_name = 'search_button_click'
                  AND search_keyword IS NOT NULL
                ORDER BY search_keyword, event_timestamp DESC
                LIMIT ?
                """;
        return jdbc.queryForList(sql, String.class, userId, limit);
    }

    public List<CategoryCount> findTopCategoriesByUser(Long userId, int limit) {
        String sql = """
                SELECT product_category AS category, COUNT(*) AS count
                FROM event_log
                WHERE user_id = ?
                  AND event_name = 'purchase_button_click'
                  AND product_category IS NOT NULL
                GROUP BY product_category
                ORDER BY count DESC
                LIMIT ?
                """;
        return jdbc.query(sql, (rs, i) -> new CategoryCount(
                rs.getString("category"),
                rs.getLong("count")
        ), userId, limit);
    }

    public record CategoryCount(String category, long count) {}

    // ──────────────── Monthly Churn (Server B) ────────────────

    public record MonthlyChurnRow(String month, long churnVisitors) {}

    public List<MonthlyChurnRow> findMonthlyChurnVisitors() {
        String sql = """
                SELECT TO_CHAR((json_log::jsonb->>'eventTimestamp')::timestamp, 'YYYY-MM') AS month,
                       COUNT(DISTINCT json_log::jsonb->>'user_login_id')                    AS churn_visitors
                FROM event_log
                WHERE json_log::jsonb->>'event_name' = 'page_view'
                  AND json_log::jsonb->>'pageName' = '탈퇴'
                  AND (json_log::jsonb->>'eventTimestamp')::timestamp >= NOW() - INTERVAL '12 months'
                GROUP BY TO_CHAR((json_log::jsonb->>'eventTimestamp')::timestamp, 'YYYY-MM')
                ORDER BY month
                """;
        return jdbc.query(sql, (rs, i) -> new MonthlyChurnRow(
                rs.getString("month"),
                rs.getLong("churn_visitors")
        ));
    }

    // ──────────────── Admin User List (batch) ────────────────

    public record AdStatRow(long impressions, long clicks) {}

    public Map<Long, AdStatRow> findAdStatsByUserIds(List<Long> userIds) {
        if (userIds.isEmpty()) return Map.of();
        String sql = """
                SELECT user_id,
                       COUNT(CASE WHEN event_name = 'ad_impression' THEN 1 END) AS impressions,
                       COUNT(CASE WHEN event_name = 'ad_click' THEN 1 END)      AS clicks
                FROM event_log
                WHERE user_id IN (:userIds)
                GROUP BY user_id
                """;
        return namedJdbc.query(sql, Map.of("userIds", userIds), (rs, i) ->
                Map.entry(rs.getLong("user_id"), new AdStatRow(rs.getLong("impressions"), rs.getLong("clicks")))
        ).stream().collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }
}
