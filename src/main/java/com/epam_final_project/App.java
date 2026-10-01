package com.epam_final_project;

import com.epam_final_project.config.AppConfig;
import com.epam_final_project.domain.Trainee;
import com.epam_final_project.domain.Trainer;
import com.epam_final_project.domain.Training;
import com.epam_final_project.domain.TrainingType;
import com.epam_final_project.facade.GymFacade;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.time.Duration;
import java.time.LocalDate;

public class App {
    private static final Logger log = LoggerFactory.getLogger(App.class);

    public static void main(String[] args) {
        log.info("Starting Spring Gym Application");
        ApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
        GymFacade facade = context.getBean(GymFacade.class);

        log.info("GymFacade bean retrieved: {}", facade);

        facade.createTrainer(
                new Trainer(
                        "John",
                        "Smith",
                        "John.Smith",
                        "abcdefghij",
                        true,
                        1,
                        TrainingType.HIIT
                )
        );

        facade.createTrainee(
                new Trainee(
                        "Alex",
                        "Wilson",
                        "Alex.Wilson",
                        "klmnopqrst",
                        true,
                        1,
                        LocalDate.of(1990, 5, 15),
                        "Tbilisi"
                )
        );

        facade.createTraining(
                new Training(
                        1,
                        1,
                        "HIIT Interval Training",
                        TrainingType.HIIT,
                        LocalDate.of(2026, 10, 28),
                        Duration.ofMinutes(30)
                )
        );

        log.info("Created trainer, trainee, and training.");
        log.info("application completed successfully");
    }
}