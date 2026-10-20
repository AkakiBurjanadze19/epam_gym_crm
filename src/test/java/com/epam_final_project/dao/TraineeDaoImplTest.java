package com.epam_final_project.dao;

import com.epam_final_project.config.TestDataSourceConfig;
import com.epam_final_project.domain.Trainee;
import com.epam_final_project.domain.Trainer;
import com.epam_final_project.domain.Training;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.support.AnnotationConfigContextLoader;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(
        classes = {TestDataSourceConfig.class},
        loader = AnnotationConfigContextLoader.class
)
@Transactional
public class TraineeDaoImplTest {
    @PersistenceContext
    private EntityManager em;

    @Autowired
    private TraineeDao traineeDao;

    @Autowired
    private TrainerDao trainerDao;

    @Autowired
    private TrainingDao trainingDao;

    @Autowired
    private TrainingTypeDao trainingTypeDao;

    @BeforeEach
    public void setUp() {
        em.createQuery("DELETE FROM Trainee").executeUpdate();
    }

    @Test
    public void create_shouldCreateTraineeSuccessfully() {
        Trainee trainee = getTrainee();

        assertEquals("John", trainee.getFirstName());
        assertEquals("Smith", trainee.getLastName());
        assertEquals("john.smith", trainee.getUsername());
        assertTrue(trainee.isActive());
        assertEquals(LocalDate.of(2005, 12, 3), trainee.getDateOfBirth());
        assertEquals("Rustaveli N12", trainee.getAddress());
        assertEquals("password123", trainee.getPassword());
    }

    @Test
    public void findById_shouldReturnExistingTrainee() {
        Trainee trainee = getTrainee();

        Optional<Trainee> result = traineeDao.findById(trainee.getId());

        assertTrue(result.isPresent());
        assertEquals("John", result.get().getFirstName());
        assertEquals("Smith", result.get().getLastName());
        assertEquals("john.smith", result.get().getUsername());
        assertEquals("password123", result.get().getPassword());
    }

    @Test
    public void findById_shouldReturnEmptyWhenTraineeNotFound() {
        Integer traineeId = 999;

        Optional<Trainee> result = traineeDao.findById(traineeId);

        assertTrue(result.isEmpty());
    }

    @Test
    public void update_shouldUpdateTraineeFields() {
        Result result = getUpdatedResult();

        traineeDao.update(result.updatedTrainee(), result.existing().getId());

        assertEquals("Nikoloz", result.existing().getFirstName());
        assertEquals("Burduli", result.existing().getLastName());
        assertEquals("john.smith", result.existing().getUsername());
        assertEquals("currentPassword", result.existing().getPassword());
        assertFalse(result.existing().isActive());
        assertEquals(LocalDate.of(2002, 10, 12), result.existing().getDateOfBirth());
        assertEquals("Axalgazrdoba N5", result.existing().getAddress());
    }

    private Result getUpdatedResult() {
        Trainee existing = new Trainee();
        existing.setFirstName("John");
        existing.setLastName("Smith");
        existing.setUsername("john.smith");
        existing.setPassword("currentPassword");
        existing.setActive(true);
        existing.setDateOfBirth(LocalDate.of(2005, 12, 3));
        existing.setAddress("Rustaveli N12");
        traineeDao.create(existing);

        Trainee updatedTrainee = new Trainee();
        updatedTrainee.setFirstName("Nikoloz");
        updatedTrainee.setLastName("Burduli");
        updatedTrainee.setUsername("Nikoloz.Burduli");
        updatedTrainee.setPassword("newPassword");
        updatedTrainee.setActive(false);
        updatedTrainee.setDateOfBirth(LocalDate.of(2002, 10, 12));
        updatedTrainee.setAddress("Axalgazrdoba N5");
        Result result = new Result(existing, updatedTrainee);
        return result;
    }

    private record Result(Trainee existing, Trainee updatedTrainee) {
    }

