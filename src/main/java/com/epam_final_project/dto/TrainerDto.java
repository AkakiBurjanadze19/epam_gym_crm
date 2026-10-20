package com.epam_final_project.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TrainerDto {
    private String firstName;
    private String lastName;
    private String username;
    private boolean isActive;
    private String specialization;
}