package com.epam_final_project.storage;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class StorageInitializerPostProcessorConfig {
    @Bean
    public StorageInitializerPostProcessor storageInitializerPostProcessor() {
        return new StorageInitializerPostProcessor();
    }
}