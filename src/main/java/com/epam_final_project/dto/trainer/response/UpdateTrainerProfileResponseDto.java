package com.epam_final_project.dto.trainer.response;

import com.epam_final_project.dto.common.TraineeSummaryDto;

import java.util.List;

public record UpdateTrainerProfileResponseDto(
        String username,
        String firstName,
        String lastName,
        String specialization,
        boolean isActive,
        List<TraineeSummaryDto> trainees
) {}