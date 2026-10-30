package com.epam_final_project.facade;

import com.epam_final_project.dto.*;
import com.epam_final_project.dto.trainee.request.CreateTraineeRequest;
import com.epam_final_project.dto.trainee.request.UpdateTraineeProfileRequestDto;
import com.epam_final_project.dto.trainee.response.TraineeProfileDto;
import com.epam_final_project.dto.trainer.request.CreateTrainerRequest;
import com.epam_final_project.dto.trainer.request.UpdateTrainerProfileRequestDto;
import com.epam_final_project.dto.trainer.response.TrainerSummaryDto;
import com.epam_final_project.dto.training.request.CreateTrainingRequestDto;
import com.epam_final_project.dto.training.response.TraineeTrainingResponseDto;
import com.epam_final_project.dto.training.response.TrainerTrainingResponseDto;
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
        UpdateTraineeProfileRequestDto request = new UpdateTraineeProfileRequestDto();
        request.setFirstName("Jane");
        request.setLastName("Doe");
        String username = "john.smith";

        // Act
        gymFacade.updateTrainee(request, username, "password");

        // Assert
        verify(traineeService).updateTrainee(request, username, "password");
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
        TraineeProfileDto traineeProfileDto = new TraineeProfileDto();
        traineeProfileDto.setFirstName("John");
        traineeProfileDto.setLastName("Smith");

        when(traineeService.selectTrainee(traineeId)).thenReturn(Optional.of(traineeProfileDto));

        // Act
        Optional<TraineeProfileDto> result = gymFacade.selectTrainee(traineeId);

        // Assert
        assertEquals(Optional.of(traineeProfileDto), result);
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
        UpdateTrainerProfileRequestDto request = new UpdateTrainerProfileRequestDto();
        request.setFirstName("Jane");
        request.setLastName("Doe");
        String username = "john.smith";

        // Act
        gymFacade.updateTrainer(request, username, "password");

        // Assert
        verify(trainerService).updateTrainer(request, username, "password");
    }

    @Test
    public void selectTrainer_shouldDelegateToTrainerService() {
        // Arrange
        Integer trainerId = 139;
        TrainerSummaryDto trainerSummaryDto = new TrainerSummaryDto();
        trainerSummaryDto.setFirstName("John");
        trainerSummaryDto.setLastName("Smith");

        when(trainerService.selectTrainer(trainerId)).thenReturn(Optional.of(trainerSummaryDto));

        // Act
        Optional<TrainerSummaryDto> result = gymFacade.selectTrainer(trainerId);

        // Assert
        assertEquals(Optional.of(trainerSummaryDto), result);
        verify(trainerService).selectTrainer(trainerId);
    }

    @Test
    public void createTraining_shouldDelegateToTrainingService() throws AuthenticationException {
        // Arrange
        CreateTrainingRequestDto request = new CreateTrainingRequestDto();
        request.setTraineeUsername("john.smith");
        request.setTrainerUsername("trainer.jane");
        request.setTrainingName("Morning Yoga");
        request.setTrainingDate(LocalDate.now());
        request.setTrainingDuration(60);

        // Act
        gymFacade.createTraining(request, "john.smith", "password");

        // Assert
        verify(trainingService).createTraining(request, "john.smith", "password");
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
    public void getTrainingsByUsernameAndCriteria_shouldDelegateToTraineeService() throws AuthenticationException {
        // Arrange
        String username = "john.smith";
        LocalDate fromDate = LocalDate.of(2026, 1, 1);
        LocalDate toDate = LocalDate.of(2026, 12, 31);
        String trainerName = "trainer";
        String trainingTypeName = "strength";
        String authUsername = "john.smith";
        String authPassword = "password";

        List<TraineeTrainingResponseDto> expectedTrainings = new ArrayList<>();
        when(traineeService.getTrainingsByUsernameAndCriteria(
                username,
                fromDate,
                toDate,
                trainerName,
                trainingTypeName,
                authUsername,
                authPassword))
                .thenReturn(expectedTrainings);

        // Act
        List<TraineeTrainingResponseDto> result = gymFacade.getTrainingsByUsernameAndCriteria(
                username,
                fromDate,
                toDate,
                trainerName,
                trainingTypeName,
                authUsername,
                authPassword);

        // Assert
        assertEquals(expectedTrainings, result);
        verify(traineeService).getTrainingsByUsernameAndCriteria(
                username,
                fromDate,
                toDate,
                trainerName,
                trainingTypeName,
                authUsername,
                authPassword);
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
    public void getTrainersNotAssignedOnTraineeByTraineeUsername_shouldDelegateToTrainerService() throws AuthenticationException {
        // Arrange
        String username = "john.smith";
        String authUsername = "john.smith";
        String authPassword = "password";
        List<TrainerSummaryDto> expectedTrainers = new ArrayList<>();
        TrainerSummaryDto trainerSummaryDto = new TrainerSummaryDto();
        trainerSummaryDto.setFirstName("Trainer");
        trainerSummaryDto.setLastName("One");
        expectedTrainers.add(trainerSummaryDto);

        when(trainerService.getTrainersNotAssignedOnTraineeByTraineeUsername(username, authUsername, authPassword))
                .thenReturn(expectedTrainers);

        // Act
        List<TrainerSummaryDto> result = gymFacade.getTrainersNotAssignedOnTraineeByTraineeUsername(username, authUsername, authPassword);

        // Assert
        assertEquals(expectedTrainers, result);
        verify(trainerService).getTrainersNotAssignedOnTraineeByTraineeUsername(username, authUsername, authPassword);
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
    public void getTrainingsByUsernameAndCriteria_shouldDelegateToTrainerService() throws AuthenticationException {
        // Arrange
        String username = "john.smith";
        LocalDate fromDate = LocalDate.of(2026, 1, 1);
        LocalDate toDate = LocalDate.of(2026, 12, 31);
        String traineeName = "trainee";
        String authUsername = "john.smith";
        String authPassword = "password";

        List<TrainerTrainingResponseDto> expectedTrainings = new ArrayList<>();
        when(trainerService.getTrainingsByUsernameAndCriteria(
                username,
                fromDate,
                toDate,
                traineeName,
                authUsername,
                authPassword)
        ).thenReturn(expectedTrainings);

        // Act
        List<TrainerTrainingResponseDto> result = gymFacade.getTrainingsByUsernameAndCriteria(
                username,
                fromDate,
                toDate,
                traineeName,
                authUsername,
                authPassword
        );

        // Assert
        assertEquals(expectedTrainings, result);
        verify(trainerService).getTrainingsByUsernameAndCriteria(
                username,
                fromDate,
                toDate,
                traineeName,
                authUsername,
                authPassword
        );
    }
}