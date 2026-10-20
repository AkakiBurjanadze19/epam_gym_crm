package com.epam_final_project.service.impl;

import com.epam_final_project.dao.TraineeDao;
import com.epam_final_project.dao.TrainerDao;
import com.epam_final_project.dao.TrainingDao;
import com.epam_final_project.domain.Trainee;
import com.epam_final_project.domain.Trainer;
import com.epam_final_project.domain.Training;
import com.epam_final_project.dto.CreateTraineeRequest;
import com.epam_final_project.dto.CreateTrainingRequest;
import com.epam_final_project.dto.TrainingDto;
import com.epam_final_project.mapper.TrainingMapper;
import com.epam_final_project.service.TrainingService;
import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class TrainingServiceImpl implements TrainingService {
    private static final Logger log = LoggerFactory.getLogger(TrainingServiceImpl.class);
    private TrainingDao trainingDao;
    private TraineeDao traineeDao;
    private TrainerDao trainerDao;
    private TrainingMapper trainingMapper;

    @Autowired
    public void setTrainingDao(TrainingDao trainingDao) {
        this.trainingDao = trainingDao;
    }

    @Autowired
    public void setTraineeDao(TraineeDao traineeDao) {
        this.traineeDao = traineeDao;
    }

    @Autowired
    public void setTrainerDao(TrainerDao trainerDao) {
        this.trainerDao = trainerDao;
    }

    @Autowired
    public void setTrainingMapper(TrainingMapper trainingMapper) {
        this.trainingMapper = trainingMapper;
    }

    @Override
    @Transactional
    public void createTraining(CreateTrainingRequest request) {
        Trainee trainee = traineeDao
                .findByUsername(request.getTraineeUsername())
                .orElseThrow(() -> new EntityNotFoundException("Trainee not found"));

        Trainer trainer = trainerDao
                .findByUsername(request.getTrainerUsername())
                .orElseThrow(() -> new EntityNotFoundException("Trainer not found"));

        Training training = trainingMapper.toEntity(request);
        training.setTrainee(trainee);
        training.setTrainer(trainer);
        training.setType(trainer.getSpecialization());

        validateTraining(training);

        trainingDao.create(training);
    }

    private static void validateTraining(Training training) {
        if (training.getName() == null || training.getName().isEmpty()) {
            throw new IllegalArgumentException("training name must not be null or empty");
        }

        if (training.getDate() == null) {
            throw new IllegalArgumentException("training date must be provided. it cannot be null");
        }

        if (training.getDuration() <= 0) {
            throw new IllegalArgumentException("training duration must be a positive number");
        }
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<TrainingDto> selectTraining(Integer trainingId) {
        log.info("selecting training with id {}", trainingId);
        return trainingDao
                .findById(trainingId)
                .map(trainingMapper::toDto);
    }
}