package dao;

import com.epam_final_project.dao.impl.TrainingDaoImpl;
import com.epam_final_project.domain.Training;
import com.epam_final_project.domain.TrainingType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TrainingDaoImplTest {
    private TrainingDaoImpl trainingDao;
    private final Map<Integer, Training> trainings = new HashMap<>();

    @BeforeEach
    public void setUp() {
        trainingDao = new TrainingDaoImpl();
        trainingDao.setTrainings(trainings);
    }

    @Test
    public void create_shouldCreateTrainingSuccessfully() {
        Training training = new Training();
        training.setTrainerId(1);
        training.setName("Morning Cardio Blast");
        training.setType(TrainingType.CARDIO);
        training.setDate(LocalDate.of(2026, 9, 15));
        training.setDuration(Duration.ofHours(1).plusMinutes(30));

        trainingDao.create(training);

        assertEquals(1, training.getId());
        assertEquals(1, training.getTrainerId());
        assertEquals("Morning Cardio Blast", training.getName());
        assertEquals(TrainingType.CARDIO, training.getType());
        assertEquals(LocalDate.of(2026, 9, 15), training.getDate());
        assertEquals(Duration.ofHours(1).plusMinutes(30), training.getDuration());

        assertEquals(training, trainings.get(1));
    }

    @Test
    public void select_shouldReturnExistingTraining() {
        Training training = new Training();
        training.setTrainerId(1);
        training.setName("Morning Cardio Blast");
        training.setType(TrainingType.CARDIO);
        training.setDate(LocalDate.of(2026, 9, 15));
        training.setDuration(Duration.ofHours(1).plusMinutes(30));

        trainingDao.create(training);

        assertEquals(training, trainingDao.select(1).get());
    }
}
