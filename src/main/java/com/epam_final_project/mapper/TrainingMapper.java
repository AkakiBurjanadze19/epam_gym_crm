package com.epam_final_project.mapper;

import com.epam_final_project.domain.Training;
import com.epam_final_project.dto.CreateTrainingRequest;
import com.epam_final_project.dto.TrainingDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface TrainingMapper {
    @Mapping(target = "traineeUsername", source = "trainee.username")
    @Mapping(target = "trainerUsername", source = "trainer.username")
    @Mapping(target = "type", source = "type.trainingTypeName")
    TrainingDto toDto(Training training);
    Training toEntity(CreateTrainingRequest request);
}