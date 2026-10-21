package com.epam_final_project.service;

import com.epam_final_project.domain.TrainingType;

import java.util.List;
import java.util.Optional;

public interface TrainingTypeService {
    Optional<TrainingType> selectTrainingType(Integer trainingTypeId);
    List<TrainingType> getAllTrainingTypes();
}