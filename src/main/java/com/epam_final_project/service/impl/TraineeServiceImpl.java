package com.epam_final_project.service.impl;

import com.epam_final_project.dao.TraineeDao;
import com.epam_final_project.dao.TrainerDao;
import com.epam_final_project.domain.Trainee;
import com.epam_final_project.domain.Trainer;
import com.epam_final_project.service.TraineeService;
import com.epam_final_project.util.UsernameAndPasswordGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class TraineeServiceImpl implements TraineeService {
    private static final Logger log = LoggerFactory.getLogger(TraineeServiceImpl.class);
    private TraineeDao traineeDao;
    private TrainerDao trainerDao;

    @Autowired
    public void setTraineeDao(TraineeDao traineeDao) {
        this.traineeDao = traineeDao;
    }

    @Autowired
    public void setTrainerDao(TrainerDao trainerDao) {
        this.trainerDao = trainerDao;
    }

    @Override
    public void createTrainee(Trainee trainee) {
        log.info("creating trainee: {}", trainee);

        if (trainee.getUsername() == null || trainee.getUsername().isEmpty()) {
            trainee.setUsername(UsernameAndPasswordGenerator.generateUsername(
                    trainee.getFirstName(),
                    trainee.getLastName(),
                    getExistingUsernames()
            ));
        }

        if (trainee.getPassword() == null || trainee.getPassword().isEmpty()) {
            trainee.setPassword(UsernameAndPasswordGenerator.generatePassword());
        }

        traineeDao.create(trainee);
    }

    @Override
    public void updateTrainee(Trainee updatedTrainee, Integer traineeId) {
        log.info("updating trainee with id {} with updated trainee: {}", traineeId, updatedTrainee);
        traineeDao.update(updatedTrainee, traineeId);
    }

    @Override
    public void deleteTrainee(Integer traineeId) {
        log.info("deleting trainee with id {}", traineeId);
        traineeDao.delete(traineeId);
    }

    @Override
    public Optional<Trainee> selectTrainee(Integer traineeId) {
        log.info("selecting trainee with id {}", traineeId);
        return traineeDao.findById(traineeId);
    }

    private List<String> getExistingUsernames() {
        List<String> traineeUsernames = traineeDao
                .findAll()
                .stream()
                .map(Trainee::getUsername)
                .toList();

        List<String> trainerUsernames = trainerDao
                .findAll()
                .stream()
                .map(Trainer::getUsername)
                .toList();

        List<String> existingUsernames = new ArrayList<>();
        existingUsernames.addAll(traineeUsernames);
        existingUsernames.addAll(trainerUsernames);

        return existingUsernames;
    }
}
