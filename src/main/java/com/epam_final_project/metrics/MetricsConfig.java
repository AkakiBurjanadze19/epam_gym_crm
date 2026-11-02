package com.epam_final_project.metrics;

import io.micrometer.core.instrument.Meter;
import io.micrometer.core.instrument.Tag;
import io.micrometer.core.instrument.config.MeterFilter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;
import java.util.List;

@Configuration
public class MetricsConfig {

    @Value("${spring.application.name:gym-crm}")
    private String applicationName;

    @Value("${spring.profiles.active:local}")
    private String environment;

    @Bean
    public MeterFilter commonTags() {
        return new MeterFilter() {
            @Override
            public Meter.Id map(Meter.Id id) {
                List<Tag> tags = Arrays.asList(
                        Tag.of("application", applicationName),
                        Tag.of("environment", environment),
                        Tag.of("instance", getHostname())
                );
                return id.withTags(tags);
            }
        };
    }

    private String getHostname() {
        try {
            return java.net.InetAddress.getLocalHost().getHostName();
        } catch (Exception ex) {
            return "unknown";
        }
    }
}