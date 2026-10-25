package com.epam_final_project.controller.training;

import com.epam_final_project.dto.training.request.CreateTrainingRequestDto;
import com.epam_final_project.dto.training.response.TraineeTrainingResponseDto;
import com.epam_final_project.dto.training.response.TrainerTrainingResponseDto;
import com.epam_final_project.service.TrainingService;
import com.epam_final_project.service.TraineeService;
import com.epam_final_project.service.TrainerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.persistence.EntityNotFoundException;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/trainings")
@Tag(name = "Training", description = "Training management APIs")
public class TrainingController {
    private final TrainingService trainingService;
    private final TraineeService traineeService;
    private final TrainerService trainerService;

    @Autowired
    public TrainingController(TrainingService trainingService, TraineeService traineeService, TrainerService trainerService) {
        this.trainingService = trainingService;
        this.traineeService = traineeService;
        this.trainerService = trainerService;
    }

    @PostMapping
    @Operation(summary = "Add a new training",
            description = "Creates a new training session linking a trainee and trainer. Training type is automatically set to the trainer's specialization. Training name, date, and duration are required. Duration must be a positive number.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Training created successfully"),
            @ApiResponse(responseCode = "400", description = "Validation failed - missing required fields or invalid data",
                    content = @Content(schema = @Schema(implementation = Void.class))),
            @ApiResponse(responseCode = "404", description = "Trainee or trainer not found",
                    content = @Content(schema = @Schema(implementation = Void.class)))
    })
    public ResponseEntity<Void> createTraining(@Valid @RequestBody CreateTrainingRequestDto request) {
        trainingService.createTraining(request);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/trainee/{username}")
    @Operation(summary = "Get trainee trainings with filters",
            description = "Retrieves all trainings for a trainee with optional filtering by date range, trainer name, and training type.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Trainings retrieved successfully",
                    content = @Content(schema = @Schema(implementation = TraineeTrainingResponseDto.class))),
            @ApiResponse(responseCode = "404", description = "Trainee not found",
                    content = @Content(schema = @Schema(implementation = Void.class)))
    })
    public ResponseEntity<List<TraineeTrainingResponseDto>> getTraineeTrainings(
            @Parameter(description = "Trainee username", example = "john.smith")
            @PathVariable("username") String username,
            @Parameter(description = "Filter trainings from this date (inclusive)", example = "2024-01-01")
            @RequestParam(value = "period_from", required = false) LocalDate periodFrom,
            @Parameter(description = "Filter trainings to this date (inclusive)", example = "2024-12-31")
            @RequestParam(value = "period_to", required = false) LocalDate periodTo,
            @Parameter(description = "Filter by trainer name (partial match)", example = "jane")
            @RequestParam(value = "trainer_name", required = false) String trainerName,
            @Parameter(description = "Filter by training type name", example = "Strength Training")
            @RequestParam(value = "training_type", required = false) String trainingType
    ) {
        if (traineeService.selectTraineeByUsername(username).isEmpty()) {
            throw new EntityNotFoundException("Trainee not found: " + username);
        }
        List<TraineeTrainingResponseDto> response = traineeService.getTrainingsByUsernameAndCriteria(
                username,
                periodFrom,
                periodTo,
                trainerName,
                trainingType
        );

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/trainer/{username}")
    @Operation(summary = "Get trainer trainings with filters",
            description = "Retrieves all trainings for a trainer with optional filtering by date range and trainee name.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Trainings retrieved successfully",
                    content = @Content(schema = @Schema(implementation = TrainerTrainingResponseDto.class))),
            @ApiResponse(responseCode = "404", description = "Trainer not found",
                    content = @Content(schema = @Schema(implementation = Void.class)))
    })
    public ResponseEntity<List<TrainerTrainingResponseDto>> getTrainerTrainings(
            @Parameter(description = "Trainer username", example = "jane.trainer")
            @PathVariable("username") String username,
            @Parameter(description = "Filter trainings from this date (inclusive)", example = "2024-01-01")
            @RequestParam(value = "period_from", required = false) LocalDate periodFrom,
            @Parameter(description = "Filter trainings to this date (inclusive)", example = "2024-12-31")
            @RequestParam(value = "period_to", required = false) LocalDate periodTo,
            @Parameter(description = "Filter by trainee name (partial match)", example = "john")
            @RequestParam(value = "trainee_name", required = false) String traineeName
    ) {
        if (trainerService.selectTrainerByUsername(username).isEmpty()) {
            throw new EntityNotFoundException("Trainer not found: " + username);
        }
        List<TrainerTrainingResponseDto> response = trainerService.getTrainingsByUsernameAndCriteria(
                username,
                periodFrom,
                periodTo,
                traineeName
        );

        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}