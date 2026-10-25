package com.epam_final_project.service.impl;

import com.epam_final_project.dao.TraineeDao;
import com.epam_final_project.dao.TrainerDao;
import com.epam_final_project.dao.TrainingTypeDao;
import com.epam_final_project.domain.Trainer;
import com.epam_final_project.domain.TrainingType;
import com.epam_final_project.dto.common.TraineeSummaryDto;
import com.epam_final_project.dto.trainer.request.CreateTrainerRequest;
import com.epam_final_project.dto.trainer.request.UpdateTrainerProfileRequestDto;
import com.epam_final_project.dto.trainer.response.TrainerProfileDto;
import com.epam_final_project.dto.trainer.response.TrainerSummaryDto;
import com.epam_final_project.dto.trainer.response.TrainerRegistrationResponseDto;
import com.epam_final_project.dto.trainer.response.UpdateTrainerProfileResponseDto;
import com.epam_final_project.dto.training.response.TrainerTrainingResponseDto;
import com.epam_final_project.mapper.TraineeMapper;
import com.epam_final_project.mapper.TrainerMapper;
import com.epam_final_project.mapper.TrainingMapper;
import com.epam_final_project.service.AuthService;
import com.epam_final_project.service.TrainerService;
import com.epam_final_project.util.UsernameAndPasswordGenerator;
import com.epam_final_project.util.UsernameConflictChecker;
import org.springframework.transaction.annotation.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.naming.AuthenticationException;
import jakarta.persistence.EntityNotFoundException;
import java.time.LocalDate;
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
    private TraineeMapper traineeMapper;
    private UsernameConflictChecker usernameConflictChecker;

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
    public void setTraineeMapper(TraineeMapper traineeMapper) {
        this.traineeMapper = traineeMapper;
    }

    @Autowired
    public void setTrainingMapper(TrainingMapper trainingMapper) {
        this.trainingMapper = trainingMapper;
    }

    @Autowired
    public void setTrainingTypeDao(TrainingTypeDao trainingTypeDao) {
        this.trainingTypeDao = trainingTypeDao;
    }

    @Autowired
    public void setUsernameConflictChecker(UsernameConflictChecker usernameConflictChecker) {
        this.usernameConflictChecker = usernameConflictChecker;
    }

    @Override
    @Transactional
    public TrainerRegistrationResponseDto createTrainer(CreateTrainerRequest request) {
        log.info("creating trainer: {}", request);

        checkIfUsernameExistsAsTrainee(request);

        Trainer trainer = trainerMapper.toEntity(request);

        TrainingType specialization = getTrainingType(request);
        trainer.setSpecialization(specialization);

        validateTrainer(trainer);

        generateUsernameAndPassword(trainer);

        trainerDao.create(trainer);

        return new TrainerRegistrationResponseDto(trainer.getUsername(), trainer.getPassword());
    }

    private TrainingType getTrainingType(CreateTrainerRequest request) {
        String specializationName = request.getSpecialization();
        if (specializationName == null || specializationName.isBlank()) {
            throw new IllegalArgumentException("Specialization must not be null or empty");
        }

        TrainingType specialization = trainingTypeDao
                .findByTrainingTypeName(specializationName)
                .orElseThrow(() -> new IllegalArgumentException("Invalid specialization: " +
                        specializationName));
        return specialization;
    }

    private void checkIfUsernameExistsAsTrainee(CreateTrainerRequest request) {
        usernameConflictChecker.checkUsernameConflict(
                request.getFirstName(),
                request.getLastName(),
                true
        );
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
    public UpdateTrainerProfileResponseDto updateTrainer(
            UpdateTrainerProfileRequestDto request,
            String username,
            String password
    ) throws AuthenticationException {
        log.info("updating trainer with username {} with updated trainer: {}", username, request);
        if (!authService.checkTrainerUsernameAndPassword(username, password)) {
            throw new AuthenticationException("invalid credentials");
        }

        Optional<Trainer> foundTrainer = trainerDao.findByUsername(username);
        if (foundTrainer.isEmpty()) {
            throw new EntityNotFoundException("Trainer not found: " + username);
        }

        Trainer trainer = foundTrainer.get();
        trainer.setFirstName(request.getFirstName());
        trainer.setLastName(request.getLastName());
        trainer.setActive(request.getIsActive());

        trainerDao.update(trainer, trainer.getId());

        List<TraineeSummaryDto> trainees = getTraineeSummaryDtos(trainer);

        return getUpdateTrainerProfileResponseDto(trainer, trainees);
    }

    private UpdateTrainerProfileResponseDto getUpdateTrainerProfileResponseDto(
            Trainer updatedTrainer,
            List<TraineeSummaryDto> trainees
    ) {
        return new UpdateTrainerProfileResponseDto(
                updatedTrainer.getUsername(),
                updatedTrainer.getFirstName(),
                updatedTrainer.getLastName(),
                updatedTrainer.getSpecialization() != null ? updatedTrainer.getSpecialization().getTrainingTypeName() : null,
                updatedTrainer.isActive(),
                trainees
        );
    }

    private List<TraineeSummaryDto> getTraineeSummaryDtos(Trainer updatedTrainer) {
        List<TraineeSummaryDto> trainees = updatedTrainer
                .getTrainees()
                .stream()
                .map(traineeMapper::toSummaryDto)
                .toList();
        return trainees;
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<TrainerSummaryDto> selectTrainer(Integer trainerId) {
        log.info("selecting trainer with id {}", trainerId);
        return trainerDao
                .findById(trainerId)
                .map(trainerMapper::toDto);
    }

    @Override
    public Optional<TrainerProfileDto> selectTrainerByUsername(String username) {
        return trainerDao
                .findByUsername(username)
                .map(trainerMapper::toTrainerProfileDto);
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
            throws AuthenticationException {
        if (!authService.checkTrainerUsernameAndPassword(username, password)) {
            throw new AuthenticationException("invalid credentials provided");
        }

        Optional<Trainer> foundTrainer = trainerDao.findById(trainerId);

        foundTrainer.ifPresent(trainer -> {
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
            trainer.setActive(false);
        });
    }

    @Override
    @Transactional
    public void activateDeactivateTrainer(String username, boolean isActive, String authUsername, String authPassword)
            throws AuthenticationException {
        if (!authService.checkTrainerUsernameAndPassword(authUsername, authPassword)) {
            throw new AuthenticationException("invalid credentials");
        }

        Optional<Trainer> foundTrainer = trainerDao.findByUsername(username);
        if (foundTrainer.isEmpty()) {
            throw new EntityNotFoundException("Trainer not found: " + username);
        }

        Trainer trainer = foundTrainer.get();
        trainer.setActive(isActive);
    }

    @Override
    @Transactional(readOnly = true)
    public List<TrainerTrainingResponseDto> getTrainingsByUsernameAndCriteria(
            String trainerUsername,
            LocalDate fromDate,
            LocalDate toDate,
            String traineeName
    ) {
        return trainerDao.findTrainingsByUsernameAndCriteria(
                trainerUsername, fromDate, toDate, traineeName)
                .stream()
                .map(trainingMapper::toTrainerTrainingResponseDto)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<TrainerSummaryDto> getTrainersNotAssignedOnTraineeByTraineeUsername(String username) {
        return trainerDao
                .findTrainersNotAssignedOnTraineeByTraineeUsername(username)
                .stream()
                .map(trainerMapper::toDto)
                .toList();
    }
}