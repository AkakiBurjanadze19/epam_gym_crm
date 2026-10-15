package com.epam_final_project.service;

import com.epam_final_project.dao.TrainingDao;
import com.epam_final_project.domain.Training;
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
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class TrainingServiceImplTest {
    @Mock
    private TrainingDao trainingDao;

    @InjectMocks
    private TrainingServiceImpl trainingServiceImpl;

    @Test
    public void createTraining_shouldCallDaoCreate() {
        // Arrange
        Training training = new Training();
        training.setName("Practicing Strength");
        training.setDate(LocalDate.of(2026, 11, 5));
        training.setDuration(1);

        // Act
        trainingServiceImpl.createTraining(training);

        // Assert
        verify(trainingDao).create(training);
    }

    @Test
    public void selectTraining_shouldReturnDaoResult() {
        // Arrange
        Training training = new Training();
        Integer trainingId = 219;

        when(trainingDao.findById(trainingId)).thenReturn(Optional.of(training));

        // Act
        Optional<Training> result = trainingServiceImpl.selectTraining(trainingId);

        // Assert
        assertEquals(Optional.of(training), result);
        verify(trainingDao).findById(trainingId);
    }

    @Test
    public void createTraining_shouldThrowExceptionWhenNameIsNull() {
        // Arrange
        Training training = new Training();
        training.setName(null);
        training.setDate(LocalDate.of(2026, 11, 5));
        training.setDuration(1);

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            trainingServiceImpl.createTraining(training);
        });
    }

    @Test
    public void createTraining_shouldThrowExceptionWhenNameIsEmpty() {
        // Arrange
        Training training = new Training();
        training.setName("");
        training.setDate(LocalDate.of(2026, 11, 5));
        training.setDuration(1);

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            trainingServiceImpl.createTraining(training);
        });
    }

    @Test
    public void createTraining_shouldThrowExceptionWhenDateIsNull() {
        // Arrange
        Training training = new Training();
        training.setName("Valid Training");
        training.setDate(null);
        training.setDuration(1);

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            trainingServiceImpl.createTraining(training);
        });
    }

    @Test
    public void createTraining_shouldThrowExceptionWhenDurationIsZero() {
        // Arrange
        Training training = new Training();
        training.setName("Valid Training");
        training.setDate(LocalDate.of(2026, 11, 5));
        training.setDuration(0);

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            trainingServiceImpl.createTraining(training);
        });
    }

    @Test
    public void createTraining_shouldThrowExceptionWhenDurationIsNegative() {
        // Arrange
        Training training = new Training();
        training.setName("Valid Training");
        training.setDate(LocalDate.of(2026, 11, 5));
        training.setDuration(-1);

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            trainingServiceImpl.createTraining(training);
        });
    }
}
