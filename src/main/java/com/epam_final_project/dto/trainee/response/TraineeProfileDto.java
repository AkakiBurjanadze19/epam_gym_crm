package com.epam_final_project.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
public class TraineeProfileDto {
    private String firstName;
    private String lastName;
    private LocalDate dateOfBirth;
    private String address;
    private boolean isActive;
    private List<TrainerDto> trainers;
}