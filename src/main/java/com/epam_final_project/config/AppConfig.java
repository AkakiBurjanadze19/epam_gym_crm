package com.epam_final_project.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackages = "com.epam_final_project")
public class AppConfig {
    private static final Logger log = LoggerFactory.getLogger(AppConfig.class);

    public AppConfig() {
        log.info("Initializing Spring Application Configuration");
    }
}