package com.epam_final_project.storage;

import com.epam_final_project.domain.Trainee;
import com.epam_final_project.domain.Trainer;
import com.epam_final_project.domain.Training;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import java.util.HashMap;
import java.util.Map;

@Configuration
@PropertySource("classpath:application.properties")
public class Storage {
    private static final Logger log = LoggerFactory.getLogger(Storage.class);

    @Bean
    public Map<Integer, Trainer> trainers() {
        log.info("Initializing trainers storage Map");
        Map<Integer, Trainer> trainers = new HashMap<>();
        log.info("Trainers storage initialized with capacity: {}", trainers.size());
        return trainers;
    }

    @Bean
    public Map<Integer, Trainee> trainees() {
        log.info("Initializing trainees storage Map");
        Map<Integer, Trainee> trainees = new HashMap<>();
        log.info("Trainees storage initialized with capacity: {}", trainees.size());
        return trainees;
    }

    @Bean
    public Map<Integer, Training> trainings() {
        log.info("Initializing trainings storage Map");
        Map<Integer, Training> trainings = new HashMap<>();
        log.info("Trainings storage initialized with capacity: {}", trainings.size());
        return trainings;
    }

    @Bean
    public StorageInitializer trainersInitializer(
            @Value("${storage.data.file.trainers}") String filePath,
            Map<Integer, Trainer> trainers,
            Map<Integer, Trainee> trainees,
            Map<Integer, Training> trainings
    ) {
        log.info("Creating StorageInitializer for trainers from file: {}", filePath);
        StorageInitializer initializer = new StorageInitializer(filePath, trainers, trainees, trainings);
        log.info("StorageInitializer for trainers created successfully");
        return initializer;
    }

    @Bean
    public StorageInitializer traineesInitializer(
            @Value("${storage.data.file.trainees}") String filePath,
            Map<Integer, Trainer> trainers,
            Map<Integer, Trainee> trainees,
            Map<Integer, Training> trainings
    ) {
        log.info("Creating StorageInitializer for trainees from file: {}", filePath);
        StorageInitializer initializer = new StorageInitializer(filePath, trainers, trainees, trainings);
        log.info("StorageInitializer for trainees created successfully");
        return initializer;
    }

    @Bean
    public StorageInitializer trainingsInitializer(
            @Value("${storage.data.file.trainings}") String filePath,
            Map<Integer, Trainer> trainers,
            Map<Integer, Trainee> trainees,
            Map<Integer, Training> trainings
    ) {
        log.info("Creating StorageInitializer for trainings from file: {}", filePath);
        StorageInitializer initializer = new StorageInitializer(filePath, trainers, trainees, trainings);
        log.info("StorageInitializer for trainings created successfully");
        return initializer;
    }
}