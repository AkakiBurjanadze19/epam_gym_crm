package com.epam_final_project.health;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;

@Component
public class DatabaseHealthIndicator implements HealthIndicator {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public DatabaseHealthIndicator(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    public Health health() {
        try {
            // Check database connectivity
            Integer result = jdbcTemplate.queryForObject("SELECT 1", Integer.class);

            if (result != null && result == 1) {
                // Get table counts for key entities
                long traineeCount = getTableCount("trainees");
                long trainerCount = getTableCount("trainers");
                long trainingCount = getTableCount("trainings");
                long trainingTypeCount = getTableCount("training_types");
                long userCount = getTableCount("users");

                return Health.up()
                        .withDetail("database", "H2/PostgreSQL")
                        .withDetail("connection", "OK")
                        .withDetail("trainees", traineeCount)
                        .withDetail("trainers", trainerCount)
                        .withDetail("trainings", trainingCount)
                        .withDetail("trainingTypes", trainingTypeCount)
                        .withDetail("users", userCount)
                        .build();
            } else {
                return Health.down()
                        .withDetail("database", "H2/PostgreSQL")
                        .withDetail("connection", "FAILED - unexpected result")
                        .build();
            }
        } catch (Exception ex) {
            return Health.down()
                    .withDetail("database", "H2/PostgreSQL")
                    .withDetail("connection", "FAILED")
                    .withDetail("error", ex.getMessage())
                    .withException(ex)
                    .build();
        }
    }

    private long getTableCount(String tableName) {
        try {
            return jdbcTemplate.queryForObject(
                    "SELECT COUNT(*) FROM " + tableName, Long.class);
        } catch (Exception ex) {
            return -1;
        }
    }
}