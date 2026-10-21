package com.epam_final_project.domain;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "trainers")
public class Trainer extends User {
    @ManyToOne
    @JoinColumn(name = "training_type_id")
    private TrainingType specialization;

    @ManyToMany
    @JoinTable(
            name = "trainer_trainee",
            joinColumns = @JoinColumn(name = "trainer_id"),
            inverseJoinColumns = @JoinColumn(name = "trainee_id")
    )
    private List<Trainee> trainees = new ArrayList<>();

    @OneToMany(mappedBy = "trainer", fetch = FetchType.LAZY)
    private List<Training> trainings = new ArrayList<>();

    public Trainer() {}

    public Trainer(
            String firstName,
            String lastName,
            String username,
            String password,
            boolean isActive,
            TrainingType specialization
    ) {
        super(firstName, lastName, username, password, isActive);
        this.specialization = specialization;
    }

    public void addTrainee(Trainee trainee) {
        trainees.add(trainee);
        trainee.getTrainers().add(this);
    }

    public void removeTrainee(Trainee trainee) {
        trainees.remove(trainee);
        trainee.getTrainers().remove(this);
    }

    public void addTraining(Training training) {
        trainings.add(training);
        training.setTrainer(this);
    }

    public void removeTraining(Training training) {
        trainings.remove(training);
        training.setTrainer(null);
    }

    public TrainingType getSpecialization() {
        return specialization;
    }

    public void setSpecialization(TrainingType specialization) {
        this.specialization = specialization;
    }

    public void setTrainees(List<Trainee> trainees) {
        this.trainees = trainees;
    }

    public List<Trainee> getTrainees() {
        return trainees;
    }

    public void setTrainings(List<Training> trainings) {
        this.trainings = trainings;
    }

    public List<Training> getTrainings() {
        return trainings;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Trainer trainer = (Trainer) o;
        return Objects.equals(specialization, trainer.specialization) && Objects.equals(trainees, trainer.trainees);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), specialization, trainees);
    }

    @Override
    public String toString() {
        return "Trainer{" +
                "id=" + getId() +
                ", specialization=" + specialization +
                ", traineeCount=" + (trainees == null ? 0 : trainees.size()) +
                '}';
    }
}