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

import javax.naming.AuthenticationException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import com.epam_final_project.domain.Training;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.never;

@ExtendWith(MockitoExtension.class)
public class TraineeServiceImplTest {
    @Mock
    private TraineeDao traineeDao;
    @Mock
    private TrainerDao trainerDao;
    @Mock
    private AuthService authService;

    @InjectMocks
    private TraineeServiceImpl traineeServiceImpl;

    @Test
    public void createTrainee_shouldCallDaoCreate() {
        // Arrange
        Trainee trainee = new Trainee();
        trainee.setFirstName("Bob");
        trainee.setLastName("Smith");

        // Act
        traineeServiceImpl.createTrainee(trainee);

        // Assert
        verify(traineeDao).create(trainee);
    }

    @Test
    public void createTrainee_shouldGenerateUsernameAndPasswordWhenTheyAreNotProvided() {
        // Arrange
        Trainee trainee = new Trainee();
        trainee.setFirstName("John");
        trainee.setLastName("Smith");

        // Act
        traineeServiceImpl.createTrainee(trainee);

        // Assert
        assertEquals("John.Smith", trainee.getUsername());
        assertNotNull(trainee.getPassword());

        verify(traineeDao).create(trainee);
    }

    @Test
    public void updateTrainee_shouldThrowException_whenCredentialsAreInvalid() throws AuthenticationException {
        // Arrange
        Integer traineeId = 1;

        Trainee updated = getTrainee();

        when(authService.checkTraineeUsernameAndPassword(
                traineeId,
                "bla.bla",
                "blabla"))
                .thenReturn(false);

        // Act & Assert
        assertThrows(AuthenticationException.class, () -> {
            traineeServiceImpl.updateTrainee(
                    updated,
                    traineeId,
                    "bla.bla",
                    "blabla"
            );
        });

        verify(traineeDao, never()).update(any(Trainee.class), any(Integer.class));
    }

    @Test
    public void updateTrainee_shouldCallDaoUpdate_whenCredentialsAreValid() throws AuthenticationException {
        // Arrange
        Integer traineeId = 1;

        when(authService.checkTraineeUsernameAndPassword(
                traineeId,
                "john.smith",
                "password123"))
                .thenReturn(true);

        // Act
        traineeServiceImpl.updateTrainee(new Trainee(), traineeId, "john.smith", "password123");

        // Assert
        verify(traineeDao).update(any(Trainee.class), any(Integer.class));
    }

    @Test
    public void deleteTrainee_shouldCallDaoDelete() throws AuthenticationException {
        // Arrange
        Integer traineeId = 1;

        getTrainee();

        when(authService.checkTraineeUsernameAndPassword(
                traineeId,
                "john.smith",
                "password123"))
                .thenReturn(true);

        // Act
        traineeServiceImpl.deleteTrainee(traineeId, "john.smith", "password123");

        // Assert
        verify(traineeDao).delete(traineeId);
    }

    @Test
    public void deleteTrainee_shouldThrowExceptionWhenInvalidCredentials() throws AuthenticationException {
        // Arrange
        Integer traineeId = 1;

        when(authService.checkTraineeUsernameAndPassword(
                traineeId,
                "john.smith",
                "wrongpassword"))
                .thenReturn(false);

        // Act & Assert
        assertThrows(AuthenticationException.class, () -> {
            traineeServiceImpl.deleteTrainee(traineeId, "john.smith", "wrongpassword");
        });

        verify(traineeDao, never()).delete(traineeId);
    }

    @Test
    public void selectTrainee_shouldReturnDaoResult() {
        // Arrange
        Trainee trainee = new Trainee();
        Integer traineeId = 867;

        // Act
        when(traineeDao.findById(traineeId)).thenReturn(Optional.of(trainee));

        Optional<Trainee> result = traineeServiceImpl.selectTrainee(traineeId);

        // Assert
        assertEquals(Optional.of(trainee), result);
        verify(traineeDao).findById(traineeId);
    }

