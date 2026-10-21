package com.epam_final_project.service.impl;

import com.epam_final_project.dao.TraineeDao;
import com.epam_final_project.dao.TrainerDao;
import com.epam_final_project.domain.Trainee;
import com.epam_final_project.domain.Trainer;
import com.epam_final_project.domain.Training;
import com.epam_final_project.service.AuthService;
import com.epam_final_project.service.TraineeService;
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
public class TraineeServiceImpl implements TraineeService {
    private static final Logger log = LoggerFactory.getLogger(TraineeServiceImpl.class);
    private TraineeDao traineeDao;
    private TrainerDao trainerDao;
    private AuthService authService;

    @Autowired
    public void setTraineeDao(TraineeDao traineeDao) {
        this.traineeDao = traineeDao;
    }

    @Autowired
    public void setTrainerDao(TrainerDao trainerDao) {
        this.trainerDao = trainerDao;
    }

    @Autowired
    public void setAuthService(AuthService authService) {
        this.authService = authService;
    }

    @Override
    @Transactional
    public void createTrainee(Trainee trainee) {
        log.info("creating trainee: {}", trainee);

        validateTrainee(trainee);

        generateUsernameAndPassword(trainee);

        traineeDao.create(trainee);
    }

    private void generateUsernameAndPassword(Trainee trainee) {
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
    }

    private void validateTrainee(Trainee trainee) {
        if (trainee.getFirstName() == null || trainee.getFirstName().isEmpty()) {
            throw new IllegalArgumentException("first name must not be null or empty");
        }

        if (trainee.getLastName() == null || trainee.getLastName().isEmpty()) {
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
    public void updateTrainee(Trainee updatedTrainee, Integer traineeId, String username, String password)
            throws AuthenticationException {
        log.info("updating trainee with id {} with updated trainee: {}", traineeId, updatedTrainee);
        if (!authService.checkTraineeUsernameAndPassword(traineeId, username, password)) {
            throw new AuthenticationException("invalid credentials provided");
        }

        traineeDao.update(updatedTrainee, traineeId);
    }

    @Override
    @Transactional
    public void deleteTrainee(Integer traineeId, String username, String password)
            throws AuthenticationException {
        log.info("deleting trainee with id {}", traineeId);
        if (!authService.checkTraineeUsernameAndPassword(traineeId, username, password)) {
            throw new AuthenticationException("invalid credentials provided");
        }

        traineeDao.delete(traineeId);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Trainee> selectTrainee(Integer traineeId) {
        log.info("selecting trainee with id {}", traineeId);
        return traineeDao.findById(traineeId);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Trainee> selectTraineeByUsername(String username) {
        return traineeDao.findByUsername(username);
    }

    @Override
    @Transactional
    public void changeTraineePassword(
            String updatedPassword,
            Integer traineeId,
            String username,
            String password
    ) throws AuthenticationException {
        if (!authService.checkTraineeUsernameAndPassword(traineeId, username, password)) {
            throw new AuthenticationException("invalid credentials provided");
        }

        Optional<Trainee> foundTrainee = traineeDao.findById(traineeId);

        foundTrainee.ifPresent(trainee -> {
            trainee.setPassword(updatedPassword);
        });
    }

    @Override
    @Transactional
    public void activateTrainee(Integer traineeId, String username, String password)
            throws AuthenticationException {
        if (!authService.checkTraineeUsernameAndPassword(traineeId, username, password)) {
            throw new AuthenticationException("invalid credentials provided");
        }

        Optional<Trainee> foundTrainee = traineeDao.findById(traineeId);

        foundTrainee.ifPresent(trainee -> {
            if (!trainee.isActive()) {
                trainee.setActive(true);
            }
        });
    }

    @Override
    @Transactional
    public void deActivateTrainee(Integer traineeId, String username, String password)
            throws AuthenticationException {
        if (!authService.checkTraineeUsernameAndPassword(traineeId, username, password)) {
            throw new AuthenticationException("invalid credentials provided");
        }

        Optional<Trainee> foundTrainee = traineeDao.findById(traineeId);

        foundTrainee.ifPresent(trainee -> {
            if (trainee.isActive()) {
                trainee.setActive(false);
            }
        });
    }

    @Override
    @Transactional
    public void deleteTraineeByUsername(String username) {
        traineeDao.deleteByUsername(username);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Training> getTrainingsByUsernameAndCriteria(
            String username,
            LocalDate fromDate,
            LocalDate toDate,
            String trainerName,
            String trainingTypeName
    ) {
        return traineeDao.findTrainingsByUsernameAndCriteria(
                username,
                fromDate,
                toDate,
                trainerName,
                trainingTypeName
        );
    }

    @Override
    @Transactional
    public void updateTraineeTrainers(Integer traineeId, List<Integer> updatedTrainerIds) {
        traineeDao.updateTrainers(traineeId, updatedTrainerIds);
    }
}