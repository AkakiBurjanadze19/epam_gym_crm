package com.epam_final_project.dao.impl;

import com.epam_final_project.dao.TrainerDao;
import com.epam_final_project.domain.Trainee;
import com.epam_final_project.domain.Trainer;
import com.epam_final_project.domain.Training;
import jakarta.persistence.*;
import jakarta.persistence.criteria.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.*;

@Repository
public class TrainerDaoImpl implements TrainerDao {
    private static final Logger log = LoggerFactory.getLogger(TrainerDaoImpl.class);

    @PersistenceContext
    private EntityManager em;

    @Override
    public void create(Trainer trainer) {
        em.persist(trainer);
        log.info("created trainer: {}", trainer);
    }

    @Override
    public void update(Trainer updatedTrainer, Integer trainerId) {
        Optional<Trainer> foundTrainer = Optional.ofNullable(em.find(Trainer.class, trainerId));

        log.info("updating trainer: {}", foundTrainer);

        foundTrainer.ifPresent(trainer -> {
            trainer.setFirstName(updatedTrainer.getFirstName());
            trainer.setLastName(updatedTrainer.getLastName());
            trainer.setUsername(updatedTrainer.getUsername());
            trainer.setPassword(updatedTrainer.getPassword());
            trainer.setActive(updatedTrainer.isActive());
            trainer.setSpecialization(updatedTrainer.getSpecialization());

            trainer.getTrainees().clear();
            trainer.getTrainees().addAll(updatedTrainer.getTrainees());

            trainer.getTrainings().clear();
            trainer.getTrainings().addAll(updatedTrainer.getTrainings());
        });

        log.info("updated trainer: {}", foundTrainer);
    }

    @Override
    public Optional<Trainer> findById(Integer trainerId) {
        log.info("select trainer with id {}", trainerId);
        return Optional.ofNullable(em.find(Trainer.class, trainerId));
    }

    @Override
    public List<Trainer> findAll() {
        String jpql = "SELECT t FROM Trainer t";
        TypedQuery<Trainer> query = em.createQuery(jpql, Trainer.class);

        return query.getResultList();
    }

    @Override
    public Optional<Trainer> findByUsername(String username) {
        String jpql = "SELECT t FROM Trainer t WHERE t.username = :username";

        TypedQuery<Trainer> query = em.createQuery(jpql, Trainer.class)
                .setParameter("username", username);

        return query.getResultStream().findFirst();
    }

    @Override
    public List<Training> findTrainingsByUsernameAndCriteria(
            String trainerUsername,
            LocalDate fromDate,
            LocalDate toDate,
            String traineeName
    ) {
        CriteriaBuilder cb = em.getCriteriaBuilder();

        CriteriaQuery<Training> query = cb.createQuery(Training.class);

        Root<Training> training = query.from(Training.class);

        Join<Training, Trainer> trainer = training.join("trainer");
        Join<Training, Trainee> trainee = training.join("trainee");

        List<Predicate> predicates = new ArrayList<>();

        predicates.add(cb.equal(trainer.get("username"), trainerUsername));

        // Criteria
        if (fromDate != null) {
            predicates.add(cb.greaterThanOrEqualTo(
                    training.get("date"),
                    fromDate
            ));
        }

        if (toDate != null) {
            predicates.add(cb.lessThanOrEqualTo(
                    training.get("date"),
                    toDate
            ));
        }

        if (traineeName != null) {
            predicates.add(cb.equal(
                    trainee.get("firstName"),
                    traineeName
            ));
        }

        query.select(training)
                .where(predicates.toArray(new Predicate[0]))
                .orderBy(cb.asc(training.get("date")));

        return em.createQuery(query).getResultList();
    }

    @Override
    public List<Trainer> findTrainersNotAssignedOnTraineeByTraineeUsername(String username) {
        String jpql =
                """
                SELECT tr FROM Trainer tr
                WHERE tr NOT IN (
                    SELECT t2 FROM Trainee t
                    JOIN t.trainers t2
                    WHERE t.username = :username
                )
                """;

        TypedQuery<Trainer> query = em.createQuery(jpql, Trainer.class)
                .setParameter("username", username);

        return query.getResultList();
    }
}