package com.epam_final_project.health;

import com.epam_final_project.dao.TrainingTypeDao;
import com.epam_final_project.domain.TrainingType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class TrainingTypeHealthIndicator implements HealthIndicator {

    private final TrainingTypeDao trainingTypeDao;
    private static final int EXPECTED_TRAINING_TYPES = 6;

    @Autowired
    public TrainingTypeHealthIndicator(TrainingTypeDao trainingTypeDao) {
        this.trainingTypeDao = trainingTypeDao;
    }

    @Override
    public Health health() {
        try {
            List<TrainingType> trainingTypes = trainingTypeDao.findAll();

            if (trainingTypes == null || trainingTypes.isEmpty()) {
                return Health.down()
                        .withDetail("trainingTypes", "No training types found")
                        .withDetail("expectedCount", EXPECTED_TRAINING_TYPES)
                        .withDetail("actualCount", 0)
                        .build();
            }

            int actualCount = trainingTypes.size();

            if (actualCount < EXPECTED_TRAINING_TYPES) {
                return Health.down()
                        .withDetail("trainingTypes", "Incomplete training types")
                        .withDetail("expectedCount", EXPECTED_TRAINING_TYPES)
                        .withDetail("actualCount", actualCount)
                        .withDetail("missing", EXPECTED_TRAINING_TYPES - actualCount)
                        .build();
            }

            // Build details with training type names
            var typeNames = trainingTypes
                    .stream()
                    .map(TrainingType::getTrainingTypeName)
                    .toList();

            return Health.up()
                    .withDetail("trainingTypes", "Seeded correctly")
                    .withDetail("count", actualCount)
                    .withDetail("types", typeNames)
                    .build();
        } catch (Exception ex) {
            return Health.down()
                    .withDetail("trainingTypes", "Error checking training types")
                    .withDetail("error", ex.getMessage())
                    .withException(ex)
                    .build();
        }
    }
}