package storage;

import com.epam_final_project.domain.Trainee;
import com.epam_final_project.domain.Trainer;
import com.epam_final_project.domain.Training;
import com.epam_final_project.storage.Storage;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class StorageTest {
    @Test
    public void should_CreateThreeStorageMaps() {
        try (var context = new AnnotationConfigApplicationContext(Storage.class)) {
            Map<Integer, Trainer> trainers = context.getBean("trainers", Map.class);
            Map<Integer, Trainee> trainees = context.getBean("trainees", Map.class);
            Map<Integer, Training> trainings = context.getBean("trainings", Map.class);

            assertNotNull(trainers);
            assertNotNull(trainees);
            assertNotNull(trainings);
        }
    }

    @Test
    public void storageMaps_shouldBeInitiallyEmpty() {
        try (var context = new AnnotationConfigApplicationContext(Storage.class)) {
            Map<Integer, Trainer> trainers = context.getBean("trainers", Map.class);
            Map<Integer, Trainee> trainees = context.getBean("trainees", Map.class);
            Map<Integer, Training> trainings = context.getBean("trainings", Map.class);

            assertTrue(trainers.isEmpty());
            assertTrue(trainees.isEmpty());
            assertTrue(trainings.isEmpty());
        }
    }
}
