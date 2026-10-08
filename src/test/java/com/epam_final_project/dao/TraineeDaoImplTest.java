package com.epam_final_project.dao;

import com.epam_final_project.dao.impl.TraineeDaoImpl;
import com.epam_final_project.domain.Trainee;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public class TraineeDaoImplTest {
    private TraineeDaoImpl traineeDao;
    private final Map<Integer, Trainee> trainees = new HashMap<>();

    @BeforeEach
    public void setUp() {
        traineeDao = new TraineeDaoImpl();
        traineeDao.setTrainees(trainees);
    }

    @Test
    public void create_shouldCreateTraineeSuccessfully() {
        Trainee trainee = new Trainee();
        trainee.setFirstName("John");
        trainee.setLastName("Smith");
        trainee.setActive(true);
        trainee.setDateOfBirth(LocalDate.of(2005, 12, 3));
        trainee.setAddress("Rustaveli N12");

        traineeDao.create(trainee);

        assertEquals(1, trainee.getUserId());
        assertEquals("John", trainee.getFirstName());
        assertEquals("Smith", trainee.getLastName());
        assertTrue(trainee.isActive());
        assertEquals(LocalDate.of(2005, 12, 3), trainee.getDateOfBirth());
        assertEquals("Rustaveli N12", trainee.getAddress());

        assertEquals(trainee, trainees.get(1));
    }

    @Test
    public void select_shouldReturnExistingTrainee() {
        Trainee existing = new Trainee();
        existing.setUserId(1);
        existing.setFirstName("John");
        existing.setLastName("Smith");

        trainees.put(1, existing);

        Optional<Trainee> result = traineeDao.findById(1);

        assertTrue(result.isPresent());
    }

    @Test
    public void select_shouldReturnEmptyWhenTraineeNotFound() {
        Optional<Trainee> result = traineeDao.findById(999);

        assertTrue(result.isEmpty());
    }

    @Test
    public void update_shouldUpdateTraineeFields() {
        Trainee existing = new Trainee();
        existing.setUserId(1);
        existing.setFirstName("John");
        existing.setLastName("Smith");
        existing.setUsername("John.Smith");
        existing.setPassword("currentPassword");
        existing.setActive(true);
        existing.setDateOfBirth(LocalDate.of(2005, 12, 3));
        existing.setAddress("Rustaveli N12");

        trainees.put(1, existing);

        Trainee updatedTrainee = new Trainee();
        updatedTrainee.setFirstName("Nikoloz");
        updatedTrainee.setLastName("Burduli");
        updatedTrainee.setUsername("Nikoloz.Burduli");
        updatedTrainee.setPassword("newPassword");
        updatedTrainee.setActive(false);
        updatedTrainee.setDateOfBirth(LocalDate.of(2002, 10, 12));
        updatedTrainee.setAddress("Axalgazrdoba N5");

        traineeDao.update(updatedTrainee, 1);

        assertEquals("Nikoloz", existing.getFirstName());
        assertEquals("Burduli", existing.getLastName());
        assertEquals("Nikoloz.Burduli", existing.getUsername());
        assertEquals("newPassword", existing.getPassword());
        assertFalse(existing.isActive());
        assertEquals(LocalDate.of(2002, 10, 12), existing.getDateOfBirth());
        assertEquals("Axalgazrdoba N5", existing.getAddress());
    }

    @Test
    public void update_shouldThrowIllegalStateExceptionWhenTraineeEmpty() {
        Trainee updated = new Trainee();

        assertThrows(
                IllegalStateException.class,
                () -> traineeDao.update(updated, 299)
        );
    }

    @Test
    public void delete_shouldDeleteTraineeSuccessfully() {
        Trainee trainee = new Trainee();
        trainee.setFirstName("John");
        trainee.setLastName("Smith");

        traineeDao.create(trainee);

        traineeDao.delete(1);

        assertNull(trainees.get(1));
    }
}