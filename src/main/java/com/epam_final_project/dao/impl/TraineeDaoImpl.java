package com.epam_final_project.dao.impl;

import com.epam_final_project.dao.TraineeDao;
import com.epam_final_project.domain.Trainee;
import com.epam_final_project.domain.Trainer;
import com.epam_final_project.domain.Training;
import com.epam_final_project.domain.TrainingType;
import jakarta.persistence.*;
import jakarta.persistence.criteria.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.*;

@Repository
public class TraineeDaoImpl implements TraineeDao {
    private static final Logger log = LoggerFactory.getLogger(TraineeDaoImpl.class);

    @PersistenceContext
    private EntityManager em;

    @Override
    public void create(Trainee trainee) {
        em.persist(trainee);
        log.info("created trainee: {}", trainee);
    }

    @Override
    public void update(Trainee updatedTrainee, Integer traineeId) {
        Optional<Trainee> foundTrainee = Optional.ofNullable(em.find(Trainee.class, traineeId));

        log.info("updating trainee: {}", foundTrainee);

        foundTrainee.ifPresent(trainee -> {
            trainee.setFirstName(updatedTrainee.getFirstName());
            trainee.setLastName(updatedTrainee.getLastName());
            trainee.setDateOfBirth(updatedTrainee.getDateOfBirth());
            trainee.setAddress(updatedTrainee.getAddress());
            trainee.setActive(updatedTrainee.isActive());
        });

        log.info("updated trainee: {}", foundTrainee);
    }

    @Override
    public void delete(Integer traineeId) {
        Trainee trainee = em.find(Trainee.class, traineeId);

        if (trainee != null) {
            em.remove(trainee);
        }
    }

    @Override
    public Optional<Trainee> findById(Integer traineeId) {
        log.info("selecting trainee with id {}", traineeId);
        return Optional.ofNullable(em.find(Trainee.class, traineeId));
    }

    @Override
    public List<Trainee> findAll() {
        String jpql = "SELECT t FROM Trainee t";

        TypedQuery<Trainee> query = em.createQuery(jpql, Trainee.class);

        return query.getResultList();
    }

    @Override
    public Optional<Trainee> findByUsername(String username) {
        String jpql = "SELECT t FROM Trainee t WHERE t.username = :username";

        TypedQuery<Trainee> query = em.createQuery(jpql, Trainee.class)
                .setParameter("username", username);

        return query.getResultStream().findFirst();
    }

    @Override
    public void deleteByUsername(String username) {
        Optional<Trainee> foundTrainee = findByUsername(username);

        foundTrainee.ifPresent(t -> em.remove(t));
    }

    @Override
    public List<Training> findTrainingsByUsernameAndCriteria(
            String traineeUsername,
            LocalDate fromDate,
            LocalDate toDate,
            String trainerName,
            String trainingTypeName
    ) {
        CriteriaBuilder cb = em.getCriteriaBuilder();

        CriteriaQuery<Training> query = cb.createQuery(Training.class);

        Root<Training> training = query.from(Training.class);

        training.fetch("trainee");
        training.fetch("trainer");
        training.fetch("type");

        Join<Training, Trainee> trainee = training.join("trainee");
        Join<Training, Trainer> trainer = training.join("trainer");
        Join<Training, TrainingType> trainingType = training.join("type");

        List<Predicate> predicates = new ArrayList<>();

        predicates.add(cb.equal(trainee.get("username"), traineeUsername));

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

        if (trainerName != null) {
            String name = trainerName.trim().toLowerCase();

            Expression<String> firstName = cb.lower(trainer.get("firstName"));
            Expression<String> lastName = cb.lower(trainer.get("lastName"));
            Expression<String> fullName = cb.concat(
                    cb.concat(firstName, ""),
                    lastName
            );

            predicates.add(cb.or(
                    cb.equal(firstName, name),
                    cb.equal(lastName, name),
                    cb.equal(fullName, name)
            ));
        }

        if (trainingTypeName != null) {
            predicates.add(cb.equal(
                    trainingType.get("trainingTypeName"),
                    trainingTypeName
            ));
        }

        query.select(training)
                .where(predicates.toArray(new Predicate[0]))
                .orderBy(cb.asc(training.get("date")));

        return em.createQuery(query).getResultList();
    }

    @Override
    public void updateTrainers(
            String traineeUsername,
            List<String> updatedTrainerUsernames
    ) {
        String jpql =
                """
                SELECT t FROM Trainee t
                WHERE t.username = :username
                """;

        TypedQuery<Trainee> query = em.createQuery(jpql, Trainee.class)
                .setParameter("username", traineeUsername);

        Trainee trainee = query
                .getResultStream()
                .findFirst()
                .orElseThrow(
                () -> new EntityNotFoundException("trainee not found")
        );

        List<Trainer> trainers = em.createQuery(
                "SELECT t FROM Trainer t WHERE t.username in :usernames",
                        Trainer.class
                )
                .setParameter("usernames", updatedTrainerUsernames)
                .getResultList();

        if (trainers.size() != updatedTrainerUsernames.stream().distinct().count()) {
            throw new EntityNotFoundException("One or more trainer were not found");
        }

        trainee.getTrainers().clear();
        trainee.getTrainers().addAll(trainers);
    }

    @Override
    public boolean existsByUsername(String username) {
        String jpql = "SELECT COUNT(t) > 0 FROM Trainee t WHERE t.username = :username";

        TypedQuery<Boolean> query = em.createQuery(jpql, Boolean.class)
                .setParameter("username", username);

        return query.getSingleResult();
    }
}