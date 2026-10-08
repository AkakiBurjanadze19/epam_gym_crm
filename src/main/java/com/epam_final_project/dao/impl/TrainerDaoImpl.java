package com.epam_final_project.dao.impl;

import com.epam_final_project.dao.TrainerDao;
import com.epam_final_project.domain.Trainer;
import com.epam_final_project.util.UsernameAndPasswordGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class TrainerDaoImpl implements TrainerDao {
    private static final Logger log = LoggerFactory.getLogger(TrainerDaoImpl.class);

    private Map<Integer, Trainer> trainers;

    @Autowired
    public void setTrainers(Map<Integer, Trainer> trainers) {
        this.trainers = trainers;
    }

    @Override
    public void create(Trainer trainer) {
        int nextId = UsernameAndPasswordGenerator.computeNextId(trainers);
        trainer.setUserId(nextId);

        trainers.put(trainer.getUserId(), trainer);

        log.info("creating trainer: {}", trainer);

        log.info("trainers count: {}", trainers.size());
        log.info("trainers: {}", trainers);
    }

    @Override
    public void update(Trainer updatedTrainer, Integer trainerId) {
        Optional<Trainer> foundTrainer = trainers
                .values()
                .stream()
                .filter(t -> t.getUserId().equals(trainerId))
                .findFirst();

        log.info("updating trainer: {}", foundTrainer);

        foundTrainer.ifPresent(trainer -> {
            trainer.setFirstName(updatedTrainer.getFirstName());
            trainer.setLastName(updatedTrainer.getLastName());
            trainer.setUsername(updatedTrainer.getUsername());
            trainer.setPassword(updatedTrainer.getPassword());
            trainer.setActive(updatedTrainer.isActive());
            trainer.setSpecialization(updatedTrainer.getSpecialization());
        });

        log.info("updated trainer: {}", foundTrainer);

        if (foundTrainer.isEmpty()) {
            throw new IllegalStateException("trainer with id " + trainerId + " not found");
        }
    }

    @Override
    public Optional<Trainer> findById(Integer trainerId) {
        log.info("select trainer with id {}", trainerId);
        return Optional.ofNullable(trainers.get(trainerId));
    }

    @Override
    public List<Trainer> findAll() {
        return (List<Trainer>) trainers.values();
    }
}