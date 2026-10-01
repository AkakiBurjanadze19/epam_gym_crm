package com.epam_final_project.service;

import com.epam_final_project.domain.Trainer;

import java.util.Optional;

public interface TrainerService {
    void createTrainer(Trainer trainer);
    void updateTrainer(Trainer updatedTrainer, Integer trainerId);
    Optional<Trainer> selectTrainer(Integer trainerId);
}
