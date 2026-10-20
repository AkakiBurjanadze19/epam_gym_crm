package com.epam_final_project.mapper;

import com.epam_final_project.domain.Trainee;
import com.epam_final_project.dto.CreateTraineeRequest;
import com.epam_final_project.dto.TraineeDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface TraineeMapper {
    TraineeDto toDto(Trainee trainee);
    Trainee toEntity(CreateTraineeRequest request);
}