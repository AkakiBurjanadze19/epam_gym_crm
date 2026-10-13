package com.epam_final_project.storage;

import com.epam_final_project.domain.Trainee;
import com.epam_final_project.domain.Trainer;
import com.epam_final_project.domain.Training;
import com.epam_final_project.domain.TrainingType;
import com.epam_final_project.storage.StorageInitializer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class StorageInitializerTest {
    private Map<Integer, Trainer> trainers;
    private Map<Integer, Trainee> trainees;
    private Map<Integer, Training> trainings;

    @BeforeEach
    public void setUp() {
        trainers = new HashMap<>();
        trainees = new HashMap<>();
        trainings = new HashMap<>();
    }

    @Test
    public void initialize_shouldLoadTrainersFromCsv() {
        StorageInitializer initializer = new StorageInitializer(
                "data/trainers.csv",
                trainers,
                trainees,
                trainings
        );

        initializer.initialize();

        assertEquals(2, trainers.size());

        assertNotNull(trainers.get(1));
        assertNotNull(trainers.get(2));

        Trainer trainer = trainers.get(1);

        assertEquals("Max", trainer.getFirstName());
        assertEquals("Payne", trainer.getLastName());
        assertNotNull(trainer.getPassword());
        assertTrue(trainer.isActive());
        assertEquals(TrainingType.CARDIO, trainer.getSpecialization());
    }

    @Test
    public void initialize_shouldLoadTraineesFromCsv() {
        StorageInitializer initializer = new StorageInitializer(
                "data/trainees.csv",
                trainers,
                trainees,
                trainings
        );

        initializer.initialize();

        assertEquals(2, trainees.size());

        assertNotNull(trainees.get(1));
        assertNotNull(trainees.get(2));

        Trainee trainee = trainees.get(1);

        assertEquals("Alice", trainee.getFirstName());
        assertEquals("Brown", trainee.getLastName());
        assertNotNull(trainee.getPassword());
        assertTrue(trainee.isActive());
        assertEquals(LocalDate.of(2000, 5, 10), trainee.getDateOfBirth());
        assertEquals("Tbilisi", trainee.getAddress());
    }

    @Test
    public void initialize_shouldLoadTrainingsFromCsv() {
        StorageInitializer initializer = new StorageInitializer(
                "data/trainings.csv",
                trainers,
                trainees,
                trainings
        );

        initializer.initialize();

        assertEquals(2, trainings.size());

        assertNotNull(trainings.get(1));
        assertNotNull(trainings.get(2));

        Training training = trainings.get(1);

        assertEquals("Morning Cardio Blast", training.getName());
        assertEquals(TrainingType.CARDIO, training.getType());
        assertEquals(LocalDate.of(2026, 9, 26), training.getDate());
        assertEquals(Duration.ofHours(1), training.getDuration());
    }
}
