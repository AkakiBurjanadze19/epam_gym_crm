package com.epam_final_project.service;

import com.epam_final_project.dao.TraineeDao;
import com.epam_final_project.dao.TrainerDao;
import com.epam_final_project.domain.Trainer;
import com.epam_final_project.domain.TrainingType;
import com.epam_final_project.service.impl.TrainerServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.naming.AuthenticationException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import com.epam_final_project.domain.Training;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.never;

@ExtendWith(MockitoExtension.class)
public class TrainerServiceImplTest {
    @Mock
    private TrainerDao trainerDao;
    @Mock
    private TraineeDao traineeDao;
    @Mock
    private AuthService authService;

    @InjectMocks
    private TrainerServiceImpl trainerServiceImpl;

    @Test
    public void createTrainer_shouldCallDaoCreate() {
        // Arrange
        Trainer trainer = new Trainer();
        trainer.setFirstName("Bob");
        trainer.setLastName("Smith");

        // Act
        trainerServiceImpl.createTrainer(trainer);

        // Assert
        verify(trainerDao).create(trainer);
    }

    @Test
    public void createTrainer_shouldGenerateUsernameAndPasswordWhenTheyAreNotProvided() {
        // Arrange
        Trainer trainer = new Trainer();
        trainer.setFirstName("Michael");
        trainer.setLastName("Desanta");

        // Act
        trainerServiceImpl.createTrainer(trainer);

        // Assert
        assertEquals("Michael.Desanta", trainer.getUsername());
        assertNotNull(trainer.getPassword());

        verify(trainerDao).create(trainer);
    }

    @Test
    public void updateTrainer_shouldCallDaoUpdate() throws AuthenticationException {
        // Arrange
        Integer trainerId = 1;

        Trainer updated = new Trainer();
        updated.setFirstName("Joel");
        updated.setLastName("Miller");
        updated.setUsername("Joel.Miller");
        updated.setPassword("newPassword");
        updated.setActive(true);
        updated.setSpecialization(new TrainingType("Mobility Training"));

        when(authService.checkTrainerUsernameAndPassword(
                trainerId,
                "Joel.Miller",
                "newPassword"))
                .thenReturn(true);

        // Act
        trainerServiceImpl.updateTrainer(updated, trainerId, "Joel.Miller", "newPassword");

        // Assert
        verify(trainerDao).update(updated, trainerId);
    }

    @Test
    public void updateTrainer_shouldThrowExceptionWhenInvalidCredentials() throws AuthenticationException {
        // Arrange
        Integer trainerId = 1;
        Trainer updated = new Trainer();
        updated.setFirstName("Joel");
        updated.setLastName("Miller");
        updated.setUsername("Joel.Miller");
        updated.setPassword("newPassword");
        updated.setActive(true);
        updated.setSpecialization(new TrainingType("Mobility Training"));

        when(authService.checkTrainerUsernameAndPassword(
                trainerId,
                "Joel.Miller",
                "wrongPassword"))
                .thenReturn(false);

        // Act & Assert
        assertThrows(AuthenticationException.class, () -> {
            trainerServiceImpl.updateTrainer(updated, trainerId, "Joel.Miller", "wrongPassword");
        });

        verify(trainerDao, never()).update(updated, trainerId);
    }

    @Test
    public void selectTrainer_shouldReturnDaoResult() {
        // Arrange
        Trainer trainer = new Trainer();
        Integer trainerId = 10;

        when(trainerDao.findById(trainerId)).thenReturn(Optional.of(trainer));

        // Act
        Optional<Trainer> result = trainerServiceImpl.selectTrainer(trainerId);

        // Assert
        assertEquals(result, Optional.of(trainer));
        verify(trainerDao).findById(trainerId);
    }

    @Test
    public void selectTrainerByUsername_shouldReturnDaoResult() {
        // Arrange
        Trainer trainer = new Trainer();
        trainer.setFirstName("Michael");
        trainer.setLastName("Desanta");
        trainer.setUsername("Michael.Desanta");

        when(trainerDao.findByUsername(trainer.getUsername())).thenReturn(Optional.of(trainer));

        // Act
        Optional<Trainer> foundTrainer = trainerServiceImpl.selectTrainerByUsername(trainer.getUsername());

        // Assert
        assertFalse(foundTrainer.isEmpty());
        assertEquals("Michael", foundTrainer.get().getFirstName());
        assertEquals("Desanta", foundTrainer.get().getLastName());
    }

