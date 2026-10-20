package com.epam_final_project.mapper;

import com.epam_final_project.domain.Trainer;
import com.epam_final_project.dto.CreateTrainerRequest;
import com.epam_final_project.dto.TrainerDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface TrainerMapper {
    @Mapping(target = "specialization", source = "specialization.trainingTypeName")
    TrainerDto toDto(Trainer trainer);

    @Mapping(target = "specialization", ignore = true)
    Trainer toEntity(CreateTrainerRequest trainerDto);
}