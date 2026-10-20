package com.epam_final_project.service;

import com.epam_final_project.dao.TraineeDao;
import com.epam_final_project.dao.TrainerDao;
import com.epam_final_project.dao.TrainingTypeDao;
import com.epam_final_project.domain.Trainer;
import com.epam_final_project.domain.TrainingType;
import com.epam_final_project.dto.CreateTrainerRequest;
import com.epam_final_project.dto.TrainerDto;
import com.epam_final_project.dto.TrainingDto;
import com.epam_final_project.mapper.TrainerMapper;
import com.epam_final_project.mapper.TrainingMapper;
import com.epam_final_project.service.impl.TrainerServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

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
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.argThat;

@ExtendWith(MockitoExtension.class)
public class TrainerServiceImplTest {
    @Mock
    private TrainerDao trainerDao;
    @Mock
    private TraineeDao traineeDao;
    @Mock
    private AuthService authService;
    @Mock
    private TrainingTypeDao trainingTypeDao;
    @Mock
    private TrainerMapper trainerMapper;
    @Mock
    private TrainingMapper trainingMapper;

    @InjectMocks
    private TrainerServiceImpl trainerServiceImpl;

    @Test
    public void createTrainer_shouldCallDaoCreate() {
        // Arrange
        CreateTrainerRequest request = new CreateTrainerRequest();
        request.setFirstName("Bob");
        request.setLastName("Smith");
        request.setSpecialization("Yoga");

        Trainer trainer = new Trainer();
        trainer.setFirstName("Bob");
        trainer.setLastName("Smith");

        TrainingType trainingType = new TrainingType();
        trainingType.setTrainingTypeName("Yoga");

        when(trainerMapper.toEntity(request)).thenReturn(trainer);
        when(trainingTypeDao.findByTrainingTypeName("Yoga")).thenReturn(Optional.of(trainingType));

        // Act
        trainerServiceImpl.createTrainer(request);

        // Assert
        verify(trainerDao).create(any(Trainer.class));
    }

    @Test
    public void createTrainer_shouldGenerateUsernameAndPasswordWhenTheyAreNotProvided() {
        // Arrange
        CreateTrainerRequest request = new CreateTrainerRequest();
        request.setFirstName("Michael");
        request.setLastName("Desanta");
        request.setSpecialization("Strength");

        Trainer trainer = new Trainer();
        trainer.setFirstName("Michael");
        trainer.setLastName("Desanta");

        TrainingType trainingType = new TrainingType();
        trainingType.setTrainingTypeName("Strength");

        when(trainerMapper.toEntity(request)).thenReturn(trainer);
        when(trainingTypeDao.findByTrainingTypeName("Strength")).thenReturn(Optional.of(trainingType));

        // Act
        trainerServiceImpl.createTrainer(request);

        // Assert
        verify(trainerDao).create(argThat(t ->
            "Michael.Desanta".equals(t.getUsername()) && t.getPassword() != null
        ));
    }

    @Test
    public void updateTrainer_shouldCallDaoUpdate() throws AuthenticationException {
        // Arrange
        Integer trainerId = 1;

        CreateTrainerRequest request = new CreateTrainerRequest();
        request.setFirstName("Joel");
        request.setLastName("Miller");
        request.setSpecialization("Mobility Training");

        Trainer trainer = new Trainer();
        trainer.setFirstName("Joel");
        trainer.setLastName("Miller");

        when(authService.checkTrainerUsernameAndPassword(
                "Joel.Miller",
                "newPassword"))
                .thenReturn(true);
        when(trainerMapper.toEntity(request)).thenReturn(trainer);

        // Act
        trainerServiceImpl.updateTrainer(request, trainerId, "Joel.Miller", "newPassword");

        // Assert
        verify(trainerDao).update(any(Trainer.class), any(Integer.class));
    }

    @Test
    public void updateTrainer_shouldThrowExceptionWhenInvalidCredentials() throws AuthenticationException {
        // Arrange
        Integer trainerId = 1;
        CreateTrainerRequest request = new CreateTrainerRequest();
        request.setFirstName("Joel");
        request.setLastName("Miller");
        request.setSpecialization("Mobility Training");

        when(authService.checkTrainerUsernameAndPassword(
                "Joel.Miller",
                "wrongPassword"))
                .thenReturn(false);

        // Act & Assert
        assertThrows(AuthenticationException.class, () -> {
            trainerServiceImpl.updateTrainer(request, trainerId, "Joel.Miller", "wrongPassword");
        });

        verify(trainerDao, never()).update(any(Trainer.class), any(Integer.class));
    }

