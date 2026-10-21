package com.epam_final_project.domain;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.Objects;

@Entity
@Table(name = "trainings")
public class Training {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "trainee_id")
    private Trainee trainee;

    @ManyToOne
    @JoinColumn(name = "trainer_id")
    private Trainer trainer;

    @Column(name = "training_name", nullable = false)
    private String name;

    @ManyToOne
    @JoinColumn(name = "training_type_id")
    private TrainingType type;

    @Column(name = "training_date", nullable = false)
    private LocalDate date;

    @Column(name = "training_duration")
    private int duration;

    public Training() {
    }

    public Training(
            Trainee trainee,
            Trainer trainer,
            String name,
            TrainingType type,
            LocalDate date,
            int duration
    ) {
        this.trainee = trainee;
        this.trainer = trainer;
        this.name = name;
        this.type = type;
        this.date = date;
        this.duration = duration;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Trainer getTrainer() {
        return trainer;
    }

    public void setTrainer(Trainer trainer) {
        this.trainer = trainer;
    }

    public Trainee getTrainee() {
        return trainee;
    }

    public void setTrainee(Trainee trainee) {
        this.trainee = trainee;
    }

    public Integer getTrainerId() {
        return trainer.getId();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public TrainingType getType() {
        return type;
    }

    public void setType(TrainingType type) {
        this.type = type;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Training training = (Training) o;
        return duration == training.duration
                && Objects.equals(id, training.id)
                && Objects.equals(trainee, training.trainee)
                && Objects.equals(trainer, training.trainer)
                && Objects.equals(name, training.name)
                && type == training.type && Objects.equals(date, training.date);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, trainee, trainer, name, type, date, duration);
    }

    @Override
    public String toString() {
        return "Training{" +
                "id=" + id +
                ", trainee=" + trainee +
                ", trainer=" + trainer +
                ", name='" + name + '\'' +
                ", type=" + type +
                ", date=" + date +
                ", duration=" + duration +
                '}';
    }
}