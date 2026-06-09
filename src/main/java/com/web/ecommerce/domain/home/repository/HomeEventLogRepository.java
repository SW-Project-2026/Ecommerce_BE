package com.web.ecommerce.domain.home.repository;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class HomeEventLogRepository {

    @Qualifier("kafkaJdbcTemplate")
    private final JdbcTemplate jdbc;

    public List<String> findInterestCategories(Long userId, int days, int limit) {
        String sql = """
                SELECT category, COUNT(*) AS cnt
                FROM (
                    SELECT product_category AS category
                    FROM event_log
                    WHERE user_id = ?
                      AND event_name IN ('product_detail_view', 'page_view', 'purchase_button_click')
                      AND product_category IS NOT NULL
                      AND event_timestamp >= NOW() - MAKE_INTERVAL(days => ?)
                    UNION ALL
                    SELECT search_keyword AS category
                    FROM event_log
                    WHERE user_id = ?
                      AND event_name = 'search_button_click'
                      AND search_keyword IS NOT NULL
                      AND event_timestamp >= NOW() - MAKE_INTERVAL(days => ?)
                ) t
                GROUP BY category
                ORDER BY cnt DESC
                LIMIT ?
                """;
        return jdbc.query(sql, (rs, i) -> rs.getString("category"), userId, days, userId, days, limit);
    }

    public List<Long> findRecentViewedProductIds(Long userId, int days, int limit) {
        String sql = """
                SELECT product_id
                FROM (
                    SELECT DISTINCT ON (product_id) product_id, event_timestamp
                    FROM event_log
                    WHERE user_id = ?
                      AND event_name = 'product_detail_view'
                      AND product_id IS NOT NULL
                      AND event_timestamp >= NOW() - MAKE_INTERVAL(days => ?)
                    ORDER BY product_id, event_timestamp DESC
                ) t
                ORDER BY event_timestamp DESC
                LIMIT ?
                """;
        return jdbc.query(sql, (rs, i) -> rs.getLong("product_id"), userId, days, limit);
    }
}
