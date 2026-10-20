package com.epam_final_project.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateTrainerRequest {
    private String firstName;
    private String lastName;
    private String specialization;
}