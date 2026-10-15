package com.epam_final_project.domain;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "trainees")
public class Trainee extends User {
    @Column(name = "date_of_birth")
    private LocalDate dateOfBirth;

    @Column(name = "address")
    private String address;

    @ManyToMany(mappedBy = "trainees")
    private List<Trainer> trainers = new ArrayList<>();

    @OneToMany(mappedBy = "trainee", cascade = CascadeType.REMOVE)
    private List<Training> trainings = new ArrayList<>();

    public Trainee() {}

    public Trainee(
            String firstName,
            String lastName,
            String username,
            String password,
            boolean isActive,
            LocalDate dateOfBirth,
            String address
    ) {
        super(firstName, lastName, username, password, isActive);
        this.dateOfBirth = dateOfBirth;
        this.address = address;
    }

    public void addTrainer(Trainer trainer) {
        trainers.add(trainer);
        trainer.getTrainees().add(this);
    }

    public void removeTrainer(Trainer trainer) {
        trainers.remove(trainer);
        trainer.getTrainees().remove(this);
    }

    public void addTraining(Training training) {
        trainings.add(training);
        training.setTrainee(this);
    }

    public void removeTraining(Training training) {
        trainings.remove(training);
        training.setTrainee(null);
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setTrainings(List<Training> trainings) {
        this.trainings = trainings;
    }

    public List<Training> getTrainings() {
        return trainings;
    }

    public void setTrainers(List<Trainer> trainers) {
        this.trainers = trainers;
    }

    public List<Trainer> getTrainers() {
        return trainers;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Trainee trainee = (Trainee) o;
        return Objects.equals(dateOfBirth, trainee.dateOfBirth) && Objects.equals(address, trainee.address) && Objects.equals(trainers, trainee.trainers);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), dateOfBirth, address, trainers);
    }

    @Override
    public String toString() {
        return "Trainee{" +
                "dateOfBirth=" + dateOfBirth +
                ", address='" + address + '\'' +
                ", trainers=" + trainers +
                '}';
    }
}
