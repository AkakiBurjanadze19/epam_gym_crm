package com.epam_final_project.dto.trainee.response;

import com.epam_final_project.dto.trainer.response.TrainerSummaryDto;

import java.time.LocalDate;
import java.util.List;

public record UpdateTraineeProfileResponseDto(
    String username,
    String firstName,
    String lastName,
    LocalDate dateOfBirth,
    String address,
    boolean isActive,
    List<TrainerSummaryDto> trainers
) {}