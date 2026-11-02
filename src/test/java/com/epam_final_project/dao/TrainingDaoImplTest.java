package com.epam_final_project.dao;

import com.epam_final_project.dao.impl.TrainingDaoImpl;
import com.epam_final_project.dao.impl.TrainingTypeDaoImpl;
import com.epam_final_project.domain.Training;
import com.epam_final_project.domain.TrainingType;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.TestPropertySource;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@TestPropertySource(locations = "classpath:application.properties")
@Sql(scripts = {"/schema.sql", "/data.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Import({TrainingDaoImpl.class, TrainingTypeDaoImpl.class})
@Transactional
public class TrainingDaoImplTest {
    @PersistenceContext
    private EntityManager em;

    @Autowired
    private TrainingDao trainingDao;

    @Test
    public void create_shouldCreateTrainingSuccessfully() {
        // Arrange
        Training training = new Training();
        training.setName("Morning Cardio Blast");
        training.setType(new TrainingType("Strength Training"));
        training.setDate(LocalDate.of(2026, 9, 15));
        training.setDuration(1);

        // Act
        trainingDao.create(training);

        // Assert
        assertEquals("Morning Cardio Blast", training.getName());
        assertEquals("Strength Training", training.getType().getTrainingTypeName());
        assertEquals(LocalDate.of(2026, 9, 15), training.getDate());
        assertEquals(1, training.getDuration());
    }

    @Test
    public void findById_shouldReturnExistingTraining() {
        // Arrange
        Training training = new Training();
        training.setName("Morning Cardio Blast");
        training.setType(new TrainingType("Strength Training"));
        training.setDate(LocalDate.of(2026, 9, 15));
        training.setDuration(1);
        trainingDao.create(training);

        // Act
        Optional<Training> result = trainingDao.findById(training.getId());

        // Assert
        assertFalse(result.isEmpty());
        assertEquals("Morning Cardio Blast", training.getName());
        assertEquals("Strength Training", training.getType().getTrainingTypeName());
        assertEquals(LocalDate.of(2026, 9, 15), training.getDate());
        assertEquals(1, training.getDuration());
    }
}