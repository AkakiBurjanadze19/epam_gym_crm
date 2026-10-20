package com.epam_final_project.dao;

import com.epam_final_project.domain.Trainee;
import com.epam_final_project.domain.Training;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface TraineeDao {
    void create(Trainee trainee);
    void update(Trainee updatedTrainee, Integer traineeId);
    void delete(Integer traineeId);
    Optional<Trainee> findById(Integer traineeId);
    List<Trainee> findAll();
    Optional<Trainee> findByUsername(String username);
    void deleteByUsername(String username);
    List<Training> findTrainingsByUsernameAndCriteria(
            String username,
            LocalDate fromDate,
            LocalDate toDate,
            String trainerName,
            String trainingTypeName
    );
    void updateTrainers(String traineeUsername, List<String> updatedTrainerUsernames);
    boolean existsByUsername(String username);
}