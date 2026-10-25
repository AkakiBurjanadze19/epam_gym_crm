package com.epam_final_project.dto.common;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ActivateProfileRequestDto {
    @NotNull(message = "isActive is required")
    private Boolean isActive;
}
