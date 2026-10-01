package com.epam_final_project.dao;

import com.epam_final_project.domain.Trainer;

import java.util.Optional;

public interface TrainerDao {
    void create(Trainer trainer);
    void update(Trainer updatedTrainer, Integer trainerId);
    Optional<Trainer> select(Integer trainerId);
}
