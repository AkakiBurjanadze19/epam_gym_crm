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
            trainee.setUsername(updatedTrainee.getUsername());
            trainee.setPassword(updatedTrainee.getPassword());
            trainee.setActive(updatedTrainee.isActive());
            trainee.setDateOfBirth(updatedTrainee.getDateOfBirth());
            trainee.setAddress(updatedTrainee.getAddress());

            trainee.getTrainers().clear();
            trainee.getTrainers().addAll(updatedTrainee.getTrainers());

            trainee.getTrainings().clear();
            trainee.getTrainings().addAll(updatedTrainee.getTrainings());
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
        String jpql = "DELETE FROM Trainee t WHERE t.username = :username";

        Query query = em.createQuery(jpql).setParameter("username", username);

        query.executeUpdate();
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
            predicates.add(cb.equal(trainer.get("firstName"), trainerName));
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
    public void updateTrainers(Integer traineeId, List<Integer> updatedTrainerIds) {
        Trainee trainee = em.find(Trainee.class, traineeId);

        if (trainee == null) {
            throw new EntityNotFoundException("Trainee not found");
        }

        String jpql = "SELECT t FROM Trainer t WHERE t.id IN :ids";

        TypedQuery<Trainer> query = em.createQuery(jpql, Trainer.class)
                .setParameter("ids", updatedTrainerIds);

        List<Trainer> trainers = query.getResultList();

        trainee.getTrainers().clear();
        trainee.getTrainers().addAll(trainers);
    }
}