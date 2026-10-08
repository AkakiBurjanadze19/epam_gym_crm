package com.epam_final_project.service.impl;

import com.epam_final_project.dao.TrainerDao;
import com.epam_final_project.domain.Trainer;
import com.epam_final_project.service.TrainerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class TrainerServiceImpl implements TrainerService {
    private static final Logger log = LoggerFactory.getLogger(TrainerServiceImpl.class);
    private TrainerDao trainerDao;

    @Autowired
    public void setTrainerDao(TrainerDao trainerDao) {
        this.trainerDao = trainerDao;
    }

    @Override
    public void createTrainer(Trainer trainer) {
        log.info("creating trainer: {}", trainer);
        trainerDao.create(trainer);
    }

    @Override
    public void updateTrainer(Trainer updatedTrainer, Integer trainerId) {
        log.info("updating trainer with id {} with updated trainer: {}", trainerId, updatedTrainer);
        trainerDao.update(updatedTrainer, trainerId);
    }

    @Override
    public Optional<Trainer> selectTrainer(Integer trainerId) {
        log.info("selecting trainer with id {}", trainerId);
        return trainerDao.findById(trainerId);
    }
}
