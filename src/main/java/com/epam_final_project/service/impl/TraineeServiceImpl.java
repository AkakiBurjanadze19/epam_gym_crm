package com.epam_final_project.service.impl;

import com.epam_final_project.dao.TraineeDao;
import com.epam_final_project.dao.TrainerDao;
import com.epam_final_project.domain.Trainee;
import com.epam_final_project.dto.trainee.request.CreateTraineeRequest;
import com.epam_final_project.dto.trainee.request.UpdateTraineeProfileRequestDto;
import com.epam_final_project.dto.trainee.response.TraineeProfileDto;
import com.epam_final_project.dto.trainee.response.UpdateTraineeProfileResponseDto;
import com.epam_final_project.dto.trainee.response.TraineeRegistrationResponseDto;
import com.epam_final_project.dto.trainer.response.TrainerSummaryDto;
import com.epam_final_project.dto.training.response.TraineeTrainingResponseDto;
import com.epam_final_project.mapper.TraineeMapper;
import com.epam_final_project.mapper.TrainerMapper;
import com.epam_final_project.mapper.TrainingMapper;
import com.epam_final_project.service.AuthService;
import com.epam_final_project.service.TraineeService;
import com.epam_final_project.util.UsernameAndPasswordGenerator;
import com.epam_final_project.util.UsernameConflictChecker;
import org.springframework.transaction.annotation.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.naming.AuthenticationException;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class TraineeServiceImpl implements TraineeService {
    private static final Logger log = LoggerFactory.getLogger(TraineeServiceImpl.class);
    private TraineeDao traineeDao;
    private TrainerDao trainerDao;
    private AuthService authService;
    private TraineeMapper traineeMapper;
    private TrainerMapper trainerMapper;
    private TrainingMapper trainingMapper;
    private UsernameConflictChecker usernameConflictChecker;
    private EntityManager em;

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
    public void setTrainerMapper(TrainerMapper trainerMapper) {
        this.trainerMapper = trainerMapper;
    }

    @Autowired
    public void setTrainingMapper(TrainingMapper trainingMapper) {
        this.trainingMapper = trainingMapper;
    }

    @Autowired
    public void setUsernameConflictChecker(UsernameConflictChecker usernameConflictChecker) {
        this.usernameConflictChecker = usernameConflictChecker;
    }

    @Autowired
    public void setEntityManager(EntityManager em) {
        this.em = em;
    }

    @Override
    @Transactional
    public TraineeRegistrationResponseDto createTrainee(CreateTraineeRequest request) {
        log.info("creating trainee: {}", request);

        checkIfUsernameExistsAsTrainer(request);

        Trainee trainee = traineeMapper.toEntity(request);

        validateTrainee(trainee);

        generateUsernameAndPassword(trainee);

        traineeDao.create(trainee);

        return new TraineeRegistrationResponseDto(trainee.getUsername(), trainee.getPassword());
    }

    private void checkIfUsernameExistsAsTrainer(CreateTraineeRequest request) {
        usernameConflictChecker.checkUsernameConflict(
                request.getFirstName(),
                request.getLastName(),
                false
        );
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
    public UpdateTraineeProfileResponseDto updateTrainee(
            UpdateTraineeProfileRequestDto request,
            String username,
            String password
    ) throws AuthenticationException {
        log.info("updating trainee with username {} with updated trainee: {}", username, request);
        if (!authService.checkTraineeUsernameAndPassword(username, password)) {
            throw new AuthenticationException("invalid credentials provided");
        }

        Optional<Trainee> foundTrainee = traineeDao.findByUsername(username);
        if (foundTrainee.isEmpty()) {
            throw new EntityNotFoundException("Trainee not found: " + username);
        }

        Trainee trainee = getUpdatedTrainee(request, foundTrainee);

        traineeDao.update(trainee, trainee.getId());

        List<TrainerSummaryDto> trainerSummaryDtos = getTrainerSummaryDtos(trainee);

        return getUpdateTraineeProfileResponseDto(trainee, trainerSummaryDtos);
    }

    private UpdateTraineeProfileResponseDto getUpdateTraineeProfileResponseDto(
            Trainee trainee,
            List<TrainerSummaryDto> trainerSummaryDtos
    ) {
        return new UpdateTraineeProfileResponseDto(
                trainee.getUsername(),
                trainee.getFirstName(),
                trainee.getLastName(),
                trainee.getDateOfBirth(),
                trainee.getAddress(),
                trainee.isActive(),
                trainerSummaryDtos
        );
    }

    private List<TrainerSummaryDto> getTrainerSummaryDtos(Trainee trainee) {
        List<TrainerSummaryDto> trainerSummaryDtos = trainee
                .getTrainers()
                .stream()
                .map(trainerMapper::toDto)
                .toList();
        return trainerSummaryDtos;
    }

    private Trainee getUpdatedTrainee(
            UpdateTraineeProfileRequestDto request,
            Optional<Trainee> foundTrainee
    ) {
        Trainee trainee = foundTrainee.get();
        trainee.setFirstName(request.getFirstName());
        trainee.setLastName(request.getLastName());
        trainee.setDateOfBirth(request.getDateOfBirth());
        trainee.setAddress(request.getAddress());
        trainee.setActive(request.getIsActive());
        return trainee;
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
    public Optional<TraineeProfileDto> selectTrainee(Integer traineeId) {
        log.info("selecting trainee with id {}", traineeId);
        return traineeDao
                .findById(traineeId)
                .map(traineeMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<TraineeProfileDto> selectTraineeByUsername(String username) {
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
            trainee.setActive(false);
        });
    }

    @Override
    @Transactional
    public void activateDeactivateTrainee(
            String username,
            boolean isActive,
            String authUsername,
            String authPassword
    ) throws AuthenticationException {
        if (!authService.checkTraineeUsernameAndPassword(authUsername, authPassword)) {
            throw new AuthenticationException("invalid credentials provided");
        }

        Optional<Trainee> foundTrainee = traineeDao.findByUsername(username);
        if (foundTrainee.isEmpty()) {
            throw new EntityNotFoundException("Trainee not found: " + username);
        }

        Trainee trainee = foundTrainee.get();
        trainee.setActive(isActive);
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
    public List<TraineeTrainingResponseDto> getTrainingsByUsernameAndCriteria(
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
                .map(trainingMapper::toTraineeTrainingResponseDto)
                .toList();
    }

    @Override
    @Transactional
    public List<TrainerSummaryDto> updateTraineeTrainers(
            String traineeUsername,
            List<String> updatedTrainerUsernames,
            String username,
            String password
    ) throws AuthenticationException {
        if (!authService.checkTraineeUsernameAndPassword(username, password)) {
            throw new AuthenticationException("invalid credentials provided");
        }

        traineeDao.updateTrainers(traineeUsername, updatedTrainerUsernames);

        em.flush();
        Optional<Trainee> foundTrainee = traineeDao.findByUsername(traineeUsername);
        if (foundTrainee.isEmpty()) {
            throw new EntityNotFoundException("Trainee not found: " + traineeUsername);
        }

        em.refresh(foundTrainee.get());

        List<TrainerSummaryDto> trainerDtos = getTrainerSummaryDtos(foundTrainee.get());

        return trainerDtos;
    }
}