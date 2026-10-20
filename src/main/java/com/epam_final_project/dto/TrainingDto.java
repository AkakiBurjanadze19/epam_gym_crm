package com.epam_final_project.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class TrainingDto {
    private String traineeUsername;
    private String trainerUsername;
    private String name;
    private String type;
    private LocalDate date;
    private int duration;
}