package com.epam_final_project.facade;

import com.epam_final_project.domain.Trainee;
import com.epam_final_project.domain.Trainer;
import com.epam_final_project.domain.Training;
import com.epam_final_project.facade.GymFacade;
import com.epam_final_project.service.TraineeService;
import com.epam_final_project.service.TrainerService;
import com.epam_final_project.service.TrainingService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

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
        Trainee trainee = new Trainee();

        gymFacade.createTrainee(trainee);

        verify(traineeService).createTrainee(trainee);
    }

    @Test
    public void updateTrainee_shouldDelegateToTraineeService() {
        Trainee updated = new Trainee();
        Integer traineeId = 10;

        gymFacade.updateTrainee(updated, traineeId);

        verify(traineeService).updateTrainee(updated, traineeId);
    }

    @Test
    public void deleteTrainee_shouldDelegateToTraineeService() {
        Integer traineeId = 100;

        gymFacade.deleteTrainee(traineeId);

        verify(traineeService).deleteTrainee(traineeId);
    }

    @Test
    public void selectTrainee_shouldDelegateToTraineeService() {
        Trainee trainee = new Trainee();
        Integer traineeId = 10;

        when(traineeService.selectTrainee(traineeId)).thenReturn(Optional.of(trainee));

        Optional<Trainee> result = gymFacade.selectTrainee(traineeId);

        assertEquals(Optional.of(trainee), result);
        verify(traineeService).selectTrainee(traineeId);
    }

    @Test
    public void createTrainer_shouldDelegateToTrainerService() {
        Trainer trainer = new Trainer();

        gymFacade.createTrainer(trainer);

        verify(trainerService).createTrainer(trainer);
    }

    @Test
    public void updateTrainer_shouldDelegateToTrainerService() {
        Trainer updated = new Trainer();
        Integer trainerId = 10;

        gymFacade.updateTrainer(updated, trainerId);

        verify(trainerService).updateTrainer(updated, trainerId);
    }

    @Test
    public void selectTrainer_shouldDelegateToTrainerService() {
        Trainer trainer = new Trainer();
        Integer trainerId = 139;

        when(trainerService.selectTrainer(trainerId)).thenReturn(Optional.of(trainer));

        Optional<Trainer> result = gymFacade.selectTrainer(trainerId);

        assertEquals(Optional.of(trainer), result);
        verify(trainerService).selectTrainer(trainerId);
    }

    @Test
    public void createTraining_shouldDelegateToTrainingService() {
        Training training = new Training();

        gymFacade.createTraining(training);

        verify(trainingService).createTraining(training);
    }

    @Test
    public void selectTraining_shouldDelegateToTrainingService() {
        Training training = new Training();
        Integer trainingId = 789;

        when(trainingService.selectTraining(trainingId)).thenReturn(Optional.of(training));

        Optional<Training> result = gymFacade.selectTraining(trainingId);

        assertEquals(Optional.of(training), result);
        verify(trainingService).selectTraining(trainingId);
    }
}
