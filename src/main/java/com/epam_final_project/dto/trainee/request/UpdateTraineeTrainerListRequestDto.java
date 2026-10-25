package com.epam_final_project.dto.trainee.request;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class UpdateTraineeTrainerListRequestDto {
    @NotEmpty
    private List<String> trainerUsernames;
}