package com.epam_final_project.dao.impl;

import com.epam_final_project.dao.TraineeDao;
import com.epam_final_project.domain.Trainee;
import com.epam_final_project.domain.Trainer;
import com.epam_final_project.util.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Component
public class TraineeDaoImpl implements TraineeDao {
    private static final Logger log = LoggerFactory.getLogger(TraineeDaoImpl.class);
    private Map<Integer, Trainee> trainees;

    @Autowired
    public void setTrainees(Map<Integer, Trainee> trainees) {
        this.trainees = trainees;
    }

    @Override
    public void create(Trainee trainee) {
        int nextId = Utils.computeNextId(trainees);
        trainee.setUserId(nextId);

        if (trainee.getUsername() == null || trainee.getUsername().isEmpty()) {
            trainee.setUsername(Utils.generateUsername(
                    trainee.getFirstName(),
                    trainee.getLastName(),
                    trainees
                            .values()
                            .stream()
                            .map(Trainee::getUsername)
                            .toList()
            ));
        }

        if (trainee.getPassword() == null || trainee.getPassword().isEmpty()) {
            trainee.setPassword(Utils.generatePassword());
        }

        trainees.put(trainee.getUserId(), trainee);

        log.info("creating trainee: {}", trainee);
        log.info("trainees size: {}", trainees.size());
        log.info("trainees: {}", trainees);
    }

    @Override
    public void update(Trainee updatedTrainee, Integer traineeId) {
        Optional<Trainee> foundTrainee = trainees
                .values()
                .stream()
                .filter(t -> t.getUserId().equals(traineeId))
                .findFirst();

        log.info("updating trainee: {}", foundTrainee);

        foundTrainee.ifPresent(trainee -> {
            trainee.setFirstName(updatedTrainee.getFirstName());
            trainee.setLastName(updatedTrainee.getLastName());
            trainee.setUsername(updatedTrainee.getUsername());
            trainee.setPassword(updatedTrainee.getPassword());
            trainee.setActive(updatedTrainee.isActive());
            trainee.setDateOfBirth(updatedTrainee.getDateOfBirth());
            trainee.setAddress(updatedTrainee.getAddress());
        });

        log.info("updated trainee: {}", foundTrainee);

        if (foundTrainee.isEmpty()) {
            throw new IllegalStateException("trainee with id " + traineeId + " not found");
        }
    }

    @Override
    public void delete(Integer traineeId) {
        log.info("deleting trainee with id {}", traineeId);
        log.info("trainees size: {}", trainees.size());
        trainees.remove(traineeId);
    }

    @Override
    public Optional<Trainee> select(Integer traineeId) {
        log.info("selecting trainee with id {}", traineeId);
        return Optional.ofNullable(trainees.get(traineeId));
    }
}