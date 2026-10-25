package com.epam_final_project.mapper;

import com.epam_final_project.domain.Training;
import com.epam_final_project.dto.training.request.CreateTrainingRequestDto;
import com.epam_final_project.dto.TrainingDto;
import com.epam_final_project.dto.training.response.TraineeTrainingResponseDto;
import com.epam_final_project.dto.training.response.TrainerTrainingResponseDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface TrainingMapper {
    @Mapping(target = "traineeUsername", source = "trainee.username")
    @Mapping(target = "trainerUsername", source = "trainer.username")
    @Mapping(target = "type", source = "type.trainingTypeName")
    TrainingDto toDto(Training training);
    TraineeTrainingResponseDto toTraineeTrainingResponseDto(Training training);
    TrainerTrainingResponseDto toTrainerTrainingResponseDto(Training training);
    Training toEntity(CreateTrainingRequestDto request);
}