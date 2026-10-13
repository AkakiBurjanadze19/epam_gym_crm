package com.epam_final_project.dao;

import com.epam_final_project.domain.Trainee;

import java.util.List;
import java.util.Optional;

public interface TraineeDao {
    void create(Trainee trainee);
    void update(Trainee updatedTrainee, Integer traineeId);
    void delete(Integer traineeId);
    Optional<Trainee> findById(Integer traineeId);
    List<Trainee> findAll();
}
