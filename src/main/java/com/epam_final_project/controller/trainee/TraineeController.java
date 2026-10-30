package com.epam_final_project.controller.trainee;

import com.epam_final_project.dto.trainee.request.CreateTraineeRequest;
import com.epam_final_project.dto.trainee.request.UpdateTraineeProfileRequestDto;
import com.epam_final_project.dto.trainee.response.TraineeProfileDto;
import com.epam_final_project.dto.trainee.response.UpdateTraineeProfileResponseDto;
import com.epam_final_project.dto.trainee.response.TraineeRegistrationResponseDto;
import com.epam_final_project.dto.trainer.response.TrainerSummaryDto;
import com.epam_final_project.dto.training.response.TraineeTrainingResponseDto;
import com.epam_final_project.dto.common.ActivateProfileRequestDto;
import com.epam_final_project.service.AuthService;
import com.epam_final_project.service.TraineeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.naming.AuthenticationException;
import jakarta.persistence.EntityNotFoundException;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/trainees")
@Tag(name = "Trainee", description = "Trainee management APIs")
public class TraineeController {
    private final TraineeService traineeService;
    private final AuthService authService;

    @Autowired
    public TraineeController(TraineeService traineeService, AuthService authService) {
        this.traineeService = traineeService;
        this.authService = authService;
    }