    @Test
    public void selectTrainer_shouldReturnDaoResult() {
        // Arrange
        Integer trainerId = 10;
        Trainer trainer = new Trainer();
        trainer.setFirstName("John");
        trainer.setLastName("Smith");
        trainer.setUsername("john.smith");

        TrainerDto trainerDto = new TrainerDto();
        trainerDto.setFirstName("John");
        trainerDto.setLastName("Smith");

        when(trainerDao.findById(trainerId)).thenReturn(Optional.of(trainer));
        when(trainerMapper.toDto(trainer)).thenReturn(trainerDto);

        // Act
        Optional<TrainerDto> result = trainerServiceImpl.selectTrainer(trainerId);

        // Assert
        assertTrue(result.isPresent());
        assertEquals("John", result.get().getFirstName());
        assertEquals("Smith", result.get().getLastName());
        verify(trainerDao).findById(trainerId);
    }

    @Test
    public void selectTrainerByUsername_shouldReturnDaoResult() {
        // Arrange
        Trainer trainer = new Trainer();
        trainer.setFirstName("Michael");
        trainer.setLastName("Desanta");
        trainer.setUsername("Michael.Desanta");

        TrainerDto trainerDto = new TrainerDto();
        trainerDto.setFirstName("Michael");
        trainerDto.setLastName("Desanta");

        when(trainerDao.findByUsername(trainer.getUsername())).thenReturn(Optional.of(trainer));
        when(trainerMapper.toDto(trainer)).thenReturn(trainerDto);

        // Act
        Optional<TrainerDto> foundTrainer = trainerServiceImpl.selectTrainerByUsername(trainer.getUsername());

        // Assert
        assertFalse(foundTrainer.isEmpty());
        assertEquals("Michael", foundTrainer.get().getFirstName());
        assertEquals("Desanta", foundTrainer.get().getLastName());
    }

    @Test
    public void createTrainer_shouldThrowExceptionWhenFirstNameIsNull() {
        // Arrange
        CreateTrainerRequest request = new CreateTrainerRequest();
        request.setFirstName(null);
        request.setLastName("Smith");
        request.setSpecialization("Yoga");

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            trainerServiceImpl.createTrainer(request);
        });
    }

    @Test
    public void createTrainer_shouldThrowExceptionWhenFirstNameIsEmpty() {
        // Arrange
        CreateTrainerRequest request = new CreateTrainerRequest();
        request.setFirstName("");
        request.setLastName("Smith");
        request.setSpecialization("Yoga");

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            trainerServiceImpl.createTrainer(request);
        });
    }

    @Test
    public void createTrainer_shouldThrowExceptionWhenLastNameIsNull() {
        // Arrange
        CreateTrainerRequest request = new CreateTrainerRequest();
        request.setFirstName("John");
        request.setLastName(null);
        request.setSpecialization("Yoga");

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            trainerServiceImpl.createTrainer(request);
        });
    }

    @Test
    public void createTrainer_shouldThrowExceptionWhenLastNameIsEmpty() {
        // Arrange
        CreateTrainerRequest request = new CreateTrainerRequest();
        request.setFirstName("John");
        request.setLastName("");
        request.setSpecialization("Yoga");

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            trainerServiceImpl.createTrainer(request);
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
        List<TrainingDto> result = trainerServiceImpl.getTrainingsByUsernameAndCriteria(
                trainerUsername,
                fromDate,
                toDate,
                traineeName);

        // Assert
        assertEquals(expectedTrainings.size(), result.size());
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

        TrainerDto trainerDto = new TrainerDto();
        trainerDto.setFirstName("Trainer");
        trainerDto.setLastName("One");

        when(trainerDao.findTrainersNotAssignedOnTraineeByTraineeUsername(username))
                .thenReturn(expectedTrainers);
        when(trainerMapper.toDto(any(Trainer.class))).thenReturn(trainerDto);

        // Act
        List<TrainerDto> result = trainerServiceImpl.getTrainersNotAssignedOnTraineeByTraineeUsername(username);

        // Assert
        assertEquals(expectedTrainers.size(), result.size());
        verify(trainerDao).findTrainersNotAssignedOnTraineeByTraineeUsername(username);
    }
}