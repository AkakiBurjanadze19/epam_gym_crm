package com.epam_final_project.service;

import com.epam_final_project.dao.TrainingDao;
import com.epam_final_project.domain.Training;
import com.epam_final_project.service.impl.TrainingServiceImpl;
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
public class TrainingServiceImplTest {
    @Mock
    private TrainingDao trainingDao;

    @InjectMocks
    private TrainingServiceImpl trainingServiceImpl;

    @Test
    public void createTraining_shouldCallDaoCreate() {
        Training training = new Training();

        trainingServiceImpl.createTraining(training);

        verify(trainingDao).create(training);
    }

    @Test
    public void selectTraining_shouldReturnDaoResult() {
        Training training = new Training();
        Integer trainingId = 219;

        when(trainingDao.findById(trainingId)).thenReturn(Optional.of(training));

        Optional<Training> result = trainingServiceImpl.selectTraining(trainingId);

        assertEquals(Optional.of(training), result);
        verify(trainingDao).findById(trainingId);
    }
}
