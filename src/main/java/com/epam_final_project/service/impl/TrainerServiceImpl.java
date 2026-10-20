package com.epam_final_project.service.impl;

import com.epam_final_project.dao.TraineeDao;
import com.epam_final_project.dao.TrainerDao;
import com.epam_final_project.dao.TrainingTypeDao;
import com.epam_final_project.domain.Trainee;
import com.epam_final_project.domain.Trainer;
import com.epam_final_project.domain.TrainingType;
import com.epam_final_project.dto.CreateTrainerRequest;
import com.epam_final_project.dto.TrainerDto;
import com.epam_final_project.dto.TrainingDto;
import com.epam_final_project.mapper.TrainerMapper;
import com.epam_final_project.mapper.TrainingMapper;
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
    private TrainingTypeDao trainingTypeDao;
    private AuthService authService;
    private TrainerMapper trainerMapper;
    private TrainingMapper trainingMapper;

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

    @Autowired
    public void setTrainerMapper(TrainerMapper trainerMapper) {
        this.trainerMapper = trainerMapper;
    }

    @Autowired
    public void setTrainingMapper(TrainingMapper trainingMapper) {
        this.trainingMapper = trainingMapper;
    }

    @Autowired
    public void setTrainingTypeDao(TrainingTypeDao trainingTypeDao) {
        this.trainingTypeDao = trainingTypeDao;
    }

    @Override
    @Transactional
    public void createTrainer(CreateTrainerRequest request) {
        log.info("creating trainer: {}", request);

        Trainer trainer = trainerMapper.toEntity(request);

        TrainingType specialization = trainingTypeDao
                .findByTrainingTypeName(request.getSpecialization())
                .orElseThrow(() -> new IllegalArgumentException("Invalid specialization: " +
                        request.getSpecialization()));
        trainer.setSpecialization(specialization);

        validateTrainer(trainer);

        generateUsernameAndPassword(trainer);

        trainerDao.create(trainer);
    }

    private void generateUsernameAndPassword(Trainer trainer) {
        if (trainer.getUsername() == null || trainer.getUsername().isEmpty()) {
            trainer.setUsername(UsernameAndPasswordGenerator.generateUsername(
                    trainer.getFirstName(),
                    trainer.getLastName(),
                    traineeDao,
                    trainerDao
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

    @Override
    @Transactional
    public void updateTrainer(
            CreateTrainerRequest request,
            Integer trainerId,
            String username,
            String password
    ) throws AuthenticationException {
        log.info("updating trainer with id {} with updated trainer: {}", trainerId, request);
        if (!authService.checkTrainerUsernameAndPassword(username, password)) {
            throw new AuthenticationException("invalid credentials");
        }

        Trainer updatedTrainer = trainerMapper.toEntity(request);

        trainerDao.update(updatedTrainer, trainerId);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<TrainerDto> selectTrainer(Integer trainerId) {
        log.info("selecting trainer with id {}", trainerId);
        return trainerDao
                .findById(trainerId)
                .map(trainerMapper::toDto);
    }

    @Override
    public Optional<TrainerDto> selectTrainerByUsername(String username) {
        return trainerDao
                .findByUsername(username)
                .map(trainerMapper::toDto);
    }

    @Override
    @Transactional
    public void changeTrainerPassword(
            String updatedPassword,
            Integer trainerId,
            String username,
            String password
    ) throws AuthenticationException {
        if (!authService.checkTrainerUsernameAndPassword(username, password)) {
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
        if (!authService.checkTrainerUsernameAndPassword(username, password)) {
            throw new AuthenticationException("invalid credentials");
        }

        Optional<Trainer> foundTrainer = trainerDao.findById(trainerId);

        foundTrainer.ifPresent(trainer -> {
            if (trainer.isActive()) {
                throw new IllegalStateException("Trainer is already active");
            }

            trainer.setActive(true);
        });
    }

    @Override
    @Transactional
    public void deActivateTrainer(Integer trainerId, String username, String password)
            throws AuthenticationException {
        if (!authService.checkTrainerUsernameAndPassword(username, password)) {
            throw new AuthenticationException("invalid credentials");
        }

        Optional<Trainer> foundTrainer = trainerDao.findById(trainerId);

        foundTrainer.ifPresent(trainer -> {
            if (!trainer.isActive()) {
                throw new IllegalStateException("Trainer is already not active");
            }

            trainer.setActive(false);
        });
    }

    @Override
    @Transactional(readOnly = true)
    public List<TrainingDto> getTrainingsByUsernameAndCriteria(
            String trainerUsername,
            LocalDate fromDate,
            LocalDate toDate,
            String traineeName
    ) {
        return trainerDao.findTrainingsByUsernameAndCriteria(
                trainerUsername, fromDate, toDate, traineeName)
                .stream()
                .map(trainingMapper::toDto).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<TrainerDto> getTrainersNotAssignedOnTraineeByTraineeUsername(String username) {
        return trainerDao
                .findTrainersNotAssignedOnTraineeByTraineeUsername(username)
                .stream()
                .map(trainerMapper::toDto)
                .toList();
    }
}