package com.epam_final_project.dao.impl;

import com.epam_final_project.dao.TraineeDao;
import com.epam_final_project.domain.Trainee;
import com.epam_final_project.util.UsernameAndPasswordGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public class TraineeDaoImpl implements TraineeDao {
    private static final Logger log = LoggerFactory.getLogger(TraineeDaoImpl.class);
    private Map<Integer, Trainee> trainees;

    @Autowired
    public void setTrainees(Map<Integer, Trainee> trainees) {
        this.trainees = trainees;
    }

    @Override
    public void create(Trainee trainee) {
        int nextId = UsernameAndPasswordGenerator.computeNextId(trainees);
        trainee.setUserId(nextId);

        trainees.put(trainee.getUserId(), trainee);

        log.info("created trainee: {}", trainee);
        log.info("trainees size: {}", trainees.size());
    }

    @Override
    public void update(Trainee updatedTrainee, Integer traineeId) {
        Optional<Trainee> foundTrainee = Optional.of(trainees.get(traineeId));

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
    }

    @Override
    public void delete(Integer traineeId) {
        log.info("deleting trainee with id {}", traineeId);
        log.info("trainees size: {}", trainees.size());
        trainees.remove(traineeId);
    }

    @Override
    public Optional<Trainee> findById(Integer traineeId) {
        log.info("selecting trainee with id {}", traineeId);
        return Optional.ofNullable(trainees.get(traineeId));
    }

    @Override
    public List<Trainee> findAll() {
        return new ArrayList<>(trainees.values());
    }
}