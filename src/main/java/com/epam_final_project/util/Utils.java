package com.epam_final_project.util;

import java.util.List;
import java.util.Map;
import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Utils {
    private static final Logger log = LoggerFactory.getLogger(Utils.class);

    private static final int PASSWORD_LENGTH = 10;

    public static <V> int computeNextId(Map<Integer, V> entityMap) {
        return entityMap.
                keySet()
                .stream()
                .mapToInt(Integer::intValue)
                .max()
                .orElse(0) + 1;
    }

    public static String generateUsername(
            String firstName,
            String lastName,
            List<String> existingUsernames
    ) {
        String base = firstName + "." + lastName;

        String username = base;
        int suffix = 1;
        while (existingUsernames.contains(username)) {
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