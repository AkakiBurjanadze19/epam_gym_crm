package com.epam_final_project.storage;

import com.epam_final_project.domain.Training;
import com.epam_final_project.domain.TrainingType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.time.LocalDate;
import java.util.Map;

public class TrainingDataLoader implements DataLoaderStrategy<Training> {
    private static final Logger log = LoggerFactory.getLogger(TrainingDataLoader.class);

    public TrainingDataLoader() {}

    @Override
    public Training loadData(String[] csvData) {
        Integer id = Integer.valueOf(csvData[0]);
        Integer trainerId = Integer.valueOf(csvData[1]);
        String name = csvData[2];
        TrainingType type = TrainingType.valueOf(csvData[3]);
        LocalDate date = LocalDate.parse(csvData[4]);
        String duration = csvData[5];
        String[] durationParts = duration.split(":");
        Duration trainingDuration = Duration.ofHours(Long.parseLong(durationParts[0]))
                .plusMinutes(Long.parseLong(durationParts[1]))
                .plusSeconds(Long.parseLong(durationParts[2]));

        Training training = new Training(
                id,
                trainerId,
                name,
                type,
                date,
                trainingDuration
        );

        log.debug("Created training object: {}", training);

        return training;
    }

    @Override
    public void storeData(Training entity, Map<Integer, Training> storage) {
        storage.put(entity.getId(), entity);
    }
}