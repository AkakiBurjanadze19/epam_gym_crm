package com.epam_final_project.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackages = "com.epam_final_project")
public class AppConfig {
    public AppConfig() {}
}