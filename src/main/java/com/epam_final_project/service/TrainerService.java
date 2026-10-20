package com.epam_final_project.service;

import com.epam_final_project.dto.CreateTrainerRequest;
import com.epam_final_project.dto.TrainerDto;
import com.epam_final_project.dto.TrainingDto;

import javax.naming.AuthenticationException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface TrainerService {
    void createTrainer(CreateTrainerRequest request);
    void updateTrainer(
            CreateTrainerRequest request,
            Integer trainerId,
            String username,
            String password
    ) throws AuthenticationException;
    Optional<TrainerDto> selectTrainer(Integer trainerId);
    Optional<TrainerDto> selectTrainerByUsername(String username);
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
    List<TrainingDto> getTrainingsByUsernameAndCriteria(
            String trainerUsername,
            LocalDate fromDate,
            LocalDate toDate,
            String traineeName
    );
    List<TrainerDto> getTrainersNotAssignedOnTraineeByTraineeUsername(String username);
}