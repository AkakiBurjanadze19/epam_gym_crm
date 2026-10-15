package com.epam_final_project.dao;

import com.epam_final_project.config.TestDataSourceConfig;
import com.epam_final_project.domain.Trainee;
import com.epam_final_project.domain.Trainer;
import com.epam_final_project.domain.Training;
import com.epam_final_project.domain.TrainingType;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

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
public class TrainerDaoImplTest {
    @PersistenceContext
    private EntityManager em;

    @Autowired
    private TrainerDao trainerDao;

    @Autowired
    private TraineeDao traineeDao;

    @Autowired
    private TrainingDao trainingDao;

    @Autowired
    private TrainingTypeDao trainingTypeDao;

    @BeforeEach
    public void setUp() {
        em.createQuery("DELETE FROM Trainer").executeUpdate();
    }

    @Test
    public void create_shouldCreateTrainerSuccessfully() {
        // Arrange
        Trainer trainer = getTrainer();

        // Act
        trainerDao.create(trainer);

        // Assert
        assertEquals("Maxime", trainer.getFirstName());
        assertEquals("Dvali", trainer.getLastName());
        assertEquals("maxime.dvali", trainer.getUsername());
        assertEquals("maxime200!", trainer.getPassword());
        assertTrue(trainer.isActive());
        assertEquals("Strength Training", trainer.getSpecialization().getTrainingTypeName());
    }

    @Test
    public void findById_shouldReturnExistingTrainer() {
        // Arrange
        Trainer trainer = getTrainer();
        trainerDao.create(trainer);

        // Act
        Optional<Trainer> result = trainerDao.findById(trainer.getId());

        // Assert
        assertTrue(result.isPresent());
    }

    @Test
    public void findById_shouldReturnEmptyWhenTrainerNotFound() {
        // Arrange
        Integer trainerId = 123;

        // Act
        Optional<Trainer> result = trainerDao.findById(trainerId);

        // Assert
        assertTrue(result.isEmpty());
    }

    @Test
    public void update_shouldUpdateTrainerFields() {
        // Arrange
        Trainer trainer = getTrainer();
        trainerDao.create(trainer);

        Trainer updatedTrainer = new Trainer();
        updatedTrainer.setFirstName("Joel");
        updatedTrainer.setLastName("Miller");
        updatedTrainer.setUsername("Joel.Miller");
        updatedTrainer.setPassword("newPassword");
        updatedTrainer.setActive(true);
        updatedTrainer.setSpecialization(new TrainingType("Mobility Training"));

        // Act
        trainerDao.update(updatedTrainer, trainer.getId());

        // Assert
        assertEquals("Joel", trainer.getFirstName());
        assertEquals("Miller", trainer.getLastName());
        assertEquals("Joel.Miller", trainer.getUsername());
        assertEquals("newPassword", trainer.getPassword());
        assertTrue(trainer.isActive());
        assertEquals("Mobility Training", trainer.getSpecialization().getTrainingTypeName());
    }

    @Test
    public void update_shouldNotThrowExceptionTrainerEmpty() {
        // Arrange
        Integer trainerId = 200;
        Trainer updated = new Trainer();

        // Assert
        assertDoesNotThrow(() -> {
            trainerDao.update(updated, trainerId);
        });
    }

    @Test
    public void findAll_shouldReturnListContainingTrainers() {
        // Arrange
        Trainer trainer = new Trainer();
        trainer.setFirstName("Maxime");
        trainer.setLastName("Dvali");
        trainer.setUsername("maxime.dvali");
        trainer.setPassword("maxime200!");
        trainer.setActive(true);
        trainerDao.create(trainer);

        // Act
        List<Trainer> trainers = trainerDao.findAll();

        // Assert
        assertNotNull(trainers);
        assertEquals("Maxime", trainers.get(0).getFirstName());
        assertEquals("Dvali", trainers.get(0).getLastName());
        assertEquals("maxime.dvali", trainers.get(0).getUsername());
        assertEquals("maxime200!", trainers.get(0).getPassword());
        assertTrue(trainer.isActive());
    }

    @Test
    public void findByUsername_shouldReturnExistingTrainer() {
        // Arrange
        Trainer trainer = getTrainer();
        trainerDao.create(trainer);

        // Act
        Optional<Trainer> foundTrainer = trainerDao.findByUsername(trainer.getUsername());

        // Assert
        assertFalse(foundTrainer.isEmpty());
        assertEquals("Maxime", foundTrainer.get().getFirstName());
        assertEquals("Dvali", foundTrainer.get().getLastName());
    }

    @Test
    public void findByUsername_shouldReturnEmptyWhenInvalidUsernameIsProvided() {
        // Arrange
        Trainer trainer = getTrainer();
        trainerDao.create(trainer);

        // Act
        Optional<Trainer> foundTrainer = trainerDao.findByUsername("Invalid Username");

        // Assert
        assertTrue(foundTrainer.isEmpty());
    }

    @Test
    public void findTrainingsByUsernameAndCriteria_shouldReturnTrainingsList() {
        // Arrange
        Trainer trainer = getTrainer();
        Training training = getTraining();
        trainerDao.create(trainer);
        trainingDao.create(training);

        trainer.addTraining(training);

        // Act
        List<Training> trainers = trainerDao.findTrainingsByUsernameAndCriteria(
                trainer.getUsername(),
                null,
                null,
                null
        );

        // Assert
        assertNotNull(trainers);
    }

    @Test
    public void findTrainersNotAssignedOnTraineeByTraineeUsername_shouldReturnTrainersList() {
        // Arrange
        Trainer trainer = getTrainer();
        Training training = getTraining();
        trainerDao.create(trainer);
        trainingDao.create(training);

        Trainee trainee1 = getTrainee();
        Trainee trainee2 = new Trainee();
        trainee2.setFirstName("John");
        trainee2.setLastName("Carmack");
        trainee2.setUsername("John.Carmack");
        trainee2.setPassword("doom222");
        trainee2.setActive(true);
        trainee2.setDateOfBirth(LocalDate.of(1972, 3, 25));
        trainee2.setAddress("Rustaveli N12");
        traineeDao.create(trainee1);
        traineeDao.create(trainee2);

        trainee1.addTrainer(trainer);

        // Act
        List<Trainer> result = trainerDao.findTrainersNotAssignedOnTraineeByTraineeUsername("John.Carmack");

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
    }

    private Trainer getTrainer() {
        Trainer trainer = new Trainer();
        trainer.setFirstName("Maxime");
        trainer.setLastName("Dvali");
        trainer.setUsername("maxime.dvali");
        trainer.setPassword("maxime200!");
        trainer.setActive(true);
        trainer.setSpecialization(trainingTypeDao.findById(3).get());
        return trainer;
    }

    private Training getTraining() {
        Training training = new Training();
        training.setName("Morning Cardio Blast");
        training.setType(trainingTypeDao.findById(2).get());
        training.setDate(LocalDate.of(2026, 9, 15));
        training.setDuration(1);
        return training;
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
        return trainee;
    }
}