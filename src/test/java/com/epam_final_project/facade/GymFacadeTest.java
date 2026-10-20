package com.epam_final_project.facade;

import com.epam_final_project.dto.*;
import com.epam_final_project.service.TraineeService;
import com.epam_final_project.service.TrainerService;
import com.epam_final_project.service.TrainingService;
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class GymFacadeTest {
    @Mock
    private TraineeService traineeService;

    @Mock
    private TrainerService trainerService;

    @Mock
    private TrainingService trainingService;

    @InjectMocks
    private GymFacade gymFacade;

    @Test
    public void createTrainee_shouldDelegateToTraineeService() {
        // Arrange
        CreateTraineeRequest request = new CreateTraineeRequest();
        request.setFirstName("John");
        request.setLastName("Smith");
        request.setDateOfBirth(LocalDate.of(2000, 1, 1));
        request.setAddress("Test Address");

        // Act
        gymFacade.createTrainee(request);

        // Assert
        verify(traineeService).createTrainee(request);
    }

    @Test
    public void updateTrainee_shouldDelegateToTraineeService() throws AuthenticationException {
        // Arrange
        CreateTraineeRequest request = new CreateTraineeRequest();
        request.setFirstName("Jane");
        request.setLastName("Doe");
        Integer traineeId = 10;

        // Act
        gymFacade.updateTrainee(request, traineeId, "john.smith", "password");

        // Assert
        verify(traineeService).updateTrainee(request, traineeId, "john.smith", "password");
    }

    @Test
    public void deleteTrainee_shouldDelegateToTraineeService() throws AuthenticationException {
        // Arrange
        Integer traineeId = 100;

        // Act
        gymFacade.deleteTrainee(traineeId, "john.smith", "password");

        // Assert
        verify(traineeService).deleteTrainee(traineeId, "john.smith", "password");
    }

    @Test
    public void selectTrainee_shouldDelegateToTraineeService() {
        // Arrange
        Integer traineeId = 10;
        TraineeDto traineeDto = new TraineeDto();
        traineeDto.setFirstName("John");
        traineeDto.setLastName("Smith");

        when(traineeService.selectTrainee(traineeId)).thenReturn(Optional.of(traineeDto));

        // Act
        Optional<TraineeDto> result = gymFacade.selectTrainee(traineeId);

        // Assert
        assertEquals(Optional.of(traineeDto), result);
        verify(traineeService).selectTrainee(traineeId);
    }

    @Test
    public void createTrainer_shouldDelegateToTrainerService() {
        // Arrange
        CreateTrainerRequest request = new CreateTrainerRequest();
        request.setFirstName("John");
        request.setLastName("Smith");
        request.setSpecialization("Yoga");

        // Act
        gymFacade.createTrainer(request);

        // Assert
        verify(trainerService).createTrainer(request);
    }

    @Test
    public void updateTrainer_shouldDelegateToTrainerService() throws AuthenticationException {
        // Arrange
        CreateTrainerRequest request = new CreateTrainerRequest();
        request.setFirstName("Jane");
        request.setLastName("Doe");
        request.setSpecialization("Strength");
        Integer trainerId = 10;

        // Act
        gymFacade.updateTrainer(request, trainerId, "john.smith", "password");

        // Assert
        verify(trainerService).updateTrainer(request, trainerId, "john.smith", "password");
    }

    @Test
    public void selectTrainer_shouldDelegateToTrainerService() {
        // Arrange
        Integer trainerId = 139;
        TrainerDto trainerDto = new TrainerDto();
        trainerDto.setFirstName("John");
        trainerDto.setLastName("Smith");

        when(trainerService.selectTrainer(trainerId)).thenReturn(Optional.of(trainerDto));

        // Act
        Optional<TrainerDto> result = gymFacade.selectTrainer(trainerId);

        // Assert
        assertEquals(Optional.of(trainerDto), result);
        verify(trainerService).selectTrainer(trainerId);
    }

    @Test
    public void createTraining_shouldDelegateToTrainingService() {
        // Arrange
        CreateTrainingRequest request = new CreateTrainingRequest();
        request.setTraineeUsername("john.smith");
        request.setTrainerUsername("trainer.jane");
        request.setName("Morning Yoga");
        request.setDate(LocalDate.now());
        request.setDuration(60);

        // Act
        gymFacade.createTraining(request);

        // Assert
        verify(trainingService).createTraining(request);
    }

    @Test
    public void selectTraining_shouldDelegateToTrainingService() {
        // Arrange
        Integer trainingId = 789;
        TrainingDto trainingDto = new TrainingDto();
        trainingDto.setName("Morning Yoga");

        when(trainingService.selectTraining(trainingId)).thenReturn(Optional.of(trainingDto));

        // Act
        Optional<TrainingDto> result = gymFacade.selectTraining(trainingId);

        // Assert
        assertEquals(Optional.of(trainingDto), result);
        verify(trainingService).selectTraining(trainingId);
    }

    @Test
    public void changeTraineePassword_shouldDelegateToTraineeService() throws AuthenticationException {
        // Arrange
        String updatedPassword = "newPassword";
        Integer traineeId = 1;
        String username = "john.smith";
        String password = "oldPassword";

        // Act
        gymFacade.changeTraineePassword(updatedPassword, traineeId, username, password);

        // Assert
        verify(traineeService).changeTraineePassword(updatedPassword, traineeId, username, password);
    }

    @Test
    public void activateTrainee_shouldDelegateToTraineeService() throws AuthenticationException {
        // Arrange
        Integer traineeId = 1;
        String username = "john.smith";
        String password = "password";

        // Act
        gymFacade.activateTrainee(traineeId, username, password);

        // Assert
        verify(traineeService).activateTrainee(traineeId, username, password);
    }

    @Test
    public void deActivateTrainee_shouldDelegateToTraineeService() throws AuthenticationException {
        // Arrange
        Integer traineeId = 1;
        String username = "john.smith";
        String password = "password";

        // Act
        gymFacade.deActivateTrainee(traineeId, username, password);

        // Assert
        verify(traineeService).deActivateTrainee(traineeId, username, password);
    }

    @Test
    public void getTrainingsByUsernameAndCriteria_shouldDelegateToTraineeService() {
        // Arrange
        String username = "john.smith";
        LocalDate fromDate = LocalDate.of(2026, 1, 1);
        LocalDate toDate = LocalDate.of(2026, 12, 31);
        String trainerName = "trainer";
        String trainingTypeName = "strength";

        List<TrainingDto> expectedTrainings = new ArrayList<>();
        when(traineeService.getTrainingsByUsernameAndCriteria(
                username,
                fromDate,
                toDate,
                trainerName,
                trainingTypeName))
                .thenReturn(expectedTrainings);

        // Act
        List<TrainingDto> result = gymFacade.getTrainingsByUsernameAndCriteria(
                username,
                fromDate,
                toDate,
                trainerName,
                trainingTypeName);

        // Assert
        assertEquals(expectedTrainings, result);
        verify(traineeService).getTrainingsByUsernameAndCriteria(
                username,
                fromDate,
                toDate,
                trainerName,
                trainingTypeName);
    }

    @Test
    public void updateTraineeTrainers_shouldDelegateToTraineeService() throws AuthenticationException {
        // Arrange
        String traineeUsername = "john.smith";
        List<String> trainerUsernames = Arrays.asList("trainer1", "trainer2");
        String username = "john.smith";
        String password = "password";

        // Act
        gymFacade.updateTraineeTrainers(traineeUsername, trainerUsernames, username, password);

        // Assert
        verify(traineeService).updateTraineeTrainers(traineeUsername, trainerUsernames, username, password);
    }

    @Test
    public void getTrainersNotAssignedOnTraineeByTraineeUsername_shouldDelegateToTrainerService() {
        // Arrange
        String username = "john.smith";
        List<TrainerDto> expectedTrainers = new ArrayList<>();
        TrainerDto trainerDto = new TrainerDto();
        trainerDto.setFirstName("Trainer");
        trainerDto.setLastName("One");
        expectedTrainers.add(trainerDto);

        when(trainerService.getTrainersNotAssignedOnTraineeByTraineeUsername(username))
                .thenReturn(expectedTrainers);

        // Act
        List<TrainerDto> result = gymFacade.getTrainersNotAssignedOnTraineeByTraineeUsername(username);

        // Assert
        assertEquals(expectedTrainers, result);
        verify(trainerService).getTrainersNotAssignedOnTraineeByTraineeUsername(username);
    }

    @Test
    public void changeTrainerPassword_shouldDelegateToTrainerService() throws AuthenticationException {
        // Arrange
        String updatedPassword = "newPassword";
        Integer trainerId = 1;
        String username = "john.smith";
        String password = "oldPassword";

        // Act
        gymFacade.changeTrainerPassword(updatedPassword, trainerId, username, password);

        // Assert
        verify(trainerService).changeTrainerPassword(updatedPassword, trainerId, username, password);
    }

    @Test
    public void activateTrainer_shouldDelegateToTrainerService() throws AuthenticationException {
        // Arrange
        Integer trainerId = 1;
        String username = "john.smith";
        String password = "password";

        // Act
        gymFacade.activateTrainer(trainerId, username, password);

        // Assert
        verify(trainerService).activateTrainer(trainerId, username, password);
    }

    @Test
    public void deActivateTrainer_shouldDelegateToTrainerService() throws AuthenticationException {
        // Arrange
        Integer trainerId = 1;
        String username = "john.smith";
        String password = "password";

        // Act
        gymFacade.deActivateTrainer(trainerId, username, password);

        // Assert
        verify(trainerService).deActivateTrainer(trainerId, username, password);
    }

    @Test
    public void getTrainingsByUsernameAndCriteria_shouldDelegateToTrainerService() {
        // Arrange
        String username = "john.smith";
        LocalDate fromDate = LocalDate.of(2026, 1, 1);
        LocalDate toDate = LocalDate.of(2026, 12, 31);
        String traineeName = "trainee";

        List<TrainingDto> expectedTrainings = new ArrayList<>();
        when(trainerService.getTrainingsByUsernameAndCriteria(
                username,
                fromDate,
                toDate,
                traineeName)
        ).thenReturn(expectedTrainings);

        // Act
        List<TrainingDto> result = gymFacade.getTrainingsByUsernameAndCriteria(
                username,
                fromDate,
                toDate,
                traineeName
        );

        // Assert
        assertEquals(expectedTrainings, result);
        verify(trainerService).getTrainingsByUsernameAndCriteria(
                username,
                fromDate,
                toDate,
                traineeName
        );
    }
}