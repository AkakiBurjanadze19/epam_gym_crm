package com.epam_final_project.service.impl;

import com.epam_final_project.dao.TraineeDao;
import com.epam_final_project.dao.TrainerDao;
import com.epam_final_project.domain.Trainee;
import com.epam_final_project.domain.Trainer;
import com.epam_final_project.domain.Training;
import com.epam_final_project.service.AuthService;
import com.epam_final_project.service.TrainerService;
import com.epam_final_project.util.UsernameAndPasswordGenerator;
import org.springframework.transaction.annotation.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.naming.AuthenticationException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class TrainerServiceImpl implements TrainerService {
    private static final Logger log = LoggerFactory.getLogger(TrainerServiceImpl.class);
    private TrainerDao trainerDao;
    private TraineeDao traineeDao;
    private AuthService authService;

    @Autowired
    public void setTrainerDao(TrainerDao trainerDao) {
        this.trainerDao = trainerDao;
    }

    @Autowired
    public void setTraineeDao(TraineeDao traineeDao) {
        this.traineeDao = traineeDao;
    }

    @Autowired
    public void setAuthService(AuthService authService) {
        this.authService = authService;
    }

    @Override
    @Transactional
    public void createTrainer(Trainer trainer) {
        log.info("creating trainer: {}", trainer);

        validateTrainer(trainer);

        generateUsernameAndPassword(trainer);

        trainerDao.create(trainer);
    }

    private void generateUsernameAndPassword(Trainer trainer) {
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
    }

    private static void validateTrainer(Trainer trainer) {
        if (trainer.getFirstName() == null || trainer.getFirstName().isEmpty()) {
            throw new IllegalArgumentException("first name must not be null or empty");
        }

        if (trainer.getLastName() == null || trainer.getLastName().isEmpty()) {
            throw new IllegalArgumentException("last name must not be null or empty");
        }
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

    @Override
    @Transactional
    public void updateTrainer(
            Trainer updatedTrainer,
            Integer trainerId,
            String username,
            String password
    ) throws AuthenticationException {
        log.info("updating trainer with id {} with updated trainer: {}", trainerId, updatedTrainer);
        if (!authService.checkTrainerUsernameAndPassword(trainerId, username, password)) {
            throw new AuthenticationException("invalid credentials");
        }

        trainerDao.update(updatedTrainer, trainerId);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Trainer> selectTrainer(Integer trainerId) {
        log.info("selecting trainer with id {}", trainerId);
        return trainerDao.findById(trainerId);
    }

    @Override
    public Optional<Trainer> selectTrainerByUsername(String username) {
        return trainerDao.findByUsername(username);
    }

    @Override
    @Transactional
    public void changeTrainerPassword(
            String updatedPassword,
            Integer trainerId,
            String username,
            String password
    ) throws AuthenticationException {
        if (!authService.checkTrainerUsernameAndPassword(trainerId, username, password)) {
            throw new AuthenticationException("invalid credentials");
        }

        Optional<Trainer> foundTrainer = trainerDao.findById(trainerId);

        foundTrainer.ifPresent(trainer -> {
            trainer.setPassword(updatedPassword);
        });
    }

    @Override
    @Transactional
    public void activateTrainer(Integer trainerId, String username, String password)
            throws AuthenticationException{
        if (!authService.checkTrainerUsernameAndPassword(trainerId, username, password)) {
            throw new AuthenticationException("invalid credentials");
        }

        Optional<Trainer> foundTrainer = trainerDao.findById(trainerId);

        foundTrainer.ifPresent(trainer -> {
            if (!trainer.isActive()) {
                trainer.setActive(true);
            }
        });
    }

    @Override
    @Transactional
    public void deActivateTrainer(Integer trainerId, String username, String password)
            throws AuthenticationException {
        if (!authService.checkTrainerUsernameAndPassword(trainerId, username, password)) {
            throw new AuthenticationException("invalid credentials");
        }

        Optional<Trainer> foundTrainer = trainerDao.findById(trainerId);

        foundTrainer.ifPresent(trainer -> {
            if (trainer.isActive()) {
                trainer.setActive(false);
            }
        });
    }

    @Override
    @Transactional(readOnly = true)
    public List<Training> getTrainingsByUsernameAndCriteria(
            String trainerUsername,
            LocalDate fromDate,
            LocalDate toDate,
            String traineeName
    ) {
        return trainerDao.findTrainingsByUsernameAndCriteria(
                trainerUsername,
                fromDate,
                toDate,
                traineeName
        );
    }

    @Override
    @Transactional(readOnly = true)
    public List<Trainer> getTrainersNotAssignedOnTraineeByTraineeUsername(String username) {
        return trainerDao.findTrainersNotAssignedOnTraineeByTraineeUsername(username);
    }
}