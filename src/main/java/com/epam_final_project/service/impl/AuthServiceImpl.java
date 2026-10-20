package com.epam_final_project.service.impl;

import com.epam_final_project.dao.TraineeDao;
import com.epam_final_project.dao.TrainerDao;
import com.epam_final_project.domain.Trainee;
import com.epam_final_project.domain.Trainer;
import com.epam_final_project.service.AuthService;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthServiceImpl implements AuthService {
    private final TraineeDao traineeDao;
    private final TrainerDao trainerDao;

    public AuthServiceImpl(TraineeDao traineeDao, TrainerDao trainerDao) {
        this.traineeDao = traineeDao;
        this.trainerDao = trainerDao;
    }

    @Override
    public boolean checkTraineeUsernameAndPassword(String username, String password) {
        if (username == null || password == null) {
            throw new IllegalArgumentException("trainee id, username and password must be provided");
        }

        return traineeDao
                .findByUsername(username)
                .map(trainee -> trainee.getPassword().equals(password))
                .orElse(false);
    }

    @Override
    public boolean checkTrainerUsernameAndPassword(String username, String password) {
        if (username == null || password == null) {
            throw new IllegalArgumentException("trainer id, username and password must be provided");
        }

        return trainerDao
                .findByUsername(username)
                .map(trainer -> trainer.getPassword().equals(password))
                .orElse(false);
    }
}