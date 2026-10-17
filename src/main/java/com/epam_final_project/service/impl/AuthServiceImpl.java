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
    public boolean checkTraineeUsernameAndPassword(Integer traineeId, String username, String password) {
        if (traineeId == null || username == null || password == null) {
            throw new IllegalArgumentException("trainee id, username and password must be provided");
        }

        Optional<Trainee> foundTraineeOpt = traineeDao.findById(traineeId);

        if (foundTraineeOpt.isEmpty()) return false;

        Trainee foundTrainee = foundTraineeOpt.get();

        return (foundTrainee.getUsername().equals(username)) && (foundTrainee.getPassword().equals(password));
    }

    @Override
    public boolean checkTrainerUsernameAndPassword(Integer trainerId, String username, String password) {
        if (trainerId == null || username == null || password == null) {
            throw new IllegalArgumentException("trainer id, username and password must be provided");
        }

        Optional<Trainer> foundTrainerOpt = trainerDao.findById(trainerId);

        if (foundTrainerOpt.isEmpty()) return false;

        Trainer foundTrainer = foundTrainerOpt.get();

        return (foundTrainer.getUsername().equals(username)) && (foundTrainer.getPassword().equals(password));
    }
}