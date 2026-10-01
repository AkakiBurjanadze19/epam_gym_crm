package com.epam_final_project.domain;

import java.time.Duration;
import java.time.LocalDate;
import java.util.Objects;

public class Training {
    private Integer id;
    private Integer trainerId;
    private String name;
    private TrainingType type;
    private LocalDate date;
    private Duration duration;

    public Training() {
    }

    public Training(
            Integer id,
            Integer trainerId,
            String name,
            TrainingType type,
            LocalDate date, Duration duration
    ) {
        this.id = id;
        this.trainerId = trainerId;
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

    public Integer getTrainerId() {
        return trainerId;
    }

    public void setTrainerId(Integer trainerId) {
        this.trainerId = trainerId;
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

    public Duration getDuration() {
        return duration;
    }

    public void setDuration(Duration duration) {
        this.duration = duration;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Training training = (Training) o;
        return Objects.equals(id, training.id) && Objects.equals(trainerId, training.trainerId) && Objects.equals(name, training.name) && type == training.type && Objects.equals(date, training.date) && Objects.equals(duration, training.duration);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, trainerId, name, type, date, duration);
    }

    @Override
    public String toString() {
        return "Training{" +
                "id=" + id +
                ", trainerId=" + trainerId +
                ", name='" + name + '\'' +
                ", type=" + type +
                ", date=" + date +
                ", duration=" + duration +
                '}';
    }
}