    @Test
    public void selectTraineeByUsername_shouldReturnDaoResult() {
        // Arrange
        Trainee trainee = getTrainee();

        when(traineeDao.findByUsername(trainee.getUsername())).thenReturn(Optional.of(trainee));

        // Act
        Optional<Trainee> foundTrainee = traineeServiceImpl.selectTraineeByUsername(trainee.getUsername());

        // Assert
        assertFalse(foundTrainee.isEmpty());
        assertEquals("John", foundTrainee.get().getFirstName());
        assertEquals("Smith", foundTrainee.get().getLastName());
    }

    @Test
    public void changeTraineePassword_shouldInvokeTraineeDao_findById()
            throws AuthenticationException {
        // Arrange
        Integer traineeId = 1;
        Trainee trainee = getTrainee();

        when(authService.checkTraineeUsernameAndPassword(
                traineeId,
                trainee.getUsername(),
                trainee.getPassword()))
                .thenReturn(true);

        // Act
        traineeServiceImpl.changeTraineePassword(
                "new_password",
                traineeId,
                trainee.getUsername(),
                trainee.getPassword()
                );

        // Assert
        verify(traineeDao).findById(traineeId);
    }

    @Test
    public void activateTrainee_shouldInvokeTraineeDao_findById() throws AuthenticationException {
        // Arrange
        Integer traineeId = 1;
        Trainee trainee = getTrainee();
        trainee.setActive(true);

        when(authService.checkTraineeUsernameAndPassword(
                traineeId,
                trainee.getUsername(),
                trainee.getPassword()))
                .thenReturn(true);

        // Act
        traineeServiceImpl.activateTrainee(traineeId, trainee.getUsername(), trainee.getPassword());

        // Assert
        verify(traineeDao).findById(traineeId);
    }

    @Test
    public void deActivateTrainee_shouldInvokeTraineeDao_findById() throws AuthenticationException {
        // Arrange
        Integer traineeId = 1;
        Trainee trainee = getTrainee();
        trainee.setActive(false);

        when(authService.checkTraineeUsernameAndPassword(
                traineeId,
                trainee.getUsername(),
                trainee.getPassword()))
                .thenReturn(true);

        // Act
        traineeServiceImpl.deActivateTrainee(traineeId, trainee.getUsername(), trainee.getPassword());

        // Assert
        verify(traineeDao).findById(traineeId);
    }

    @Test
    public void deActivateTrainee_shouldSetActiveToFalse() throws AuthenticationException {
        // Arrange
        Integer traineeId = 1;
        Trainee trainee = getTrainee();
        trainee.setActive(true);

        when(authService.checkTraineeUsernameAndPassword(
                traineeId,
                trainee.getUsername(),
                trainee.getPassword()))
                .thenReturn(true);
        when(traineeDao.findById(traineeId)).thenReturn(Optional.of(trainee));

        // Act
        traineeServiceImpl.deActivateTrainee(traineeId, trainee.getUsername(), trainee.getPassword());

        // Assert
        assertFalse(trainee.isActive());
        verify(traineeDao).findById(traineeId);
    }

    @Test
    public void deleteTraineeByUsername_shouldCallDao_deleteTraineeByUsername() {
        // Arrange
        Trainee trainee = getTrainee();

        // Act
        traineeServiceImpl.deleteTraineeByUsername(trainee.getUsername());

        // Assert
        verify(traineeDao).deleteByUsername(trainee.getUsername());
    }

