package com.epam_final_project.service.impl;

import com.epam_final_project.dao.TraineeDao;
import com.epam_final_project.dao.impl.TraineeDaoImpl;
import com.epam_final_project.domain.Trainee;
import com.epam_final_project.service.TraineeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class TraineeServiceImpl implements TraineeService {
    private static final Logger log = LoggerFactory.getLogger(TraineeServiceImpl.class);
    private TraineeDao traineeDao;

    @Autowired
    public void setTraineeDao(TraineeDao traineeDao) {
        this.traineeDao = traineeDao;
    }

    @Override
    public void createTrainee(Trainee trainee) {
        log.info("creating trainee: {}", trainee);
        traineeDao.create(trainee);
    }

    @Override
    public void updateTrainee(Trainee updatedTrainee, Integer traineeId) {
        log.info("updating trainee with id {} with updated trainee: {}", traineeId, updatedTrainee);
        traineeDao.update(updatedTrainee, traineeId);
    }

    @Override
    public void deleteTrainee(Integer traineeId) {
        log.info("deleting trainee with id {}", traineeId);
        traineeDao.delete(traineeId);
    }

    @Override
    public Optional<Trainee> selectTrainee(Integer traineeId) {
        log.info("selecting trainee with id {}", traineeId);
        return traineeDao.select(traineeId);
    }
}
