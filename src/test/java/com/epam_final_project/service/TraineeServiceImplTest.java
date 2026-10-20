package com.epam_final_project.service;

import com.epam_final_project.dao.TraineeDao;
import com.epam_final_project.dao.TrainerDao;
import com.epam_final_project.domain.Trainee;
import com.epam_final_project.dto.CreateTraineeRequest;
import com.epam_final_project.dto.TraineeDto;
import com.epam_final_project.dto.TrainingDto;
import com.epam_final_project.mapper.TraineeMapper;
import com.epam_final_project.mapper.TrainingMapper;
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
import static org.mockito.Mockito.argThat;
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
    @Mock
    private TraineeMapper traineeMapper;
    @Mock
    private TrainingMapper trainingMapper;

    @InjectMocks
    private TraineeServiceImpl traineeServiceImpl;

    @Test
    public void createTrainee_shouldCallDaoCreate() {
        // Arrange
        CreateTraineeRequest request = new CreateTraineeRequest();
        request.setFirstName("Bob");
        request.setLastName("Smith");
        request.setDateOfBirth(LocalDate.of(2000, 1, 1));
        request.setAddress("Test Address");

        Trainee trainee = new Trainee();
        trainee.setFirstName("Bob");
        trainee.setLastName("Smith");
        trainee.setDateOfBirth(LocalDate.of(2000, 1, 1));
        trainee.setAddress("Test Address");

        when(traineeMapper.toEntity(request)).thenReturn(trainee);

        // Act
        traineeServiceImpl.createTrainee(request);

        // Assert
        verify(traineeDao).create(any(Trainee.class));
    }

    @Test
    public void createTrainee_shouldGenerateUsernameAndPasswordWhenTheyAreNotProvided() {
        // Arrange
        CreateTraineeRequest request = new CreateTraineeRequest();
        request.setFirstName("John");
        request.setLastName("Smith");
        request.setDateOfBirth(LocalDate.of(2000, 1, 1));
        request.setAddress("Test Address");

        Trainee trainee = new Trainee();
        trainee.setFirstName("John");
        trainee.setLastName("Smith");
        trainee.setDateOfBirth(LocalDate.of(2000, 1, 1));
        trainee.setAddress("Test Address");

        when(traineeMapper.toEntity(request)).thenReturn(trainee);

        // Act
        traineeServiceImpl.createTrainee(request);

        // Assert
        verify(traineeDao).create(argThat(t ->
            "John.Smith".equals(t.getUsername()) && t.getPassword() != null
        ));
    }

    @Test
    public void updateTrainee_shouldThrowException_whenCredentialsAreInvalid() throws AuthenticationException {
        // Arrange
        Integer traineeId = 1;

        CreateTraineeRequest updated = new CreateTraineeRequest();
        updated.setFirstName("John");
        updated.setLastName("Smith");
        updated.setDateOfBirth(LocalDate.of(2000, 1, 1));
        updated.setAddress("Test Address");

        when(authService.checkTraineeUsernameAndPassword(
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
                "john.smith",
                "password123"))
                .thenReturn(true);

        CreateTraineeRequest request = new CreateTraineeRequest();
        request.setFirstName("John");
        request.setLastName("Smith");
        request.setDateOfBirth(LocalDate.of(2000, 1, 1));
        request.setAddress("Test Address");

        Trainee trainee = new Trainee();
        trainee.setFirstName("John");
        trainee.setLastName("Smith");
        trainee.setDateOfBirth(LocalDate.of(2000, 1, 1));
        trainee.setAddress("Test Address");

        when(traineeMapper.toEntity(request)).thenReturn(trainee);

        // Act
        traineeServiceImpl.updateTrainee(request, traineeId, "john.smith", "password123");

        // Assert
        verify(traineeDao).update(any(Trainee.class), any(Integer.class));
    }

    @Test
    public void deleteTrainee_shouldCallDaoDelete() throws AuthenticationException {
        // Arrange
        Integer traineeId = 1;

        when(authService.checkTraineeUsernameAndPassword(
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
        trainee.setFirstName("John");
        trainee.setLastName("Smith");
        Integer traineeId = 867;

        TraineeDto traineeDto = new TraineeDto();
        traineeDto.setFirstName("John");
        traineeDto.setLastName("Smith");

        when(traineeDao.findById(traineeId)).thenReturn(Optional.of(trainee));
        when(traineeMapper.toDto(trainee)).thenReturn(traineeDto);

        // Act
        Optional<TraineeDto> result = traineeServiceImpl.selectTrainee(traineeId);

        // Assert
        assertNotNull(result.get().getFirstName());
        verify(traineeDao).findById(traineeId);
    }

    @Test
    public void selectTraineeByUsername_shouldReturnDaoResult() {
        // Arrange
        Trainee trainee = getTrainee();

        TraineeDto traineeDto = new TraineeDto();
        traineeDto.setFirstName("John");
        traineeDto.setLastName("Smith");

        when(traineeDao.findByUsername(trainee.getUsername())).thenReturn(Optional.of(trainee));
        when(traineeMapper.toDto(trainee)).thenReturn(traineeDto);

        // Act
        Optional<TraineeDto> foundTrainee = traineeServiceImpl.selectTraineeByUsername(trainee.getUsername());

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
    public void deleteTraineeByUsername_shouldCallDao_deleteTraineeByUsername()
            throws AuthenticationException {
        // Arrange
        Trainee trainee = getTrainee();

        when(authService.checkTraineeUsernameAndPassword(
                trainee.getUsername(),
                trainee.getPassword()))
                .thenReturn(true);

        // Act
        traineeServiceImpl.deleteTraineeByUsername(
                trainee.getUsername(),
                trainee.getUsername(),
                trainee.getPassword()
        );

        // Assert
        verify(traineeDao).deleteByUsername(trainee.getUsername());
    }

    @Test
    public void createTrainee_shouldThrowExceptionWhenFirstNameIsNull() {
        // Arrange
        CreateTraineeRequest request = new CreateTraineeRequest();
        request.setFirstName(null);
        request.setLastName("Smith");
        request.setDateOfBirth(LocalDate.of(2000, 1, 1));
        request.setAddress("Test Address");

        Trainee trainee = new Trainee();
        trainee.setFirstName(null);
        trainee.setLastName("Smith");
        trainee.setDateOfBirth(LocalDate.of(2000, 1, 1));
        trainee.setAddress("Test Address");

        when(traineeMapper.toEntity(request)).thenReturn(trainee);

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            traineeServiceImpl.createTrainee(request);
        });
    }

    @Test
    public void createTrainee_shouldThrowExceptionWhenFirstNameIsEmpty() {
        // Arrange
        CreateTraineeRequest request = new CreateTraineeRequest();
        request.setFirstName("");
        request.setLastName("Smith");
        request.setDateOfBirth(LocalDate.of(2000, 1, 1));
        request.setAddress("Test Address");

        Trainee trainee = new Trainee();
        trainee.setFirstName("");
        trainee.setLastName("Smith");
        trainee.setDateOfBirth(LocalDate.of(2000, 1, 1));
        trainee.setAddress("Test Address");

        when(traineeMapper.toEntity(request)).thenReturn(trainee);

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            traineeServiceImpl.createTrainee(request);
        });
    }

    @Test
    public void createTrainee_shouldThrowExceptionWhenLastNameIsNull() {
        // Arrange
        CreateTraineeRequest request = new CreateTraineeRequest();
        request.setFirstName("John");
        request.setLastName(null);
        request.setDateOfBirth(LocalDate.of(2000, 1, 1));
        request.setAddress("Test Address");

        Trainee trainee = new Trainee();
        trainee.setFirstName("John");
        trainee.setLastName(null);
        trainee.setDateOfBirth(LocalDate.of(2000, 1, 1));
        trainee.setAddress("Test Address");

        when(traineeMapper.toEntity(request)).thenReturn(trainee);

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            traineeServiceImpl.createTrainee(request);
        });
    }

    @Test
    public void createTrainee_shouldThrowExceptionWhenLastNameIsEmpty() {
        // Arrange
        CreateTraineeRequest request = new CreateTraineeRequest();
        request.setFirstName("John");
        request.setLastName("");
        request.setDateOfBirth(LocalDate.of(2000, 1, 1));
        request.setAddress("Test Address");

        Trainee trainee = new Trainee();
        trainee.setFirstName("John");
        trainee.setLastName("");
        trainee.setDateOfBirth(LocalDate.of(2000, 1, 1));
        trainee.setAddress("Test Address");

        when(traineeMapper.toEntity(request)).thenReturn(trainee);

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            traineeServiceImpl.createTrainee(request);
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
        List<TrainingDto> result = traineeServiceImpl.getTrainingsByUsernameAndCriteria(
                username,
                fromDate,
                toDate,
                trainerName,
                trainingTypeName);

        // Assert
        assertEquals(expectedTrainings.size(), result.size());
        verify(traineeDao).findTrainingsByUsernameAndCriteria(
                username,
                fromDate,
                toDate,
                trainerName,
                trainingTypeName);
    }

    @Test
    public void updateTraineeTrainers_shouldCallDaoMethod() throws AuthenticationException {
        // Arrange
        String traineeUsername = "john.smith";
        List<String> trainerUsernames = Arrays.asList("trainer1", "trainer2", "trainer3");
        String username = "john.smith";
        String password = "password123";

        when(authService.checkTraineeUsernameAndPassword(username, password)).thenReturn(true);

        // Act
        traineeServiceImpl.updateTraineeTrainers(traineeUsername, trainerUsernames, username, password);

        // Assert
        verify(traineeDao).updateTrainers(traineeUsername, trainerUsernames);
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