    @PostMapping
    @Operation(summary = "Register a new trainee",
            description = "Creates a new trainee profile. Username and password are auto-generated. First name and last name are required; date of birth and address are optional.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Trainee created successfully",
                    content = @Content(schema = @Schema(implementation = TraineeRegistrationResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "Validation failed - missing required fields",
                    content = @Content(schema = @Schema(implementation = Void.class))),
            @ApiResponse(responseCode = "409", description = "User with this name already exists as a trainer",
                    content = @Content(schema = @Schema(implementation = Void.class)))
    })
    public ResponseEntity<TraineeRegistrationResponseDto> registerTrainee(
            @Valid @RequestBody CreateTraineeRequest request
    ) {
        TraineeRegistrationResponseDto response = traineeService.createTrainee(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/{username}")
    @Operation(summary = "Get trainee profile by username",
            description = "Retrieves the full trainee profile including associated trainers list. Requires authentication.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Trainee profile found",
                    content = @Content(schema = @Schema(implementation = TraineeProfileDto.class))),
            @ApiResponse(responseCode = "401", description = "Invalid credentials",
                    content = @Content(schema = @Schema(implementation = Void.class))),
            @ApiResponse(responseCode = "404", description = "Trainee not found",
                    content = @Content(schema = @Schema(implementation = Void.class)))
    })
    public ResponseEntity<TraineeProfileDto> getTraineeProfile(
            @Parameter(description = "Trainee username", example = "john.smith")
            @PathVariable("username") String username,
            @Parameter(description = "Authenticated username", example = "john.smith")
            @RequestParam("username") @NotBlank String authUsername,
            @Parameter(description = "Authenticated password", example = "secret123")
            @RequestParam("password") @NotBlank String authPassword
    ) throws AuthenticationException {
        TraineeProfileDto response = traineeService.selectTraineeByUsername(username, authUsername, authPassword)
                .orElseThrow(() -> new EntityNotFoundException("Trainee not found: " + username));
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PutMapping("/{username}")
    @Operation(summary = "Update trainee profile",
            description = "Updates trainee profile. Requires authentication via username and password. First name, last name, and isActive are required; date of birth and address are optional.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Trainee profile updated successfully",
                    content = @Content(schema = @Schema(implementation = UpdateTraineeProfileResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "Validation failed",
                    content = @Content(schema = @Schema(implementation = Void.class))),
            @ApiResponse(responseCode = "401", description = "Invalid credentials",
                    content = @Content(schema = @Schema(implementation = Void.class))),
            @ApiResponse(responseCode = "404", description = "Trainee not found",
                    content = @Content(schema = @Schema(implementation = Void.class)))
    })
    public ResponseEntity<UpdateTraineeProfileResponseDto> updateTraineeProfile(
            @Parameter(description = "Trainee username", example = "john.smith")
            @PathVariable("username") String username,
            @Parameter(description = "Current password for authentication", example = "secret123")
            @RequestParam("password") @NotBlank String password,
            @Valid @RequestBody UpdateTraineeProfileRequestDto request
    ) throws AuthenticationException {
        UpdateTraineeProfileResponseDto response = traineeService
                .updateTrainee(request, username, password);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @DeleteMapping("/{username}")
    @Operation(summary = "Delete trainee profile",
            description = "Permanently deletes a trainee profile and all associated trainings (cascade delete). Requires authentication via username and password.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Trainee deleted successfully"),
            @ApiResponse(responseCode = "401", description = "Invalid credentials",
                    content = @Content(schema = @Schema(implementation = Void.class))),
            @ApiResponse(responseCode = "404", description = "Trainee not found",
                    content = @Content(schema = @Schema(implementation = Void.class)))
    })
    public ResponseEntity<Void> deleteTraineeProfile(
            @Parameter(description = "Trainee username to delete", example = "john.smith")
            @PathVariable("username") String traineeUsername,
            @Parameter(description = "Authenticated username", example = "john.smith")
            @RequestParam("username") @NotBlank String username,
            @Parameter(description = "Authenticated password", example = "secret123")
            @RequestParam("password") @NotBlank String password
    ) throws AuthenticationException {
        traineeService.deleteTraineeByUsername(traineeUsername, username, password);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping("/{traineeUsername}/trainers")
    @Operation(summary = "Update trainee's trainer list",
            description = "Replaces the entire list of trainers assigned to a trainee. Requires authentication. Provide a list of trainer usernames.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Trainer list updated successfully",
                    content = @Content(schema = @Schema(implementation = TrainerSummaryDto.class))),
            @ApiResponse(responseCode = "401", description = "Invalid credentials",
                    content = @Content(schema = @Schema(implementation = Void.class))),
            @ApiResponse(responseCode = "404", description = "Trainee or one or more trainers not found",
                    content = @Content(schema = @Schema(implementation = Void.class))),
            @ApiResponse(responseCode = "400", description = "Trainer list cannot be empty",
                    content = @Content(schema = @Schema(implementation = Void.class)))
    })
    public ResponseEntity<List<TrainerSummaryDto>> updateTraineeTrainerList(
            @Parameter(description = "Trainee username", example = "john.smith")
            @PathVariable("traineeUsername") String traineeUsername,
            @Parameter(description = "Authenticated username", example = "john.smith")
            @RequestParam("username") @NotBlank String authUsername,
            @Parameter(description = "Authenticated password", example = "secret123")
            @RequestParam("password") @NotBlank String authPassword,
            @Valid @RequestBody @Schema(description = "List of trainer usernames", example = "[\"jane.trainer\", \"bob.trainer\"]") List<@NotBlank String> request
    ) throws AuthenticationException {
        List<TrainerSummaryDto> response = traineeService.updateTraineeTrainers(
                traineeUsername,
                request,
                authUsername,
                authPassword
        );

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PatchMapping("/{username}/status")
    @Operation(summary = "Activate or deactivate trainee",
            description = "Activates or deactivates a trainee profile. This is not an idempotent operation. Requires authentication.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Trainee status updated successfully"),
            @ApiResponse(responseCode = "400", description = "isActive is required",
                    content = @Content(schema = @Schema(implementation = Void.class))),
            @ApiResponse(responseCode = "401", description = "Invalid credentials",
                    content = @Content(schema = @Schema(implementation = Void.class))),
            @ApiResponse(responseCode = "404", description = "Trainee not found",
                    content = @Content(schema = @Schema(implementation = Void.class)))
    })
    public ResponseEntity<Void> activateDeactivateTrainee(
            @Parameter(description = "Trainee username", example = "john.smith")
            @PathVariable("username") String username,
            @Parameter(description = "Authenticated username", example = "john.smith")
            @RequestParam("username") @NotBlank String authUsername,
            @Parameter(description = "Authenticated password", example = "secret123")
            @RequestParam("password") @NotBlank String authPassword,
            @Valid @RequestBody ActivateProfileRequestDto request
    ) throws AuthenticationException {
        traineeService.activateDeactivateTrainee(username, request.getIsActive(), authUsername, authPassword);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping("/{username}/password")
    @Operation(summary = "Change trainee password",
            description = "Changes the trainee's password. Requires current password for verification.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Password changed successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request - missing parameters or invalid old password",
                    content = @Content(schema = @Schema(implementation = Void.class))),
            @ApiResponse(responseCode = "404", description = "User not found",
                    content = @Content(schema = @Schema(implementation = Void.class)))
    })
    public ResponseEntity<Void> changePassword(
            @Parameter(description = "Trainee username", example = "john.smith")
            @PathVariable("username") String username,
            @Parameter(description = "Current password", example = "secret123")
            @RequestParam("oldPassword") @NotBlank String oldPassword,
            @Parameter(description = "New password", example = "newSecret456")
            @RequestParam("newPassword") @NotBlank String newPassword
    ) {
        authService.changeLogin(username, oldPassword, newPassword);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/{username}/trainings")
    @Operation(summary = "Get trainee trainings with filters",
            description = "Retrieves all trainings for a trainee with optional filtering by date range, trainer name, and training type. Requires authentication.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Trainings retrieved successfully",
                    content = @Content(schema = @Schema(implementation = TraineeTrainingResponseDto.class))),
            @ApiResponse(responseCode = "401", description = "Invalid credentials",
                    content = @Content(schema = @Schema(implementation = Void.class))),
            @ApiResponse(responseCode = "404", description = "Trainee not found",
                    content = @Content(schema = @Schema(implementation = Void.class)))
    })
    public ResponseEntity<List<TraineeTrainingResponseDto>> getTraineeTrainings(
            @Parameter(description = "Trainee username", example = "john.smith")
            @PathVariable("username") String username,
            @Parameter(description = "Authenticated username", example = "john.smith")
            @RequestParam("username") @NotBlank String authUsername,
            @Parameter(description = "Authenticated password", example = "secret123")
            @RequestParam("password") @NotBlank String authPassword,
            @Parameter(description = "Filter trainings from this date (inclusive)", example = "2024-01-01")
            @RequestParam(value = "period_from", required = false) LocalDate periodFrom,
            @Parameter(description = "Filter trainings to this date (inclusive)", example = "2024-12-31")
            @RequestParam(value = "period_to", required = false) LocalDate periodTo,
            @Parameter(description = "Filter by trainer name (partial match)", example = "jane")
            @RequestParam(value = "trainer_name", required = false) String trainerName,
            @Parameter(description = "Filter by training type name", example = "Strength Training")
            @RequestParam(value = "training_type", required = false) String trainingType
    ) throws AuthenticationException {
        if (traineeService.selectTraineeByUsername(username, authUsername, authPassword).isEmpty()) {
            throw new EntityNotFoundException("Trainee not found: " + username);
        }
        List<TraineeTrainingResponseDto> response = traineeService.getTrainingsByUsernameAndCriteria(
                username,
                periodFrom,
                periodTo,
                trainerName,
                trainingType,
                authUsername,
                authPassword
        );

        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}