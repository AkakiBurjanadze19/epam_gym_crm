package com.epam_final_project.dao.impl;

import com.epam_final_project.dao.TrainingDao;
import com.epam_final_project.domain.Training;
import com.epam_final_project.util.UsernameAndPasswordGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.Map;
import java.util.Optional;

@Repository
public class TrainingDaoImpl implements TrainingDao {
    private static final Logger log = LoggerFactory.getLogger(TrainingDaoImpl.class);
    private Map<Integer, Training> trainings;

    @Autowired
    public void setTrainings(Map<Integer, Training> trainings) {
        this.trainings = trainings;
    }

    @Override
    public void create(Training training) {
        int nextId = UsernameAndPasswordGenerator.computeNextId(trainings);
        training.setId(nextId);

        trainings.put(training.getId(), training);

        log.info("created training: {}", training);
        log.info("trainings count: {}", trainings.size());
    }

    @Override
    public Optional<Training> findById(Integer trainingId) {
        log.info("selecting training with id {}", trainingId);
        return Optional.ofNullable(trainings.get(trainingId));
    }
}