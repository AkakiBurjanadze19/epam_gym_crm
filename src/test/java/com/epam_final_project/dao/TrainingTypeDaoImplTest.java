package com.epam_final_project.dao;

import com.epam_final_project.dao.impl.TrainingTypeDaoImpl;
import com.epam_final_project.domain.TrainingType;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@TestPropertySource(locations = "classpath:application.properties")
@Sql(scripts = {"/schema.sql", "/data.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Import(TrainingTypeDaoImpl.class)
@Transactional
public class TrainingTypeDaoImplTest {
    @PersistenceContext
    private EntityManager em;

    @Autowired
    private TrainingTypeDao trainingTypeDao;

    @Test
    public void findById_shouldReturnExistingTrainingType() {
        // Act
        Optional<TrainingType> trainingType = trainingTypeDao.findById(1);

        // Assert
        assertFalse(trainingType.isEmpty());
        assertEquals("Functional Training", trainingType.get().getTrainingTypeName());
    }

    @Test
    public void findAll_shouldReturnListContainingTrainingTypes() {
        // Act
        List<TrainingType> trainingTypes = trainingTypeDao.findAll();

        // Assert
        assertNotNull(trainingTypes);
        assertEquals(6, trainingTypes.size());
        assertEquals("Functional Training", trainingTypes.get(0).getTrainingTypeName());
        assertEquals("Mobility Training", trainingTypes.get(1).getTrainingTypeName());
        assertEquals("Strength Training", trainingTypes.get(2).getTrainingTypeName());
        assertEquals("Balance Training", trainingTypes.get(3).getTrainingTypeName());
        assertEquals("Stretching", trainingTypes.get(4).getTrainingTypeName());
        assertEquals("Agility Training", trainingTypes.get(5).getTrainingTypeName());
    }
}