package com.epam_final_project.service;

import com.epam_final_project.domain.Training;
import com.epam_final_project.dto.CreateTrainingRequest;
import com.epam_final_project.dto.TrainingDto;

import java.util.Optional;

public interface TrainingService {
    void createTraining(CreateTrainingRequest request);
    Optional<TrainingDto> selectTraining(Integer trainingId);
}
