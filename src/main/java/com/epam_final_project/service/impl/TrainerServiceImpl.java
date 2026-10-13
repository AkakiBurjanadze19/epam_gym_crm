package com.epam_final_project.service.impl;

import com.epam_final_project.dao.TraineeDao;
import com.epam_final_project.dao.TrainerDao;
import com.epam_final_project.domain.Trainee;
import com.epam_final_project.domain.Trainer;
import com.epam_final_project.service.TrainerService;
import com.epam_final_project.util.UsernameAndPasswordGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class TrainerServiceImpl implements TrainerService {
    private static final Logger log = LoggerFactory.getLogger(TrainerServiceImpl.class);
    private TrainerDao trainerDao;
    private TraineeDao traineeDao;

    @Autowired
    public void setTrainerDao(TrainerDao trainerDao) {
        this.trainerDao = trainerDao;
    }

    @Autowired
    public void setTraineeDao(TraineeDao traineeDao) {
        this.traineeDao = traineeDao;
    }

    @Override
    public void createTrainer(Trainer trainer) {
        log.info("creating trainer: {}", trainer);

        if (trainer.getUsername() == null || trainer.getUsername().isEmpty()) {
            trainer.setUsername(UsernameAndPasswordGenerator.generateUsername(
                    trainer.getFirstName(),
                    trainer.getLastName(),
                    getExistingUsernames()
            ));
        }

        if (trainer.getPassword() == null || trainer.getPassword().isEmpty()) {
            trainer.setPassword(UsernameAndPasswordGenerator.generatePassword());
        }

        trainerDao.create(trainer);
    }

    @Override
    public void updateTrainer(Trainer updatedTrainer, Integer trainerId) {
        log.info("updating trainer with id {} with updated trainer: {}", trainerId, updatedTrainer);
        trainerDao.update(updatedTrainer, trainerId);
    }

    @Override
    public Optional<Trainer> selectTrainer(Integer trainerId) {
        log.info("selecting trainer with id {}", trainerId);
        return trainerDao.findById(trainerId);
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
