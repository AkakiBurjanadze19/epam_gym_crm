package com.epam_final_project.dto.training.response;

import java.time.LocalDate;

public record TrainerTrainingResponseDto(
        String trainingName,
        LocalDate trainingDate,
        String trainingType,
        int trainingDuration,
        String traineeName
) {}