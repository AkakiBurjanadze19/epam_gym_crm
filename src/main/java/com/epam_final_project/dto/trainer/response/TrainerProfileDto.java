package com.epam_final_project.dto.trainer.response;

import com.epam_final_project.dto.common.TraineeSummaryDto;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class TrainerProfileDto {
    private String firstName;
    private String lastName;
    private String specialization;
    private boolean isActive;
    private List<TraineeSummaryDto> trainees;
}
