package com.epam_final_project.service.impl;

import com.epam_final_project.dao.TraineeDao;
import com.epam_final_project.dao.TrainerDao;
import com.epam_final_project.domain.Trainee;
import com.epam_final_project.domain.Trainer;
import com.epam_final_project.service.AuthService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
            throw new IllegalArgumentException("username and password must be provided");
        }

        return traineeDao
                .findByUsername(username)
                .map(trainee -> trainee.getPassword().equals(password))
                .orElse(false);
    }

    @Override
    public boolean checkTrainerUsernameAndPassword(String username, String password) {
        if (username == null || password == null) {
            throw new IllegalArgumentException("username and password must be provided");
        }

        return trainerDao
                .findByUsername(username)
                .map(trainer -> trainer.getPassword().equals(password))
                .orElse(false);
    }

    @Override
    @Transactional
    public void changeLogin(String username, String oldPassword, String newPassword) {
        Optional<Trainee> trainee = traineeDao.findByUsername(username);
        Optional<Trainer> trainer = trainerDao.findByUsername(username);

        handleDataCorruption(username, trainee, trainer);

        if (handleTraineeInvalidOldPassword(oldPassword, newPassword, trainee)) return;

        if (handleTrainerInvalidOldPassword(oldPassword, newPassword, trainer)) return;

        throw new IllegalArgumentException("User not found: " + username);
    }

    private boolean handleTraineeInvalidOldPassword(
            String oldPassword,
            String newPassword,
            Optional<Trainee> trainee
    ) {
        if (trainee.isPresent()) {
            if (!trainee.get().getPassword().equals(oldPassword)) {
                throw new IllegalArgumentException("Invalid old password");
            }
            trainee.get().setPassword(newPassword);
            return true;
        }
        return false;
    }

    private boolean handleTrainerInvalidOldPassword(
            String oldPassword,
            String newPassword,
            Optional<Trainer> trainer
    ) {
        if (trainer.isPresent()) {
            if (!trainer.get().getPassword().equals(oldPassword)) {
                throw new IllegalArgumentException("Invalid old password");
            }
            trainer.get().setPassword(newPassword);
            return true;
        }
        return false;
    }

    private void handleDataCorruption(
            String username,
            Optional<Trainee> trainee,
            Optional<Trainer> trainer
    ) {
        if (trainee.isPresent() && trainer.isPresent()) {
            throw new IllegalStateException("Data corruption: username exists in both trainee and trainer tables: " + username);
        }
    }
}