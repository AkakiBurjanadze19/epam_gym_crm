package com.epam_final_project;

import com.epam_final_project.config.AppConfig;
import com.epam_final_project.config.DataSourceConfig;
import com.epam_final_project.domain.Trainee;
import com.epam_final_project.domain.Trainer;
import com.epam_final_project.domain.Training;
import com.epam_final_project.facade.GymFacade;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import javax.naming.AuthenticationException;
import java.time.LocalDate;

public class App {
    private static final Logger log = LoggerFactory.getLogger(App.class);

    public static void main(String[] args) throws AuthenticationException {
        log.info("Starting Spring Gym Application");

        ApplicationContext context = new AnnotationConfigApplicationContext(
                AppConfig.class,
                DataSourceConfig.class
        );
        GymFacade facade = context.getBean(GymFacade.class);

        log.info("GymFacade bean retrieved: {}", facade);

        Trainer trainer1 = new Trainer(
                "Michael",
                "Desanta",
                "michael.desanta",
                "michael200!",
                true,
                facade.selectTrainingType(1).get()
        );

        Trainer trainer2 = new Trainer(
                "Niko",
                "Belik",
                "niko.belik",
                "niko500!",
                true,
                facade.selectTrainingType(2).get()
        );

        Trainer trainer3 = new Trainer(
                "Joel",
                "Miller",
                "joel.miller",
                "joel123",
                true,
                facade.selectTrainingType(3).get()
        );

        facade.createTrainer(trainer1);
        facade.createTrainer(trainer2);
        facade.createTrainer(trainer3);

        Trainee trainee1 = new Trainee(
                "John",
                "Doe",
                "john.doe",
                "john200",
                true,
                LocalDate.of(2000, 3, 12),
                "Tbilisi, Georgia"
        );

        Trainee trainee2 = new Trainee(
                "Nick",
                "Smith",
                "nick.smith",
                "nick!!!",
                true,
                LocalDate.of(1998, 5, 22),
                "Tbilisi, Georgia"
        );

        facade.createTrainee(trainee1);
        facade.createTrainee(trainee2);

        Training training = new Training(
                trainee1,
                trainer3,
                        "First Session in Strength Training",
                        facade.selectTrainingType(3).get(),
                        LocalDate.of(2026, 11, 15),
                        1
                );

        facade.createTraining(training);

        trainee1.addTrainer(trainer3);

        facade.updateTrainer(trainer3, trainer3.getId(), "joel.miller", "joel123");
        facade.updateTrainee(trainee1, trainee1.getId(), "john.doe", "john200");

        log.info("Created trainer, trainee, and training.");
        log.info("application completed successfully");
    }
}