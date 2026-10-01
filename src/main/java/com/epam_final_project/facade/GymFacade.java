package com.epam_final_project.facade;

import com.epam_final_project.domain.Trainee;
import com.epam_final_project.domain.Trainer;
import com.epam_final_project.domain.Training;
import com.epam_final_project.service.TraineeService;
import com.epam_final_project.service.TrainerService;
import com.epam_final_project.service.TrainingService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class GymFacade {
    private static final Logger log = LoggerFactory.getLogger(GymFacade.class);
    private final TraineeService traineeService;
    private final TrainerService trainerService;
    private final TrainingService trainingService;

    public GymFacade(TraineeService traineeService, TrainerService trainerService, TrainingService trainingService) {
        this.traineeService = traineeService;
        this.trainerService = trainerService;
        this.trainingService = trainingService;
        log.info("GymFacade initialized with all service dependencies");
    }

    /* Trainee Operations */
    public void createTrainee(Trainee trainee) {
        log.info("Creating trainee: {}", trainee);
        traineeService.createTrainee(trainee);
        log.info("Trainee created successfully with ID: {}", trainee.getUserId());
    }

    public void updateTrainee(Trainee updatedTrainee, Integer traineeId) {
        log.info("Updating trainee with ID: {} with data: {}", traineeId, updatedTrainee);
        traineeService.updateTrainee(updatedTrainee, traineeId);
        log.info("Trainee with ID: {} updated successfully", traineeId);
    }

    public void deleteTrainee(Integer traineeId) {
        log.info("Deleting trainee with ID: {}", traineeId);
        traineeService.deleteTrainee(traineeId);
        log.info("Trainee with ID: {} deleted successfully", traineeId);
    }

    public Optional<Trainee> selectTrainee(Integer traineeId) {
        log.info("Selecting trainee with ID: {}", traineeId);
        Optional<Trainee> trainee = traineeService.selectTrainee(traineeId);
        if (trainee.isPresent()) {
            log.info("Trainee found with ID: {}", traineeId);
        } else {
            log.warn("No trainee found with ID: {}", traineeId);
        }
        return trainee;
    }

    /* Trainer Operations */
    public void createTrainer(Trainer trainer) {
        log.info("Creating trainer: {}", trainer);
        trainerService.createTrainer(trainer);
        log.info("Trainer created successfully with ID: {}", trainer.getUserId());
    }

    public void updateTrainer(Trainer updatedTrainer, Integer trainerId) {
        log.info("Updating trainer with ID: {} with data: {}", trainerId, updatedTrainer);
        trainerService.updateTrainer(updatedTrainer, trainerId);
        log.info("Trainer with ID: {} updated successfully", trainerId);
    }

    public Optional<Trainer> selectTrainer(Integer trainerId) {
        log.info("Selecting trainer with ID: {}", trainerId);
        Optional<Trainer> trainer = trainerService.selectTrainer(trainerId);
        if (trainer.isPresent()) {
            log.info("Trainer found with ID: {}", trainerId);
        } else {
            log.warn("No trainer found with ID: {}", trainerId);
        }
        return trainer;
    }

    /* Training Operations */
    public void createTraining(Training training) {
        log.info("Creating training: {}", training);
        trainingService.createTraining(training);
        log.info("Training created successfully with ID: {}", training.getId());
    }

    public Optional<Training> selectTraining(Integer trainingId) {
        log.info("Selecting training with ID: {}", trainingId);
        Optional<Training> training = trainingService.selectTraining(trainingId);
        if (training.isPresent()) {
            log.info("Training found with ID: {}", trainingId);
        } else {
            log.warn("No training found with ID: {}", trainingId);
        }
        return training;
    }
}
