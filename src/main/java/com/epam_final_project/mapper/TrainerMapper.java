package com.epam_final_project.mapper;

import com.epam_final_project.domain.Trainer;
import com.epam_final_project.dto.trainer.request.CreateTrainerRequest;
import com.epam_final_project.dto.trainer.request.UpdateTrainerProfileRequestDto;
import com.epam_final_project.dto.trainer.response.TrainerProfileDto;
import com.epam_final_project.dto.trainer.response.TrainerSummaryDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface TrainerMapper {
    @Mapping(target = "specialization", source = "specialization.trainingTypeName")
    TrainerSummaryDto toDto(Trainer trainer);

    @Mapping(target = "specialization", source = "specialization.trainingTypeName")
    TrainerProfileDto toTrainerProfileDto(Trainer trainer);

    @Mapping(target = "specialization", ignore = true)
    Trainer toEntity(CreateTrainerRequest trainerDto);

    @Mapping(target = "username", ignore = true)
    @Mapping(target = "password", ignore = true)
    @Mapping(target = "specialization", ignore = true)
    @Mapping(target = "trainees", ignore = true)
    @Mapping(target = "trainings", ignore = true)
    Trainer toEntity(UpdateTrainerProfileRequestDto request);
}