package com.epam_final_project.dao.impl;

import com.epam_final_project.dao.TrainingTypeDao;
import com.epam_final_project.domain.TrainingType;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class TrainingTypeDaoImpl implements TrainingTypeDao {
    @PersistenceContext
    private EntityManager em;

    @Override
    public Optional<TrainingType> findById(Integer trainingTypeId) {
        return Optional.ofNullable(em.find(TrainingType.class, trainingTypeId));
    }

    @Override
    public Optional<TrainingType> findByTrainingTypeName(String trainingTypeName) {
        String jpql = "SELECT t FROM TrainingType t WHERE t.trainingTypeName = :name";

        TypedQuery<TrainingType> query = em
                .createQuery(jpql, TrainingType.class)
                .setParameter("name", trainingTypeName);

        return query.getResultStream().findFirst();
    }

    @Override
    public List<TrainingType> findAll() {
        String jpql = "SELECT t FROM TrainingType t";

        TypedQuery<TrainingType> query = em.createQuery(jpql, TrainingType.class);

        return query.getResultList();
    }
}