    @Test
    public void createTrainee_shouldThrowExceptionWhenFirstNameIsNull() {
        // Arrange
        Trainee trainee = new Trainee();
        trainee.setFirstName(null);
        trainee.setLastName("Smith");

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            traineeServiceImpl.createTrainee(trainee);
        });
    }

    @Test
    public void createTrainee_shouldThrowExceptionWhenFirstNameIsEmpty() {
        // Arrange
        Trainee trainee = new Trainee();
        trainee.setFirstName("");
        trainee.setLastName("Smith");

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            traineeServiceImpl.createTrainee(trainee);
        });
    }

    @Test
    public void createTrainee_shouldThrowExceptionWhenLastNameIsNull() {
        // Arrange
        Trainee trainee = new Trainee();
        trainee.setFirstName("John");
        trainee.setLastName(null);

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            traineeServiceImpl.createTrainee(trainee);
        });
    }

    @Test
    public void createTrainee_shouldThrowExceptionWhenLastNameIsEmpty() {
        // Arrange
        Trainee trainee = new Trainee();
        trainee.setFirstName("John");
        trainee.setLastName("");

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            traineeServiceImpl.createTrainee(trainee);
        });
    }

    @Test
    public void changeTraineePassword_shouldChangePassword() throws AuthenticationException {
        // Arrange
        Integer traineeId = 1;
        Trainee trainee = getTrainee();
        String oldPassword = trainee.getPassword();
        String newPassword = "newPassword123";

        when(authService.checkTraineeUsernameAndPassword(
                traineeId,
                trainee.getUsername(),
                oldPassword))
                .thenReturn(true);
        when(traineeDao.findById(traineeId)).thenReturn(Optional.of(trainee));

        // Act
        traineeServiceImpl.changeTraineePassword(newPassword, traineeId, trainee.getUsername(), oldPassword);

        // Assert
        assertEquals(newPassword, trainee.getPassword());
        verify(traineeDao).findById(traineeId);
    }

    @Test
    public void activateTrainee_shouldSetActiveToTrue() throws AuthenticationException {
        // Arrange
        Integer traineeId = 1;
        Trainee trainee = getTrainee();
        trainee.setActive(false);

        when(authService.checkTraineeUsernameAndPassword(
                traineeId,
                trainee.getUsername(),
                trainee.getPassword()))
                .thenReturn(true);
        when(traineeDao.findById(traineeId)).thenReturn(Optional.of(trainee));

        // Act
        traineeServiceImpl.activateTrainee(traineeId, trainee.getUsername(), trainee.getPassword());

        // Assert
        assertTrue(trainee.isActive());
        verify(traineeDao).findById(traineeId);
    }

    @Test
    public void getTrainingsByUsernameAndCriteria_shouldCallDaoMethod() {
        // Arrange
        String username = "john.smith";
        LocalDate fromDate = LocalDate.of(2026, 1, 1);
        LocalDate toDate = LocalDate.of(2026, 12, 31);
        String trainerName = "trainer";
        String trainingTypeName = "strength";

        List<Training> expectedTrainings = new ArrayList<>();
        when(traineeDao.findTrainingsByUsernameAndCriteria(
                username,
                fromDate,
                toDate,
                trainerName,
                trainingTypeName))
                .thenReturn(expectedTrainings);

        // Act
        List<Training> result = traineeServiceImpl.getTrainingsByUsernameAndCriteria(
                username,
                fromDate,
                toDate,
                trainerName,
                trainingTypeName);

        // Assert
        assertEquals(expectedTrainings, result);
        verify(traineeDao).findTrainingsByUsernameAndCriteria(
                username,
                fromDate,
                toDate,
                trainerName,
                trainingTypeName);
    }

    @Test
    public void updateTraineeTrainers_shouldCallDaoMethod() {
        // Arrange
        Integer traineeId = 1;
        List<Integer> trainerIds = Arrays.asList(1, 2, 3);

        // Act
        traineeServiceImpl.updateTraineeTrainers(traineeId, trainerIds);

        // Assert
        verify(traineeDao).updateTrainers(traineeId, trainerIds);
    }

    private Trainee getTrainee() {
        Trainee trainee = new Trainee();
        trainee.setFirstName("John");
        trainee.setLastName("Smith");
        trainee.setUsername("john.smith");
        trainee.setPassword("password123");
        trainee.setActive(true);
        trainee.setDateOfBirth(LocalDate.of(2005, 12, 3));
        trainee.setAddress("Rustaveli N12");
        return trainee;
    }
}
