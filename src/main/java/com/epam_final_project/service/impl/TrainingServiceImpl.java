package com.epam_final_project.service.impl;

import com.epam_final_project.dao.TrainingDao;
import com.epam_final_project.domain.Training;
import com.epam_final_project.service.TrainingService;
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

    @Autowired
    public void setTrainingDao(TrainingDao trainingDao) {
        this.trainingDao = trainingDao;
    }

    @Override
    @Transactional
    public void createTraining(Training training) {
        log.info("creating training: {}", training);
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
    public Optional<Training> selectTraining(Integer trainingId) {
        log.info("selecting training with id {}", trainingId);
        return trainingDao.findById(trainingId);
    }
}