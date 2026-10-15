package com.epam_final_project.dao;

import com.epam_final_project.domain.Trainer;
import com.epam_final_project.domain.Training;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface TrainerDao {
    void create(Trainer trainer);
    void update(Trainer updatedTrainer, Integer trainerId);
    Optional<Trainer> findById(Integer trainerId);
    List<Trainer> findAll();
    Optional<Trainer> findByUsername(String username);
    List<Training> findTrainingsByUsernameAndCriteria(
            String trainerUsername,
            LocalDate fromDate,
            LocalDate toDate,
            String traineeName
    );
    List<Trainer> findTrainersNotAssignedOnTraineeByTraineeUsername(String username);
}
