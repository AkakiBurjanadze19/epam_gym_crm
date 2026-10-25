package com.epam_final_project.mapper;

import com.epam_final_project.domain.TrainingType;
import com.epam_final_project.dto.trainingtype.response.TrainingTypeResponseDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TrainingTypeMapper {
    TrainingTypeResponseDto toDto(TrainingType trainingType);
}
