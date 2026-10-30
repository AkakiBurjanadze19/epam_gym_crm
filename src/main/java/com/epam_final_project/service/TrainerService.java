package com.epam_final_project.service;

import com.epam_final_project.dto.trainer.request.UpdateTrainerProfileRequestDto;
import com.epam_final_project.dto.trainer.response.TrainerProfileDto;
import com.epam_final_project.dto.trainer.response.TrainerSummaryDto;
import com.epam_final_project.dto.trainer.request.CreateTrainerRequest;
import com.epam_final_project.dto.trainer.response.TrainerRegistrationResponseDto;
import com.epam_final_project.dto.trainer.response.UpdateTrainerProfileResponseDto;
import com.epam_final_project.dto.training.response.TrainerTrainingResponseDto;

import javax.naming.AuthenticationException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface TrainerService {
    TrainerRegistrationResponseDto createTrainer(CreateTrainerRequest request);

    UpdateTrainerProfileResponseDto updateTrainer(
            UpdateTrainerProfileRequestDto request,
            String username,
            String password
    ) throws AuthenticationException;

    Optional<TrainerSummaryDto> selectTrainer(Integer trainerId);

    Optional<TrainerProfileDto> selectTrainerByUsername(String username, String authUsername, String authPassword)
            throws AuthenticationException;

    void changeTrainerPassword(
            String updatedPassword,
            Integer trainerId,
            String username,
            String password
    ) throws AuthenticationException;

    void activateTrainer(Integer trainerId, String username, String password)
            throws AuthenticationException;

    void deActivateTrainer(Integer trainerId, String username, String password)
            throws AuthenticationException;

    void activateDeactivateTrainer(String username, boolean isActive, String authUsername, String authPassword)
            throws AuthenticationException;

    List<TrainerTrainingResponseDto> getTrainingsByUsernameAndCriteria(
            String trainerUsername,
            LocalDate fromDate,
            LocalDate toDate,
            String traineeName,
            String authUsername,
            String authPassword
    ) throws AuthenticationException;

    List<TrainerSummaryDto> getTrainersNotAssignedOnTraineeByTraineeUsername(
            String username,
            String authUsername,
            String authPassword
    ) throws AuthenticationException;
}