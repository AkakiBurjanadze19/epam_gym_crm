package com.epam_final_project.service;

import com.epam_final_project.dao.TraineeDao;
import com.epam_final_project.dao.TrainerDao;
import com.epam_final_project.domain.Trainee;
import com.epam_final_project.service.impl.TraineeServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class TraineeServiceImplTest {
    @Mock
    private TraineeDao traineeDao;
    @Mock
    private TrainerDao trainerDao;

    @InjectMocks
    private TraineeServiceImpl traineeServiceImpl;

    @Test
    public void createTrainee_shouldCallDaoCreate() {
        Trainee trainee = new Trainee();

        traineeServiceImpl.createTrainee(trainee);

        verify(traineeDao).create(trainee);
    }

    @Test
    public void createTrainee_shouldGenerateUsernameAndPasswordWhenTheyAreNotProvided() {
        Trainee trainee = new Trainee();
        trainee.setFirstName("John");
        trainee.setLastName("Smith");

        traineeServiceImpl.createTrainee(trainee);

        assertEquals("John.Smith", trainee.getUsername());
        assertNotNull(trainee.getPassword());

        verify(traineeDao).create(trainee);
    }

    @Test
    public void updateTrainee_shouldCallDaoUpdate() {
        Trainee updated = new Trainee();
        Integer traineeId = 138;

        traineeServiceImpl.updateTrainee(updated, traineeId);

        verify(traineeDao).update(updated, traineeId);
    }

    @Test
    public void deleteTrainee_shouldCallDaoDelete() {
        Integer trainerId = 149;

        traineeServiceImpl.deleteTrainee(trainerId);

        verify(traineeDao).delete(trainerId);
    }

    @Test
    public void selectTrainee_shouldReturnDaoResult() {
        Trainee trainee = new Trainee();
        Integer traineeId = 867;

        when(traineeDao.findById(traineeId)).thenReturn(Optional.of(trainee));

        Optional<Trainee> result = traineeServiceImpl.selectTrainee(traineeId);

        assertEquals(Optional.of(trainee), result);
        verify(traineeDao).findById(traineeId);
    }
}
