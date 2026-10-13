package com.epam_final_project.dao;

import com.epam_final_project.domain.Training;

import java.util.Optional;

public interface TrainingDao {
    void create(Training training);
    Optional<Training> findById(Integer trainingId);
}