package com.epam_final_project.service;

import com.epam_final_project.domain.Training;

import java.util.Optional;

public interface TrainingService {
    void createTraining(Training training);
    Optional<Training> selectTraining(Integer trainingId);
}
