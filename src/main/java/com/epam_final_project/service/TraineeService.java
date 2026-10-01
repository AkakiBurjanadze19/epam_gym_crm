package com.epam_final_project.service;

import com.epam_final_project.domain.Trainee;

import java.util.Optional;

public interface TraineeService {
    void createTrainee(Trainee trainee);
    void updateTrainee(Trainee updatedTrainee, Integer traineeId);
    void deleteTrainee(Integer traineeId);
    Optional<Trainee> selectTrainee(Integer traineeId);
}
