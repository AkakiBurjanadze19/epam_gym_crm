package com.epam_final_project.storage;

import com.epam_final_project.domain.Trainer;
import com.epam_final_project.domain.TrainingType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

public class TrainerDataLoader implements DataLoaderStrategy<Trainer> {
    private static final Logger log = LoggerFactory.getLogger(TrainerDataLoader.class);

    public TrainerDataLoader() {}

    @Override
    public Trainer loadData(String[] csvData) {
        String firstName = csvData[1];
        String lastName = csvData[2];
        String username = csvData[3];
        String password = csvData[4];
        boolean isActive = Boolean.parseBoolean(csvData[5]);
        Integer userId = Integer.valueOf(csvData[0]);
        TrainingType specialization = TrainingType.valueOf(csvData[6]);

        Trainer trainer = new Trainer(
                firstName,
                lastName,
                username,
                password,
                isActive,
                userId,
                specialization
        );

        log.debug("Created trainer object: {}", trainer);

        return trainer;
    }

    @Override
    public void storeData(Trainer entity, Map<Integer, Trainer> storage) {
        storage.put(entity.getUserId(), entity);
    }
}
