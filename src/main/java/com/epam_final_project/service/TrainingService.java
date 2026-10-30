package com.epam_final_project.service;

import com.epam_final_project.dto.training.request.CreateTrainingRequestDto;
import com.epam_final_project.dto.TrainingDto;

import javax.naming.AuthenticationException;
import java.util.Optional;

public interface TrainingService {
    void createTraining(CreateTrainingRequestDto request, String authUsername, String authPassword)
            throws AuthenticationException;
    Optional<TrainingDto> selectTraining(Integer trainingId);
}
