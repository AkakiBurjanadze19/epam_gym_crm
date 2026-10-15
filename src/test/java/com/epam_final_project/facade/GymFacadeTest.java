package com.epam_final_project.facade;

import com.epam_final_project.domain.Trainee;
import com.epam_final_project.domain.Trainer;
import com.epam_final_project.domain.Training;
import com.epam_final_project.service.AuthService;
import com.epam_final_project.service.TraineeService;
import com.epam_final_project.service.TrainerService;
import com.epam_final_project.service.TrainingService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;

import javax.naming.AuthenticationException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;
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
        Trainee trainee = new Trainee();

        // Act
        gymFacade.createTrainee(trainee);

        // Assert
        verify(traineeService).createTrainee(trainee);
    }

    @Test
    public void updateTrainee_shouldDelegateToTraineeService() throws AuthenticationException {
        // Arrange
        Trainee updated = new Trainee();
        Integer traineeId = 10;

        // Act
        gymFacade.updateTrainee(updated, traineeId, "john.smith", "password");

        // Assert
        verify(traineeService).updateTrainee(updated, traineeId, "john.smith", "password");
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
        Trainee trainee = new Trainee();
        Integer traineeId = 10;

        when(traineeService.selectTrainee(traineeId)).thenReturn(Optional.of(trainee));

        // Act
        Optional<Trainee> result = gymFacade.selectTrainee(traineeId);

        // Assert
        assertEquals(Optional.of(trainee), result);
        verify(traineeService).selectTrainee(traineeId);
    }

    @Test
    public void createTrainer_shouldDelegateToTrainerService() {
        // Arrange
        Trainer trainer = new Trainer();

        // Act
        gymFacade.createTrainer(trainer);

        // Assert
        verify(trainerService).createTrainer(trainer);
    }

    @Test
    public void updateTrainer_shouldDelegateToTrainerService() throws AuthenticationException {
        // Arrange
        Trainer updated = new Trainer();
        Integer trainerId = 10;

        // Act
        gymFacade.updateTrainer(updated, trainerId, "john.smith", "password");

        // Assert
        verify(trainerService).updateTrainer(updated, trainerId, "john.smith", "password");
    }

    @Test
    public void selectTrainer_shouldDelegateToTrainerService() {
        // Arrange
        Trainer trainer = new Trainer();
        Integer trainerId = 139;

        when(trainerService.selectTrainer(trainerId)).thenReturn(Optional.of(trainer));

        // Act
        Optional<Trainer> result = gymFacade.selectTrainer(trainerId);

        // Assert
        assertEquals(Optional.of(trainer), result);
        verify(trainerService).selectTrainer(trainerId);
    }

    @Test
    public void createTraining_shouldDelegateToTrainingService() {
        // Arrange
        Training training = new Training();

        // Act
        gymFacade.createTraining(training);

        // Assert
        verify(trainingService).createTraining(training);
    }

    @Test
    public void selectTraining_shouldDelegateToTrainingService() {
        // Arrange
        Training training = new Training();
        Integer trainingId = 789;

        when(trainingService.selectTraining(trainingId)).thenReturn(Optional.of(training));

        // Act
        Optional<Training> result = gymFacade.selectTraining(trainingId);

        // Assert
        assertEquals(Optional.of(training), result);
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

        List<Training> expectedTrainings = new ArrayList<>();
        when(traineeService.getTrainingsByUsernameAndCriteria(
                username,
                fromDate,
                toDate,
                trainerName,
                trainingTypeName))
                .thenReturn(expectedTrainings);

        // Act
        List<Training> result = gymFacade.getTrainingsByUsernameAndCriteria(
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
    public void updateTraineeTrainers_shouldDelegateToTraineeService() {
        // Arrange
        Integer traineeId = 1;
        List<Integer> trainerIds = Arrays.asList(1, 2, 3);

        // Act
        gymFacade.updateTraineeTrainers(traineeId, trainerIds);

        // Assert
        verify(traineeService).updateTraineeTrainers(traineeId, trainerIds);
    }

    @Test
    public void getTrainersNotAssignedOnTraineeByTraineeUsername_shouldDelegateToTrainerService() {
        // Arrange
        String username = "john.smith";
        List<Trainer> expectedTrainers = new ArrayList<>();
        expectedTrainers.add(new Trainer());

        when(trainerService.getTrainersNotAssignedOnTraineeByTraineeUsername(username))
                .thenReturn(expectedTrainers);

        // Act
        List<Trainer> result = gymFacade.getTrainersNotAssignedOnTraineeByTraineeUsername(username);

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

        List<Training> expectedTrainings = new ArrayList<>();
        when(trainerService.getTrainingsByUsernameAndCriteria(
                username,
                fromDate,
                toDate,
                traineeName)
        ).thenReturn(expectedTrainings);

        // Act
        List<Training> result = gymFacade.getTrainingsByUsernameAndCriteria(
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