package com.epam_final_project.service.impl;

import com.epam_final_project.dao.TrainingDao;
import com.epam_final_project.domain.Training;
import com.epam_final_project.service.TrainingService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
    public void createTraining(Training training) {
        log.info("creating training: {}", training);
        trainingDao.create(training);
    }

    @Override
    public Optional<Training> selectTraining(Integer trainingId) {
        log.info("selecting training with id {}", trainingId);
        return trainingDao.findById(trainingId);
    }
}