package com.epam_final_project.service.impl;

import com.epam_final_project.dao.TraineeDao;
import com.epam_final_project.dao.TrainerDao;
import com.epam_final_project.domain.Trainee;
import com.epam_final_project.domain.Trainer;
import com.epam_final_project.domain.Training;
import com.epam_final_project.dto.CreateTraineeRequest;
import com.epam_final_project.dto.TraineeDto;
import com.epam_final_project.dto.TrainingDto;
import com.epam_final_project.mapper.TraineeMapper;
import com.epam_final_project.mapper.TrainerMapper;
import com.epam_final_project.mapper.TrainingMapper;
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
    private TraineeMapper traineeMapper;
    private TrainingMapper trainingMapper;

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

    @Autowired
    public void setTraineeMapper(TraineeMapper traineeMapper) {
        this.traineeMapper = traineeMapper;
    }

    @Autowired
    public void setTrainingMapper(TrainingMapper trainingMapper) {
        this.trainingMapper = trainingMapper;
    }

    @Override
    @Transactional
    public void createTrainee(CreateTraineeRequest request) {
        log.info("creating trainee: {}", request);

        Trainee trainee = traineeMapper.toEntity(request);

        validateTrainee(trainee);

        generateUsernameAndPassword(trainee);

        traineeDao.create(trainee);
    }

    private void generateUsernameAndPassword(Trainee trainee) {
        if (trainee.getUsername() == null || trainee.getUsername().isEmpty()) {
                trainee.setUsername(UsernameAndPasswordGenerator.generateUsername(
                        trainee.getFirstName(),
                        trainee.getLastName(),
                        traineeDao,
                        trainerDao
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

    @Override
    @Transactional
    public void updateTrainee(
            CreateTraineeRequest request,
            Integer traineeId,
            String username,
            String password
    ) throws AuthenticationException {
        log.info("updating trainee with id {} with updated trainee: {}", traineeId, request);
        if (!authService.checkTraineeUsernameAndPassword(username, password)) {
            throw new AuthenticationException("invalid credentials provided");
        }

        Trainee updatedTrainee = traineeMapper.toEntity(request);

        traineeDao.update(updatedTrainee, traineeId);
    }

    @Override
    @Transactional
    public void deleteTrainee(Integer traineeId, String username, String password)
            throws AuthenticationException {
        log.info("deleting trainee with id {}", traineeId);
        if (!authService.checkTraineeUsernameAndPassword(username, password)) {
            throw new AuthenticationException("invalid credentials provided");
        }

        traineeDao.delete(traineeId);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<TraineeDto> selectTrainee(Integer traineeId) {
        log.info("selecting trainee with id {}", traineeId);
        return traineeDao
                .findById(traineeId)
                .map(traineeMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<TraineeDto> selectTraineeByUsername(String username) {
        return traineeDao
                .findByUsername(username)
                .map(traineeMapper::toDto);
    }

    @Override
    @Transactional
    public void changeTraineePassword(
            String updatedPassword,
            Integer traineeId,
            String username,
            String password
    ) throws AuthenticationException {
        if (!authService.checkTraineeUsernameAndPassword(username, password)) {
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
        if (!authService.checkTraineeUsernameAndPassword(username, password)) {
            throw new AuthenticationException("invalid credentials provided");
        }

        Optional<Trainee> foundTrainee = traineeDao.findById(traineeId);

        foundTrainee.ifPresent(trainee -> {
            if (trainee.isActive()) {
                throw new IllegalStateException("Trainee is already active");
            }

            trainee.setActive(true);
        });
    }

    @Override
    @Transactional
    public void deActivateTrainee(Integer traineeId, String username, String password)
            throws AuthenticationException {
        if (!authService.checkTraineeUsernameAndPassword(username, password)) {
            throw new AuthenticationException("invalid credentials provided");
        }

        Optional<Trainee> foundTrainee = traineeDao.findById(traineeId);

        foundTrainee.ifPresent(trainee -> {
            if (!trainee.isActive()) {
                throw new IllegalStateException("Trainee is already not active");
            }

            trainee.setActive(false);
        });
    }

    @Override
    @Transactional
    public void deleteTraineeByUsername(String traineeUsername, String username, String password)
            throws AuthenticationException {
        if (!authService.checkTraineeUsernameAndPassword(username, password)) {
            throw new AuthenticationException("invalid credentials provided");
        }

        traineeDao.deleteByUsername(traineeUsername);
    }

    @Override
    @Transactional(readOnly = true)
    public List<TrainingDto> getTrainingsByUsernameAndCriteria(
            String username,
            LocalDate fromDate,
            LocalDate toDate,
            String trainerName,
            String trainingTypeName
    ) {
        return traineeDao
                .findTrainingsByUsernameAndCriteria(
                        username,
                        fromDate,
                        toDate,
                        trainerName,
                        trainingTypeName
        ).stream()
                .map(trainingMapper::toDto)
                .toList();
    }

    @Override
    @Transactional
    public void updateTraineeTrainers(
            String traineeUsername,
            List<String> updatedTrainerUsernames,
            String username,
            String password
    ) throws AuthenticationException {
        if (!authService.checkTraineeUsernameAndPassword(username, password)) {
            throw new AuthenticationException("invalid credentials provided");
        }

        traineeDao.updateTrainers(traineeUsername, updatedTrainerUsernames);
    }
}