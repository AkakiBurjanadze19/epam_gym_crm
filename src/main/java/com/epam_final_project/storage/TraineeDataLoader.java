package com.epam_final_project.storage;

import com.epam_final_project.domain.Trainee;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.util.Map;

public class TraineeDataLoader implements DataLoaderStrategy<Trainee> {
    private static final Logger log = LoggerFactory.getLogger(TraineeDataLoader.class);

    public TraineeDataLoader() {}

    @Override
    public Trainee loadData(String[] csvData) {
        String firstName = csvData[1];
        String lastName = csvData[2];
        String username = csvData[3];
        String password = csvData[4];
        boolean isActive = Boolean.parseBoolean(csvData[5]);
        Integer userId = Integer.valueOf(csvData[0]);
        LocalDate dateOfBirth = LocalDate.parse(csvData[6]);
        String address = csvData[7];

        Trainee trainee = new Trainee(
                firstName,
                lastName,
                username,
                password,
                isActive,
                userId,
                dateOfBirth,
                address
        );

        log.debug("Created trainee object: {}", trainee);

        return trainee;
    }

    @Override
    public void storeData(Trainee entity, Map<Integer, Trainee> storage) {
        storage.put(entity.getUserId(), entity);
    }
}
