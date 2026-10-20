package com.epam_final_project;

import com.epam_final_project.config.AppConfig;
import com.epam_final_project.config.DataSourceConfig;
import com.epam_final_project.facade.GymFacade;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class App {
    private static final Logger log = LoggerFactory.getLogger(App.class);

    public static void main(String[] args) {
        log.info("Starting Spring Gym Application");

        ApplicationContext context = new AnnotationConfigApplicationContext(
                AppConfig.class,
                DataSourceConfig.class
        );
        GymFacade facade = context.getBean(GymFacade.class);

        log.info("GymFacade bean retrieved: {}", facade);

        log.info("application completed successfully");
    }
}