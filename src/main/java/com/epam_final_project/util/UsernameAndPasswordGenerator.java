package com.epam_final_project.util;

import java.util.Random;

import com.epam_final_project.dao.TraineeDao;
import com.epam_final_project.dao.TrainerDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UsernameAndPasswordGenerator {
    private static final Logger log = LoggerFactory.getLogger(UsernameAndPasswordGenerator.class);

    private static final int PASSWORD_LENGTH = 10;

    public static String generateUsername(
            String firstName,
            String lastName,
            TraineeDao traineeDao,
            TrainerDao trainerDao
    ) {
        String base = firstName + "." + lastName;

        String username = base;
        int suffix = 1;
        while (traineeDao.existsByUsername(username) || trainerDao.existsByUsername(username)) {
            username = base + (suffix++);
        }

        return username;
    }

    public static String generatePassword() {
        log.debug("Generating new password");
        Random r = new Random();

        StringBuilder password = new StringBuilder();
        for (int i = 0; i < PASSWORD_LENGTH; i++) {
            char c = (char)(r.nextInt(26) + 'a');
            password.append(c);
        }

        String generatedPassword = password.toString();
        return generatedPassword;
    }
}