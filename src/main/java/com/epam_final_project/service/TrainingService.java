package com.epam_final_project.service;

import com.epam_final_project.dto.training.request.CreateTrainingRequestDto;
import com.epam_final_project.dto.TrainingDto;

import java.util.Optional;

public interface TrainingService {
    void createTraining(CreateTrainingRequestDto request);
    Optional<TrainingDto> selectTraining(Integer trainingId);
}
