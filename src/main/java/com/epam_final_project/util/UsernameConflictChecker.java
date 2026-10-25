package com.epam_final_project.util;

import com.epam_final_project.dao.TraineeDao;
import com.epam_final_project.dao.TrainerDao;
import org.springframework.stereotype.Component;

@Component
public class UsernameConflictChecker {

    private final TraineeDao traineeDao;
    private final TrainerDao trainerDao;

    public UsernameConflictChecker(TraineeDao traineeDao, TrainerDao trainerDao) {
        this.traineeDao = traineeDao;
        this.trainerDao = trainerDao;
    }

    public void checkUsernameConflict(String firstName, String lastName, boolean checkTrainee) {
        checkBaseUsernameConflict(firstName, lastName, checkTrainee);

        checkGeneratedUsernameConflict(firstName, lastName, checkTrainee);
    }

    private void checkBaseUsernameConflict(
            String firstName,
            String lastName,
            boolean checkTrainee
    ) {
        String baseUsername = firstName + "." + lastName;

        if (checkTrainee) {
            if (trainerDao.existsByUsername(baseUsername)) {
                throw new IllegalStateException("User with this name already exists as a trainer");
            }
        } else {
            if (traineeDao.existsByUsername(baseUsername)) {
                throw new IllegalStateException("User with this name already exists as a trainee");
            }
        }
    }

    private void checkGeneratedUsernameConflict(
            String firstName,
            String lastName,
            boolean checkTrainee
    ) {
        String generatedUsername = getGeneratedUsername(firstName, lastName);

        if (checkTrainee) {
            if (trainerDao.existsByUsername(generatedUsername)) {
                throw new IllegalStateException("User with this name already exists as a trainer");
            }
        } else {
            if (traineeDao.existsByUsername(generatedUsername)) {
                throw new IllegalStateException("User with this name already exists as a trainee");
            }
        }
    }

    private String getGeneratedUsername(String firstName, String lastName) {
        String generatedUsername = UsernameAndPasswordGenerator.generateUsername(
                firstName,
                lastName,
                traineeDao,
                trainerDao
        );
        return generatedUsername;
    }
}