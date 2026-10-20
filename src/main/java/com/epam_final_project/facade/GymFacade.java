package com.epam_final_project.facade;

import com.epam_final_project.domain.TrainingType;
import com.epam_final_project.dto.*;
import com.epam_final_project.service.TraineeService;
import com.epam_final_project.service.TrainerService;
import com.epam_final_project.service.TrainingService;
import com.epam_final_project.service.TrainingTypeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.naming.AuthenticationException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Component
public class GymFacade {
    private static final Logger log = LoggerFactory.getLogger(GymFacade.class);
    private final TraineeService traineeService;
    private final TrainerService trainerService;
    private final TrainingService trainingService;
    private final TrainingTypeService trainingTypeService;

    public GymFacade(
            TraineeService traineeService,
            TrainerService trainerService,
            TrainingService trainingService,
            TrainingTypeService trainingTypeService
    ) {
        this.traineeService = traineeService;
        this.trainerService = trainerService;
        this.trainingService = trainingService;
        this.trainingTypeService = trainingTypeService;
        log.info("GymFacade initialized with all service dependencies");
    }

    /* Trainee Operations */
    public void createTrainee(CreateTraineeRequest request) {
        log.info("Creating trainee: {}", request);
        traineeService.createTrainee(request);
    }

    public void updateTrainee(
            CreateTraineeRequest request,
            Integer traineeId,
            String username,
            String password
    ) throws AuthenticationException {
        log.info("Updating trainee with ID: {} with data: {}", traineeId, request);
        traineeService.updateTrainee(request, traineeId, username, password);
        log.info("Trainee with ID: {} updated successfully", traineeId);
    }

    public void deleteTrainee(Integer traineeId, String username, String password)
            throws AuthenticationException {
        log.info("Deleting trainee with ID: {}", traineeId);
        traineeService.deleteTrainee(traineeId, username, password);
        log.info("Trainee with ID: {} deleted successfully", traineeId);
    }

    public Optional<TraineeDto> selectTrainee(Integer traineeId) {
        log.info("Selecting trainee with ID: {}", traineeId);
        Optional<TraineeDto> trainee = traineeService.selectTrainee(traineeId);
        if (trainee.isPresent()) {
            log.info("Trainee found with ID: {}", traineeId);
        } else {
            log.warn("No trainee found with ID: {}", traineeId);
        }
        return trainee;
    }

    public Optional<TraineeDto> selectTraineeByUsername(String username) {
        log.info("selecting trainee with username: {}", username);
        return traineeService.selectTraineeByUsername(username);
    }

    public void changeTraineePassword(
            String updatedPassword,
            Integer traineeId,
            String username,
            String password
    ) throws AuthenticationException {
        traineeService.changeTraineePassword(updatedPassword, traineeId, username, password);
    }

    public void activateTrainee(
            Integer traineeId,
            String username,
            String password
    ) throws AuthenticationException {
        log.info("activating trainee with id {}", traineeId);
        traineeService.activateTrainee(traineeId, username, password);
    }

    public void deActivateTrainee(
            Integer traineeId,
            String username,
            String password
    ) throws AuthenticationException {
        log.info("deactivating trainee with id {}", traineeId);
        traineeService.deActivateTrainee(traineeId, username, password);
    }

    public void deleteTraineeByUsername(String traineeUsername, String username, String password)
            throws AuthenticationException {
        log.info("deleting trainee with username: {}", username);
        traineeService.deleteTraineeByUsername(traineeUsername, username, password);
    }

    public List<TrainingDto> getTrainingsByUsernameAndCriteria(
            String username,
            LocalDate fromDate,
            LocalDate toDate,
            String trainerName,
            String trainingTypeName
    ) {
        return traineeService.getTrainingsByUsernameAndCriteria(
                username,
                fromDate,
                toDate,
                trainerName,
                trainingTypeName
        );
    }

    public void updateTraineeTrainers(
            String traineeUsername,
            List<String> updatedTrainerUsernames,
            String username,
            String password
    ) throws AuthenticationException {
        traineeService.updateTraineeTrainers(
                traineeUsername,
                updatedTrainerUsernames,
                username,
                password
        );
    }

    /* Trainer Operations */
    public void createTrainer(CreateTrainerRequest request) {
        log.info("Creating trainer: {}", request);
        trainerService.createTrainer(request);
    }

    public void updateTrainer(
            CreateTrainerRequest request,
            Integer trainerId,
            String username,
            String password
    ) throws AuthenticationException {
        log.info("Updating trainer with ID: {} with data: {}", trainerId, request);
        trainerService.updateTrainer(request, trainerId, username, password);
        log.info("Trainer with ID: {} updated successfully", trainerId);
    }

    public Optional<TrainerDto> selectTrainer(Integer trainerId) {
        log.info("Selecting trainer with ID: {}", trainerId);
        Optional<TrainerDto> trainer = trainerService.selectTrainer(trainerId);
        if (trainer.isPresent()) {
            log.info("Trainer found with ID: {}", trainerId);
        } else {
            log.warn("No trainer found with ID: {}", trainerId);
        }
        return trainer;
    }

    public Optional<TrainerDto> selectTrainerByUsername(String username) {
        log.info("selecting trainer with username: {}", username);
        return trainerService.selectTrainerByUsername(username);
    }

    public void changeTrainerPassword(
            String updatedPassword,
            Integer trainerId,
            String username,
            String password
    ) throws AuthenticationException {
        log.info("changing password for trainer with id {}", trainerId);
        trainerService.changeTrainerPassword(updatedPassword, trainerId, username, password);
    }

    public void activateTrainer(Integer trainerId, String username, String password)
            throws AuthenticationException {
        log.info("activating trainer with id {}", trainerId);
        trainerService.activateTrainer(trainerId, username, password);
    }

    public void deActivateTrainer(Integer trainerId, String username, String password)
            throws AuthenticationException {
        log.info("deactivating trainer with id {}", trainerId);
        trainerService.deActivateTrainer(trainerId, username, password);
    }

    public List<TrainingDto> getTrainingsByUsernameAndCriteria(
            String trainerUsername,
            LocalDate fromDate,
            LocalDate toDate,
            String traineeName
    ) {
        return trainerService.getTrainingsByUsernameAndCriteria(
                trainerUsername,
                fromDate,
                toDate,
                traineeName
        );
    }

    public List<TrainerDto> getTrainersNotAssignedOnTraineeByTraineeUsername(String username) {
        return trainerService.getTrainersNotAssignedOnTraineeByTraineeUsername(username);
    }

    /* Training Operations */
    public void createTraining(CreateTrainingRequest request) {
        log.info("Creating training: {}", request);
        trainingService.createTraining(request);
    }

    public Optional<TrainingDto> selectTraining(Integer trainingId) {
        log.info("Selecting training with ID: {}", trainingId);
        Optional<TrainingDto> training = trainingService.selectTraining(trainingId);
        if (training.isPresent()) {
            log.info("Training found with ID: {}", trainingId);
        } else {
            log.warn("No training found with ID: {}", trainingId);
        }
        return training;
    }

    /* TrainingType operations */
    public Optional<TrainingType> selectTrainingType(Integer trainingTypeId) {
        return trainingTypeService.selectTrainingType(trainingTypeId);
    }

    public List<TrainingType> getAllTrainingTypes() {
        return trainingTypeService.getAllTrainingTypes();
    }
}