    @Test
    public void createTrainer_shouldThrowExceptionWhenFirstNameIsNull() {
        // Arrange
        Trainer trainer = new Trainer();
        trainer.setFirstName(null);
        trainer.setLastName("Smith");

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            trainerServiceImpl.createTrainer(trainer);
        });
    }

    @Test
    public void createTrainer_shouldThrowExceptionWhenFirstNameIsEmpty() {
        // Arrange
        Trainer trainer = new Trainer();
        trainer.setFirstName("");
        trainer.setLastName("Smith");

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            trainerServiceImpl.createTrainer(trainer);
        });
    }

    @Test
    public void createTrainer_shouldThrowExceptionWhenLastNameIsNull() {
        // Arrange
        Trainer trainer = new Trainer();
        trainer.setFirstName("John");
        trainer.setLastName(null);

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            trainerServiceImpl.createTrainer(trainer);
        });
    }

    @Test
    public void createTrainer_shouldThrowExceptionWhenLastNameIsEmpty() {
        // Arrange
        Trainer trainer = new Trainer();
        trainer.setFirstName("John");
        trainer.setLastName("");

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            trainerServiceImpl.createTrainer(trainer);
        });
    }

    @Test
    public void changeTrainerPassword_shouldChangePassword() throws AuthenticationException {
        // Arrange
        Integer trainerId = 1;
        Trainer trainer = new Trainer();
        trainer.setFirstName("John");
        trainer.setLastName("Smith");
        trainer.setUsername("john.smith");
        trainer.setPassword("oldPassword");
        trainer.setActive(true);

        String newPassword = "newPassword123";

        when(authService.checkTrainerUsernameAndPassword(
                trainerId,
                trainer.getUsername(),
                "oldPassword"))
                .thenReturn(true);
        when(trainerDao.findById(trainerId)).thenReturn(Optional.of(trainer));

        // Act
        trainerServiceImpl.changeTrainerPassword(newPassword, trainerId, trainer.getUsername(), "oldPassword");

        // Assert
        assertEquals(newPassword, trainer.getPassword());
        verify(trainerDao).findById(trainerId);
    }

    @Test
    public void activateTrainer_shouldSetActiveToTrue() throws AuthenticationException {
        // Arrange
        Integer trainerId = 1;
        Trainer trainer = new Trainer();
        trainer.setFirstName("John");
        trainer.setLastName("Smith");
        trainer.setUsername("john.smith");
        trainer.setPassword("password");
        trainer.setActive(false);

        when(authService.checkTrainerUsernameAndPassword(
                trainerId,
                trainer.getUsername(),
                trainer.getPassword()))
                .thenReturn(true);
        when(trainerDao.findById(trainerId)).thenReturn(Optional.of(trainer));

        // Act
        trainerServiceImpl.activateTrainer(trainerId, trainer.getUsername(), trainer.getPassword());

        // Assert
        assertTrue(trainer.isActive());
        verify(trainerDao).findById(trainerId);
    }

    @Test
    public void deActivateTrainer_shouldSetActiveToFalse() throws AuthenticationException {
        // Arrange
        Integer trainerId = 1;
        Trainer trainer = new Trainer();
        trainer.setFirstName("John");
        trainer.setLastName("Smith");
        trainer.setUsername("john.smith");
        trainer.setPassword("password");
        trainer.setActive(true);

        when(authService.checkTrainerUsernameAndPassword(
                trainerId,
                trainer.getUsername(),
                trainer.getPassword()))
                .thenReturn(true);
        when(trainerDao.findById(trainerId)).thenReturn(Optional.of(trainer));

        // Act
        trainerServiceImpl.deActivateTrainer(trainerId, trainer.getUsername(), trainer.getPassword());

        // Assert
        assertFalse(trainer.isActive());
        verify(trainerDao).findById(trainerId);
    }

    @Test
    public void getTrainingsByUsernameAndCriteria_shouldCallDaoMethod() {
        // Arrange
        String trainerUsername = "john.smith";
        LocalDate fromDate = LocalDate.of(2026, 1, 1);
        LocalDate toDate = LocalDate.of(2026, 12, 31);
        String traineeName = "trainee";

        List<Training> expectedTrainings = new ArrayList<>();
        when(trainerDao.findTrainingsByUsernameAndCriteria(
                trainerUsername,
                fromDate,
                toDate,
                traineeName))
                .thenReturn(expectedTrainings);

        // Act
        List<Training> result = trainerServiceImpl.getTrainingsByUsernameAndCriteria(
                trainerUsername,
                fromDate,
                toDate,
                traineeName);

        // Assert
        assertEquals(expectedTrainings, result);
        verify(trainerDao).findTrainingsByUsernameAndCriteria(
                trainerUsername,
                fromDate,
                toDate,
                traineeName);
    }

    @Test
    public void getTrainersNotAssignedOnTraineeByTraineeUsername_shouldCallDaoMethod() {
        // Arrange
        String username = "john.smith";
        List<Trainer> expectedTrainers = new ArrayList<>();
        expectedTrainers.add(new Trainer());

        when(trainerDao.findTrainersNotAssignedOnTraineeByTraineeUsername(username))
                .thenReturn(expectedTrainers);

        // Act
        List<Trainer> result = trainerServiceImpl.getTrainersNotAssignedOnTraineeByTraineeUsername(username);

        // Assert
        assertEquals(expectedTrainers, result);
        verify(trainerDao).findTrainersNotAssignedOnTraineeByTraineeUsername(username);
    }
}