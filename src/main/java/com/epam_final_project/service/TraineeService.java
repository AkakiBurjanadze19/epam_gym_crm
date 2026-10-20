package com.epam_final_project.service;

import com.epam_final_project.domain.Trainee;
import com.epam_final_project.domain.Training;
import com.epam_final_project.dto.CreateTraineeRequest;
import com.epam_final_project.dto.TraineeDto;
import com.epam_final_project.dto.TrainingDto;

import javax.naming.AuthenticationException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface TraineeService {
    void createTrainee(CreateTraineeRequest request);
    void updateTrainee(
            CreateTraineeRequest request,
            Integer traineeId,
            String username,
            String password
    ) throws AuthenticationException;
    void deleteTrainee(Integer traineeId, String username, String password)
            throws AuthenticationException;
    Optional<TraineeDto> selectTrainee(Integer traineeId);
    Optional<TraineeDto> selectTraineeByUsername(String username);
    void changeTraineePassword(String updatedPassword, Integer traineeId, String username, String password)
            throws AuthenticationException;
    void activateTrainee(Integer traineeId, String username, String password) throws AuthenticationException;
    void deActivateTrainee(Integer traineeId, String username, String password) throws AuthenticationException;
    void deleteTraineeByUsername(String traineeUsername, String username, String password)
            throws AuthenticationException;
    List<TrainingDto> getTrainingsByUsernameAndCriteria(
            String username,
            LocalDate fromDate,
            LocalDate toDate,
            String trainerName,
            String trainingTypeName
    );
    void updateTraineeTrainers(
            String traineeUsername,
            List<String> updatedTrainerUsernames,
            String username,
            String password
    ) throws AuthenticationException;
}