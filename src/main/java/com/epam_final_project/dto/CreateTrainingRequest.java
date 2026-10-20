package com.epam_final_project.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class CreateTrainingRequest {
    private String traineeUsername;
    private String trainerUsername;
    private String name;
    private LocalDate date;
    private int duration;
}