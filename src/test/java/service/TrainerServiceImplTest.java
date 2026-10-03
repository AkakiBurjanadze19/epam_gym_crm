package service;

import com.epam_final_project.dao.TrainerDao;
import com.epam_final_project.domain.Trainer;
import com.epam_final_project.service.impl.TrainerServiceImpl;
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
public class TrainerServiceImplTest {
    @Mock
    private TrainerDao trainerDao;

    @InjectMocks
    private TrainerServiceImpl trainerServiceImpl;

    @Test
    public void createTrainer_shouldCallDaoCreate() {
        Trainer trainer = new Trainer();

        trainerServiceImpl.createTrainer(trainer);

        verify(trainerDao).create(trainer);
    }

    @Test
    public void updateTrainer_shouldCallDaoUpdate() {
        Trainer updated = new Trainer();
        Integer trainerId = 10;

        trainerServiceImpl.updateTrainer(updated, trainerId);

        verify(trainerDao).update(updated, trainerId);
    }

    @Test
    public void selectTrainer_shouldReturnDaoResult() {
        Trainer trainer = new Trainer();
        Integer trainerId = 10;

        when(trainerDao.findById(trainerId)).thenReturn(Optional.of(trainer));

        Optional<Trainer> result = trainerServiceImpl.selectTrainer(trainerId);

        assertEquals(result, Optional.of(trainer));
        verify(trainerDao).findById(trainerId);
    }
}