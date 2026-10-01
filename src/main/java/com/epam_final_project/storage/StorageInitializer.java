package com.epam_final_project.storage;

import com.epam_final_project.domain.Trainee;
import com.epam_final_project.domain.Trainer;
import com.epam_final_project.domain.Training;
import com.epam_final_project.exception.DataInitializationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;

import java.io.*;
import java.util.Map;

public class StorageInitializer {
    private static final Logger log = LoggerFactory.getLogger(StorageInitializer.class);

    private final String filePath;

    private final Map<Integer, Trainer> trainers;
    private final Map<Integer, Trainee> trainees;
    private final Map<Integer, Training> trainings;

    private final TrainerDataLoader trainerDataLoader = new TrainerDataLoader();
    private final TraineeDataLoader traineeDataLoader = new TraineeDataLoader();
    private final TrainingDataLoader trainingDataLoader = new TrainingDataLoader();

    public StorageInitializer(
            String filePath,
            Map<Integer, Trainer> trainers,
            Map<Integer, Trainee> trainees,
            Map<Integer, Training> trainings
    ) {
        this.filePath = filePath;
        this.trainers = trainers;
        this.trainees = trainees;
        this.trainings = trainings;
        log.info("StorageInitializer created for file: {} with trainers: {}, trainees: {}, trainings: {} maps",
                filePath,
                trainers != null ? trainers.size() : 0,
                trainees != null ? trainees.size() : 0,
                trainings != null ? trainings.size() : 0);
    }

    public void initialize() {
        log.info("Starting data initialization from file: {}", filePath);
        try {
            initData();
            log.info("Data initialization completed successfully from file: {}", filePath);
        } catch (IOException e) {
            log.error("Failed to initialize data from file: {}", filePath, e);
            throw new DataInitializationException("Failed to initialize data", e);
        }
    }

    private void initData() throws IOException {
        log.info("Loading data from classpath resource: {}", filePath);
        ClassPathResource resource = new ClassPathResource(filePath);
        String line = "";
        int recordCount = 0;

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(resource.getInputStream()))) {
            String header = reader.readLine();
            log.debug("CSV header: {}", header);
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",");
                recordCount++;

                populateStorageMaps(data);
            }
        }

        log.info("Finished loading {} records from file: {}", recordCount, filePath);
    }

    private void populateStorageMaps(String[] data) {
        if (filePath.endsWith("trainers.csv")) {
            Trainer trainer = trainerDataLoader.loadData(data);
            trainerDataLoader.storeData(trainer, trainers);
            log.debug("Loaded trainer: {}", trainer);
        }

        if (filePath.endsWith("trainees.csv")) {
            Trainee trainee = traineeDataLoader.loadData(data);
            traineeDataLoader.storeData(trainee, trainees);
            log.debug("Loaded trainee: {}", trainee);
        }

        if (filePath.endsWith("trainings.csv")) {
            Training training = trainingDataLoader.loadData(data);
            trainingDataLoader.storeData(training, trainings);
            log.debug("Loaded training: {}", training);
        }
    }
}