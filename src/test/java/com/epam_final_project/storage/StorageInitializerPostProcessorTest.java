package com.epam_final_project.storage;

import com.epam_final_project.domain.Trainee;
import com.epam_final_project.domain.Trainer;
import com.epam_final_project.domain.Training;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.HashMap;
import java.util.Map;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {
        StorageInitializerPostProcessor.class,
        StorageInitializerPostProcessorTest.TestConfig.class
})
public class StorageInitializerPostProcessorTest {
    @MockitoSpyBean
    private StorageInitializer storageInitializer;

    @Test
    public void storageInitializerPostProcessor_triggersInitializationOnStartup() {
        Mockito.verify(storageInitializer, Mockito.times(1)).initialize();
    }

    @TestConfiguration
    static class TestConfig {
        @Bean
        String filePath() {
            return "data/trainers.csv";
        }

        @Bean
        Map<Integer, Trainer> trainers() {
            return new HashMap<>();
        }

        @Bean
        Map<Integer, Trainee> trainees() {
            return new HashMap<>();
        }

        @Bean
        Map<Integer, Training> trainings() {
            return new HashMap<>();
        }

        @Bean
        StorageInitializer storageInitializer(
                String filePath,
                Map<Integer, Trainee> trainees,
                Map<Integer, Trainer> trainers,
                Map<Integer, Training> trainings
        ) {
            return new StorageInitializer(filePath, trainers, trainees, trainings);
        }
    }
}
