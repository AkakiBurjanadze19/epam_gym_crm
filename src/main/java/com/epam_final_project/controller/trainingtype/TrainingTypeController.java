package com.epam_final_project.controller.trainingtype;

import com.epam_final_project.dto.trainingtype.response.TrainingTypeResponseDto;
import com.epam_final_project.service.TrainingTypeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/training_types")
@Tag(name = "Training Type", description = "Training type APIs")
public class TrainingTypeController {
    private final TrainingTypeService trainingTypeService;

    @Autowired
    public TrainingTypeController(TrainingTypeService trainingTypeService) {
        this.trainingTypeService = trainingTypeService;
    }

    @GetMapping
    @Operation(summary = "Get all training types",
            description = "Retrieves the complete list of available training types. This is a static reference list that cannot be modified through the API.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Training types retrieved successfully",
                    content = @Content(schema = @Schema(implementation = TrainingTypeResponseDto.class)))
    })
    public ResponseEntity<List<TrainingTypeResponseDto>> getTrainingTypes() {
        List<TrainingTypeResponseDto> trainingTypeDtos = trainingTypeService.getAllTrainingTypes();
        return new ResponseEntity<>(trainingTypeDtos, HttpStatus.OK);
    }
}