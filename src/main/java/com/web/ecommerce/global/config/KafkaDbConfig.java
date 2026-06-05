package com.web.ecommerce.global.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

@Configuration
public class KafkaDbConfig {

    @Value("${kafka-db.datasource.url}")
    private String url;

    @Value("${kafka-db.datasource.username}")
    private String username;

    @Value("${kafka-db.datasource.password}")
    private String password;

    private HikariDataSource buildDataSource() {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(url);
        config.setUsername(username);
        config.setPassword(password);
        config.setDriverClassName("org.postgresql.Driver");
        config.setMaximumPoolSize(5);
        config.setReadOnly(true);
        return new HikariDataSource(config);
    }

    @Bean("kafkaJdbcTemplate")
    public JdbcTemplate kafkaJdbcTemplate() {
        return new JdbcTemplate(buildDataSource());
    }

    @Bean("kafkaNamedJdbcTemplate")
    public NamedParameterJdbcTemplate kafkaNamedJdbcTemplate() {
        return new NamedParameterJdbcTemplate(buildDataSource());
    }
}
