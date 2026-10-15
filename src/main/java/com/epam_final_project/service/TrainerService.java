package com.epam_final_project.service;

import com.epam_final_project.domain.Trainer;
import com.epam_final_project.domain.Training;

import javax.naming.AuthenticationException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface TrainerService {
    void createTrainer(Trainer trainer);
    void updateTrainer(
            Trainer updatedTrainer,
            Integer trainerId,
            String username,
            String password
    ) throws AuthenticationException;
    Optional<Trainer> selectTrainer(Integer trainerId);
    Optional<Trainer> selectTrainerByUsername(String username);
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
    List<Training> getTrainingsByUsernameAndCriteria(
            String trainerUsername,
            LocalDate fromDate,
            LocalDate toDate,
            String traineeName
    );
    List<Trainer> getTrainersNotAssignedOnTraineeByTraineeUsername(String username);
}
