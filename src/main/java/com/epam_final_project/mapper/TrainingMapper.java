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

    @Mapping(target = "trainingName", source = "name")
    @Mapping(target = "trainingDate", source = "date")
    @Mapping(target = "trainingDuration", source = "duration")
    @Mapping(target = "trainingType", source = "type.trainingTypeName")
    @Mapping(target = "trainerName", expression = "java(training.getTrainer().getFirstName() + \" \" + training.getTrainer().getLastName())")
    TraineeTrainingResponseDto toTraineeTrainingResponseDto(Training training);

    @Mapping(target = "trainingName", source = "name")
    @Mapping(target = "trainingDate", source = "date")
    @Mapping(target = "trainingDuration", source = "duration")
    @Mapping(target = "trainingType", source = "type.trainingTypeName")
    @Mapping(target = "traineeName", expression = "java(training.getTrainee().getFirstName() + \" \" + training.getTrainee().getLastName())")
    TrainerTrainingResponseDto toTrainerTrainingResponseDto(Training training);

    @Mapping(target = "name", source = "trainingName")
    @Mapping(target = "date", source = "trainingDate")
    @Mapping(target = "duration", source = "trainingDuration")
    Training toEntity(CreateTrainingRequestDto request);
}