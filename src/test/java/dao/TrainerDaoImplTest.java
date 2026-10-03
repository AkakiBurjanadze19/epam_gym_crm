package dao;

import com.epam_final_project.dao.impl.TrainerDaoImpl;
import com.epam_final_project.domain.Trainer;
import com.epam_final_project.domain.TrainingType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public class TrainerDaoImplTest {
    private TrainerDaoImpl trainerDao;
    private final Map<Integer, Trainer> trainers = new HashMap<>();

    @BeforeEach
    public void setUp() {
        trainerDao = new TrainerDaoImpl();
        trainerDao.setTrainers(trainers);
    }

    @Test
    public void create_shouldCreateTrainerSuccessfully() {
        Trainer trainer = new Trainer();
        trainer.setFirstName("Maxime");
        trainer.setLastName("Dvali");
        trainer.setUsername("Maxime.Dvali");
        trainer.setActive(true);
        trainer.setSpecialization(TrainingType.STRENGTH);

        trainerDao.create(trainer);

        assertEquals(1, trainer.getUserId());
        assertEquals("Maxime", trainer.getFirstName());
        assertEquals("Dvali", trainer.getLastName());
        assertEquals("Maxime.Dvali", trainer.getUsername());
        assertNotNull(trainer.getPassword());
        assertTrue(trainer.isActive());
        assertEquals(TrainingType.STRENGTH, trainer.getSpecialization());

        assertEquals(trainer, trainers.get(1));
    }

    @Test
    public void create_shouldGenerateUniqueUsername() {
        Trainer existing = new Trainer();
        existing.setUserId(1);
        existing.setFirstName("Max");
        existing.setLastName("Payne");
        existing.setUsername("Max.Payne");

        trainers.put(1, existing);

        Trainer newTrainer = new Trainer();
        newTrainer.setFirstName("Max");
        newTrainer.setLastName("Payne");

        trainerDao.create(newTrainer);

        assertEquals(2, newTrainer.getUserId());
        assertEquals("Max.Payne1", newTrainer.getUsername());
        assertSame(newTrainer, trainers.get(2));
    }

    @Test
    public void select_shouldReturnExistingTrainer() {
        Trainer existing = new Trainer();
        existing.setUserId(1);
        existing.setFirstName("Maxime");
        existing.setLastName("Dvali");

        trainers.put(1, existing);

        Optional<Trainer> result = trainerDao.findById(1);

        assertTrue(result.isPresent());
    }

    @Test
    public void select_shouldReturnEmptyWhenTrainerNotFound() {
        Optional<Trainer> result = trainerDao.findById(123);

        assertTrue(result.isEmpty());
    }

    @Test
    public void update_shouldUpdateTrainerFields() {
        Trainer existing = new Trainer();
        existing.setUserId(1);
        existing.setFirstName("Maxime");
        existing.setLastName("Dvali");
        existing.setUsername("Maxime.Dvali");
        existing.setPassword("currentPassword");
        existing.setActive(false);
        existing.setSpecialization(TrainingType.CARDIO);

        trainers.put(1, existing);

        Trainer updatedTrainer = new Trainer();
        updatedTrainer.setFirstName("Joel");
        updatedTrainer.setLastName("Miller");
        updatedTrainer.setUsername("Joel.Miller");
        updatedTrainer.setPassword("newPassword");
        updatedTrainer.setActive(true);
        updatedTrainer.setSpecialization(TrainingType.YOGA);

        trainerDao.update(updatedTrainer, 1);

        assertEquals("Joel", existing.getFirstName());
        assertEquals("Miller", existing.getLastName());
        assertEquals("Joel.Miller", existing.getUsername());
        assertEquals("newPassword", existing.getPassword());
        assertTrue(existing.isActive());
        assertEquals(TrainingType.YOGA, existing.getSpecialization());
    }

    @Test
    public void update_shouldThrowIllegalStateExceptionWhenTrainerEmpty() {
        Trainer updated = new Trainer();

        assertThrows(
                IllegalStateException.class,
                () -> trainerDao.update(updated, 200)
        );
    }
}