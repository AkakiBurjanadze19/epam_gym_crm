package com.epam_final_project.service;

import com.epam_final_project.dto.trainee.request.CreateTraineeRequest;
import com.epam_final_project.dto.trainee.request.UpdateTraineeProfileRequestDto;
import com.epam_final_project.dto.trainee.response.TraineeProfileDto;
import com.epam_final_project.dto.trainee.response.UpdateTraineeProfileResponseDto;
import com.epam_final_project.dto.trainee.response.TraineeRegistrationResponseDto;
import com.epam_final_project.dto.trainer.response.TrainerSummaryDto;
import com.epam_final_project.dto.training.response.TraineeTrainingResponseDto;

import javax.naming.AuthenticationException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface TraineeService {
    TraineeRegistrationResponseDto createTrainee(CreateTraineeRequest request);

    UpdateTraineeProfileResponseDto updateTrainee(
            UpdateTraineeProfileRequestDto request,
            String username,
            String password
    ) throws AuthenticationException;

    void deleteTrainee(Integer traineeId, String username, String password)
            throws AuthenticationException;

    Optional<TraineeProfileDto> selectTrainee(Integer traineeId);

    Optional<TraineeProfileDto> selectTraineeByUsername(String username);

    void changeTraineePassword(String updatedPassword, Integer traineeId, String username, String password)
            throws AuthenticationException;

    void activateTrainee(Integer traineeId, String username, String password)
            throws AuthenticationException;

    void deActivateTrainee(Integer traineeId, String username, String password)
            throws AuthenticationException;

    void activateDeactivateTrainee(String username, boolean isActive, String authUsername, String authPassword)
            throws AuthenticationException;

    void deleteTraineeByUsername(String traineeUsername, String username, String password)
            throws AuthenticationException;

    List<TraineeTrainingResponseDto> getTrainingsByUsernameAndCriteria(
            String username,
            LocalDate fromDate,
            LocalDate toDate,
            String trainerName,
            String trainingTypeName
    );

    List<TrainerSummaryDto> updateTraineeTrainers(
            String traineeUsername,
            List<String> updatedTrainerUsernames,
            String username,
            String password
    ) throws AuthenticationException;
}