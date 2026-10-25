package com.epam_final_project.mapper;

import com.epam_final_project.domain.Trainee;
import com.epam_final_project.dto.common.TraineeSummaryDto;
import com.epam_final_project.dto.trainee.request.CreateTraineeRequest;
import com.epam_final_project.dto.trainee.response.TraineeProfileDto;
import com.epam_final_project.dto.trainee.request.UpdateTraineeProfileRequestDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = TrainerMapper.class)
public interface TraineeMapper {
    TraineeProfileDto toDto(Trainee trainee);
    TraineeSummaryDto toSummaryDto(Trainee trainee);
    Trainee toEntity(CreateTraineeRequest request);

    @Mapping(target = "username", ignore = true)
    @Mapping(target = "password", ignore = true)
    @Mapping(target = "trainers", ignore = true)
    @Mapping(target = "trainings", ignore = true)
    Trainee toEntity(UpdateTraineeProfileRequestDto request);
}