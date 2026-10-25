package com.epam_final_project.dto.trainer.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TrainerSummaryDto {
    private String username;
    private String firstName;
    private String lastName;
    private String specialization;
}