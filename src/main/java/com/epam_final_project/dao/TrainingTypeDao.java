package com.epam_final_project.dao;

import com.epam_final_project.domain.TrainingType;

import java.util.List;
import java.util.Optional;

public interface TrainingTypeDao {
    Optional<TrainingType> findById(Integer trainingTypeId);
    List<TrainingType> findAll();
}