    @Test
    public void update_shouldNotThrowExceptionWhenTraineeEmpty() {
        // Arrange
        Integer traineeId = 299;
        Trainee updated = new Trainee();

        // Assert
        assertDoesNotThrow(() -> {
            traineeDao.update(updated, traineeId);
        });
    }

    @Test
    public void delete_shouldDeleteTraineeSuccessfully() {
        // Arrange
        Trainee trainee = getTrainee();

        // Act
        traineeDao.delete(trainee.getId());

        // Assert
        Optional<Trainee> result = traineeDao.findById(trainee.getId());
        assertTrue(result.isEmpty());
    }

    @Test
    public void findAll_shouldReturnListContainingTrainees() {
        // Arrange
        getTrainee();

        // Act
        List<Trainee> traineesList = traineeDao.findAll();

        // Assert
        assertNotNull(traineesList);
        assertEquals(1, traineesList.size());
        assertEquals("John", traineesList.get(0).getFirstName());
        assertEquals("Smith", traineesList.get(0).getLastName());
        assertEquals("john.smith", traineesList.get(0).getUsername());
        assertEquals("password123", traineesList.get(0).getPassword());
    }

    @Test
    public void findByUsername_shouldReturnExistingTrainee() {
        // Arrange
        Trainee trainee = getTrainee();

        // Act
        Optional<Trainee> foundTrainee = traineeDao.findByUsername(trainee.getUsername());

        // Assert
        assertFalse(foundTrainee.isEmpty());
        assertEquals("john.smith", foundTrainee.get().getUsername());
        assertEquals("password123", foundTrainee.get().getPassword());
    }

    @Test
    public void findByUsername_shouldReturnEmptyWhenTraineeNotFound() {
        // Arrange
        String traineeUsername = "Dustin Morno";

        // Act
        Optional<Trainee> foundTrainee = traineeDao.findByUsername(traineeUsername);

        // Assert
        assertTrue(foundTrainee.isEmpty());
    }

    @Test
    public void deleteByUsername_shouldSuccessfullyDeleteExistingTrainee() {
        // Arrange
        Trainee trainee = getTrainee();

        // Act
        traineeDao.deleteByUsername(trainee.getUsername());

        Optional<Trainee> foundTrainee = traineeDao.findByUsername(trainee.getUsername());

        // Assert
        assertTrue(foundTrainee.isEmpty());
    }

    @Test
    public void findTrainingsByUsernameAndCriteria_returnListOfTrainings() {
        // Arrange
        Trainee trainee = getTrainee();

        Trainer trainer = getTrainer();

        Training training = getTraining();

        trainer.addTrainee(trainee);
        trainee.addTrainer(trainer);
        trainer.addTraining(training);

        // Act
        List<Training> result = traineeDao.findTrainingsByUsernameAndCriteria(
                "Joel.Miller",
                null,
                null,
                null,
                null
        );

        // Assert
        assertNotNull(result);
    }

    private Training getTraining() {
        Training training = new Training();
        training.setName("Morning Cardio Blast");
        training.setType(trainingTypeDao.findById(3).get());
        training.setDate(LocalDate.of(2026, 9, 15));
        training.setDuration(1);
        trainingDao.create(training);
        return training;
    }

    private Trainer getTrainer() {
        Trainer trainer = new Trainer();
        trainer.setFirstName("Joel");
        trainer.setLastName("Miller");
        trainer.setUsername("Joel.Miller");
        trainer.setPassword("newPassword");
        trainer.setActive(true);
        trainer.setSpecialization(trainingTypeDao.findById(3).get());
        trainerDao.create(trainer);
        return trainer;
    }

    private Trainee getTrainee() {
        Trainee trainee = new Trainee();
        trainee.setFirstName("John");
        trainee.setLastName("Smith");
        trainee.setUsername("john.smith");
        trainee.setPassword("password123");
        trainee.setActive(true);
        trainee.setDateOfBirth(LocalDate.of(2005, 12, 3));
        trainee.setAddress("Rustaveli N12");
        traineeDao.create(trainee);
        return trainee;
    }
}