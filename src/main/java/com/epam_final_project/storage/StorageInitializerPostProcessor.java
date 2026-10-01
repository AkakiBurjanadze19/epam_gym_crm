package com.epam_final_project.storage;

import org.springframework.beans.factory.config.BeanPostProcessor;

public class StorageInitializerPostProcessor implements BeanPostProcessor {
    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) {
        if (bean instanceof StorageInitializer initializer) {
            initializer.initialize();
        }
        return bean;
    }
}