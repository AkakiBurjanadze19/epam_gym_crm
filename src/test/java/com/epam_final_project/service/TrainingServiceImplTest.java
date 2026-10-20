package com.epam_final_project.service;

import com.epam_final_project.dao.TraineeDao;
import com.epam_final_project.dao.TrainerDao;
import com.epam_final_project.dao.TrainingDao;
import com.epam_final_project.domain.Training;
import com.epam_final_project.domain.Trainer;
import com.epam_final_project.domain.Trainee;
import com.epam_final_project.dto.CreateTrainingRequest;
import com.epam_final_project.dto.TrainingDto;
import com.epam_final_project.mapper.TrainingMapper;
import com.epam_final_project.service.impl.TrainingServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.any;

@ExtendWith(MockitoExtension.class)
public class TrainingServiceImplTest {
    @Mock
    private TrainingDao trainingDao;
    @Mock
    private TraineeDao traineeDao;
    @Mock
    private TrainerDao trainerDao;
    @Mock
    private TrainingMapper trainingMapper;

    @InjectMocks
    private TrainingServiceImpl trainingServiceImpl;

    @Test
    public void createTraining_shouldCallDaoCreate() {
        // Arrange
        CreateTrainingRequest request = new CreateTrainingRequest();
        request.setTraineeUsername("john.smith");
        request.setTrainerUsername("trainer.jane");
        request.setName("Practicing Strength");
        request.setDate(LocalDate.of(2026, 11, 5));
        request.setDuration(1);

        Trainee trainee = new Trainee();
        Trainer trainer = new Trainer();
        trainer.setSpecialization(new com.epam_final_project.domain.TrainingType());

        Training training = new Training();
        training.setName("Practicing Strength");
        training.setDate(LocalDate.of(2026, 11, 5));
        training.setDuration(1);

        when(traineeDao.findByUsername("john.smith")).thenReturn(Optional.of(trainee));
        when(trainerDao.findByUsername("trainer.jane")).thenReturn(Optional.of(trainer));
        when(trainingMapper.toEntity(request)).thenReturn(training);

        // Act
        trainingServiceImpl.createTraining(request);

        // Assert
        verify(trainingDao).create(any(Training.class));
    }

    @Test
    public void selectTraining_shouldReturnDaoResult() {
        // Arrange
        Integer trainingId = 219;
        Training training = new Training();
        training.setName("Test Training");

        TrainingDto trainingDto = new TrainingDto();
        trainingDto.setName("Test Training");

        when(trainingDao.findById(trainingId)).thenReturn(Optional.of(training));
        when(trainingMapper.toDto(training)).thenReturn(trainingDto);

        // Act
        Optional<TrainingDto> result = trainingServiceImpl.selectTraining(trainingId);

        // Assert
        assertTrue(result.isPresent());
        assertEquals("Test Training", result.get().getName());
        verify(trainingDao).findById(trainingId);
    }

    @Test
    public void createTraining_shouldThrowExceptionWhenNameIsNull() {
        // Arrange
        CreateTrainingRequest request = new CreateTrainingRequest();
        request.setTraineeUsername("john.smith");
        request.setTrainerUsername("trainer.jane");
        request.setName(null);
        request.setDate(LocalDate.of(2026, 11, 5));
        request.setDuration(1);

        Trainee trainee = new Trainee();
        Trainer trainer = new Trainer();
        trainer.setSpecialization(new com.epam_final_project.domain.TrainingType());

        when(traineeDao.findByUsername("john.smith")).thenReturn(Optional.of(trainee));
        when(trainerDao.findByUsername("trainer.jane")).thenReturn(Optional.of(trainer));
        when(trainingMapper.toEntity(request)).thenReturn(new Training());

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            trainingServiceImpl.createTraining(request);
        });
    }

    @Test
    public void createTraining_shouldThrowExceptionWhenNameIsEmpty() {
        // Arrange
        CreateTrainingRequest request = new CreateTrainingRequest();
        request.setTraineeUsername("john.smith");
        request.setTrainerUsername("trainer.jane");
        request.setName("");
        request.setDate(LocalDate.of(2026, 11, 5));
        request.setDuration(1);

        Trainee trainee = new Trainee();
        Trainer trainer = new Trainer();
        trainer.setSpecialization(new com.epam_final_project.domain.TrainingType());

        when(traineeDao.findByUsername("john.smith")).thenReturn(Optional.of(trainee));
        when(trainerDao.findByUsername("trainer.jane")).thenReturn(Optional.of(trainer));
        when(trainingMapper.toEntity(request)).thenReturn(new Training());

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            trainingServiceImpl.createTraining(request);
        });
    }

    @Test
    public void createTraining_shouldThrowExceptionWhenDateIsNull() {
        // Arrange
        CreateTrainingRequest request = new CreateTrainingRequest();
        request.setTraineeUsername("john.smith");
        request.setTrainerUsername("trainer.jane");
        request.setName("Valid Training");
        request.setDate(null);
        request.setDuration(1);

        Trainee trainee = new Trainee();
        Trainer trainer = new Trainer();
        trainer.setSpecialization(new com.epam_final_project.domain.TrainingType());

        when(traineeDao.findByUsername("john.smith")).thenReturn(Optional.of(trainee));
        when(trainerDao.findByUsername("trainer.jane")).thenReturn(Optional.of(trainer));
        when(trainingMapper.toEntity(request)).thenReturn(new Training());

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            trainingServiceImpl.createTraining(request);
        });
    }

    @Test
    public void createTraining_shouldThrowExceptionWhenDurationIsZero() {
        // Arrange
        CreateTrainingRequest request = new CreateTrainingRequest();
        request.setTraineeUsername("john.smith");
        request.setTrainerUsername("trainer.jane");
        request.setName("Valid Training");
        request.setDate(LocalDate.of(2026, 11, 5));
        request.setDuration(0);

        Trainee trainee = new Trainee();
        Trainer trainer = new Trainer();
        trainer.setSpecialization(new com.epam_final_project.domain.TrainingType());

        when(traineeDao.findByUsername("john.smith")).thenReturn(Optional.of(trainee));
        when(trainerDao.findByUsername("trainer.jane")).thenReturn(Optional.of(trainer));
        when(trainingMapper.toEntity(request)).thenReturn(new Training());

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            trainingServiceImpl.createTraining(request);
        });
    }

    @Test
    public void createTraining_shouldThrowExceptionWhenDurationIsNegative() {
        // Arrange
        CreateTrainingRequest request = new CreateTrainingRequest();
        request.setTraineeUsername("john.smith");
        request.setTrainerUsername("trainer.jane");
        request.setName("Valid Training");
        request.setDate(LocalDate.of(2026, 11, 5));
        request.setDuration(-1);

        Trainee trainee = new Trainee();
        Trainer trainer = new Trainer();
        trainer.setSpecialization(new com.epam_final_project.domain.TrainingType());

        when(traineeDao.findByUsername("john.smith")).thenReturn(Optional.of(trainee));
        when(trainerDao.findByUsername("trainer.jane")).thenReturn(Optional.of(trainer));
        when(trainingMapper.toEntity(request)).thenReturn(new Training());

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            trainingServiceImpl.createTraining(request);
        });
    }
}