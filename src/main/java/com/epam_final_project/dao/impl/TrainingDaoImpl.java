package com.epam_final_project.dao.impl;

import com.epam_final_project.dao.TrainingDao;
import com.epam_final_project.domain.Training;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public class TrainingDaoImpl implements TrainingDao {
    private static final Logger log = LoggerFactory.getLogger(TrainingDaoImpl.class);

    @PersistenceContext
    private EntityManager em;

    @Override
    public void create(Training training) {
        em.persist(training);
    }

    @Override
    public Optional<Training> findById(Integer trainingId) {
        log.info("selecting training with id {}", trainingId);
        return Optional.ofNullable(em.find(Training.class, trainingId));
    }
}