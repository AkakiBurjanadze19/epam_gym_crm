package com.epam_final_project.metrics;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.Gauge;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import io.micrometer.core.instrument.binder.jvm.JvmMemoryMetrics;
import io.micrometer.core.instrument.binder.jvm.JvmGcMetrics;
import io.micrometer.core.instrument.binder.jvm.JvmThreadMetrics;
import io.micrometer.core.instrument.binder.system.ProcessorMetrics;
import io.micrometer.core.instrument.binder.system.FileDescriptorMetrics;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.atomic.AtomicInteger;

@Configuration
public class BusinessMetrics {

    private final MeterRegistry meterRegistry;
    private final AtomicInteger activeTrainees = new AtomicInteger(0);
    private final AtomicInteger activeTrainers = new AtomicInteger(0);

    @Autowired
    public BusinessMetrics(MeterRegistry meterRegistry) {
        this.meterRegistry = meterRegistry;
        // Initialize standard JVM and system metrics
        new JvmMemoryMetrics().bindTo(meterRegistry);
        new JvmGcMetrics().bindTo(meterRegistry);
        new JvmThreadMetrics().bindTo(meterRegistry);
        new ProcessorMetrics().bindTo(meterRegistry);
        new FileDescriptorMetrics().bindTo(meterRegistry);

        // Register gauges for active users
        Gauge.builder("gym.active.trainees", activeTrainees, AtomicInteger::get)
                .description("Number of active trainees")
                .register(meterRegistry);

        Gauge.builder("gym.active.trainers", activeTrainers, AtomicInteger::get)
                .description("Number of active trainers")
                .register(meterRegistry);
    }

    // Counters
    public Counter traineeRegistrations() {
        return Counter.builder("gym.trainee.registrations")
                .description("Total number of trainee registrations")
                .register(meterRegistry);
    }

    public Counter trainerRegistrations() {
        return Counter.builder("gym.trainer.registrations")
                .description("Total number of trainer registrations")
                .register(meterRegistry);
    }

    public Counter trainingCreations() {
        return Counter.builder("gym.training.creations")
                .description("Total number of training creations")
                .register(meterRegistry);
    }

    public Counter authSuccess() {
        return Counter.builder("gym.auth.success")
                .description("Total number of successful authentications")
                .register(meterRegistry);
    }

    public Counter authFailure() {
        return Counter.builder("gym.auth.failure")
                .description("Total number of failed authentications")
                .register(meterRegistry);
    }

    // Timers
    public Timer apiRequestLatency(String endpoint) {
        return Timer.builder("gym.api.request.latency")
                .description("API request latency")
                .tag("endpoint", endpoint)
                .register(meterRegistry);
    }

    public Timer dbQueryLatency(String query) {
        return Timer.builder("gym.db.query.latency")
                .description("Database query latency")
                .tag("query", query)
                .register(meterRegistry);
    }

    public AtomicInteger getActiveTrainees() {
        return activeTrainees;
    }

    public AtomicInteger getActiveTrainers() {
        return activeTrainers;
    }

    // Increment methods for convenience
    public void incrementTraineeRegistrations() {
        traineeRegistrations().increment();
    }

    public void incrementTrainerRegistrations() {
        trainerRegistrations().increment();
    }

    public void incrementTrainingCreations() {
        trainingCreations().increment();
    }

    public void incrementAuthSuccess() {
        authSuccess().increment();
    }

    public void incrementAuthFailure() {
        authFailure().increment();
    }

    public void setActiveTrainees(int count) {
        activeTrainees.set(count);
    }

    public void setActiveTrainers(int count) {
        activeTrainers.set(count);
    }
}