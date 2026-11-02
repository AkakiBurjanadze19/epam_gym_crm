package com.epam_final_project.controller.trainer;

import com.epam_final_project.dto.common.ActivateProfileRequestDto;
import com.epam_final_project.dto.trainer.request.UpdateTrainerProfileRequestDto;
import com.epam_final_project.dto.trainer.response.TrainerProfileDto;
import com.epam_final_project.dto.trainer.response.TrainerSummaryDto;
import com.epam_final_project.dto.trainer.request.CreateTrainerRequest;
import com.epam_final_project.dto.trainer.response.TrainerRegistrationResponseDto;
import com.epam_final_project.dto.trainer.response.UpdateTrainerProfileResponseDto;
import com.epam_final_project.service.AuthService;
import com.epam_final_project.service.TraineeService;
import com.epam_final_project.service.TrainerService;
import io.micrometer.core.annotation.Timed;
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
import java.util.List;

@RestController
@RequestMapping("/api/trainers")
@Tag(name = "Trainer", description = "Trainer management APIs")
public class TrainerController {
    private final TrainerService trainerService;
    private final TraineeService traineeService;
    private final AuthService authService;

    @Autowired
    public TrainerController(TrainerService trainerService, TraineeService traineeService, AuthService authService) {
        this.trainerService = trainerService;
        this.traineeService = traineeService;
        this.authService = authService;
    }

    @PostMapping
    @Timed(value = "gym.api.request.latency", description = "Trainer registration latency", percentiles = {0.5, 0.95, 0.99})
    @Operation(summary = "Register a new trainer",
            description = "Creates a new trainer profile. Username and password are auto-generated. First name, last name, and specialization are required.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Trainer created successfully",
                    content = @Content(schema = @Schema(implementation = TrainerRegistrationResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "Validation failed - missing required fields",
                    content = @Content(schema = @Schema(implementation = Void.class))),
            @ApiResponse(responseCode = "409", description = "User with this name already exists as a trainee or invalid specialization",
                    content = @Content(schema = @Schema(implementation = Void.class)))
    })
    public ResponseEntity<TrainerRegistrationResponseDto> registerTrainer(
            @Valid @RequestBody CreateTrainerRequest request
    ) {
        TrainerRegistrationResponseDto response = trainerService.createTrainer(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/{username}")
    @Timed(value = "gym.api.request.latency", description = "Get trainer profile latency", percentiles = {0.5, 0.95, 0.99})
    @Operation(summary = "Get trainer profile by username",
            description = "Retrieves the full trainer profile including associated trainees list. Requires authentication.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Trainer profile found",
                    content = @Content(schema = @Schema(implementation = TrainerProfileDto.class))),
            @ApiResponse(responseCode = "401", description = "Invalid credentials",
                    content = @Content(schema = @Schema(implementation = Void.class))),
            @ApiResponse(responseCode = "404", description = "Trainer not found",
                    content = @Content(schema = @Schema(implementation = Void.class)))
    })
    public ResponseEntity<TrainerProfileDto> getTrainerProfile(
            @Parameter(description = "Trainer username", example = "jane.trainer")
            @PathVariable("username") String username,
            @Parameter(description = "Authenticated username", example = "jane.trainer")
            @RequestParam("username") @NotBlank String authUsername,
            @Parameter(description = "Authenticated password", example = "secret123")
            @RequestParam("password") @NotBlank String authPassword
    ) throws AuthenticationException {
        TrainerProfileDto response = trainerService.selectTrainerByUsername(username, authUsername, authPassword)
                .orElseThrow(() -> new EntityNotFoundException("Trainer not found: " + username));
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PutMapping("/{username}")
    @Timed(value = "gym.api.request.latency", description = "Update trainer profile latency", percentiles = {0.5, 0.95, 0.99})
    @Operation(summary = "Update trainer profile",
            description = "Updates trainer profile. Requires authentication via username and password. First name, last name, and isActive are required; specialization is read-only and cannot be changed.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Trainer profile updated successfully",
                    content = @Content(schema = @Schema(implementation = UpdateTrainerProfileResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "Validation failed",
                    content = @Content(schema = @Schema(implementation = Void.class))),
            @ApiResponse(responseCode = "401", description = "Invalid credentials",
                    content = @Content(schema = @Schema(implementation = Void.class))),
            @ApiResponse(responseCode = "404", description = "Trainer not found",
                    content = @Content(schema = @Schema(implementation = Void.class)))
    })
    public ResponseEntity<UpdateTrainerProfileResponseDto> updateTrainerProfile(
            @Parameter(description = "Trainer username", example = "jane.trainer")
            @PathVariable("username") String username,
            @Parameter(description = "Current password for authentication", example = "secret123")
            @RequestParam("password") @NotBlank String password,
            @Valid @RequestBody UpdateTrainerProfileRequestDto request
    ) throws AuthenticationException {
        UpdateTrainerProfileResponseDto response = trainerService.updateTrainer(
                request,
                username,
                password
        );

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PatchMapping("/{username}/status")
    @Timed(value = "gym.api.request.latency", description = "Activate/deactivate trainer latency", percentiles = {0.5, 0.95, 0.99})
    @Operation(summary = "Activate or deactivate trainer",
            description = "Activates or deactivates a trainer profile. This is not an idempotent operation. Requires authentication.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Trainer status updated successfully"),
            @ApiResponse(responseCode = "400", description = "isActive is required",
                    content = @Content(schema = @Schema(implementation = Void.class))),
            @ApiResponse(responseCode = "401", description = "Invalid credentials",
                    content = @Content(schema = @Schema(implementation = Void.class))),
            @ApiResponse(responseCode = "404", description = "Trainer not found",
                    content = @Content(schema = @Schema(implementation = Void.class)))
    })
    public ResponseEntity<Void> activateDeactivateTrainer(
            @Parameter(description = "Trainer username", example = "jane.trainer")
            @PathVariable("username") String username,
            @Parameter(description = "Authenticated username", example = "jane.trainer")
            @RequestParam("username") @NotBlank String authUsername,
            @Parameter(description = "Authenticated password", example = "secret123")
            @RequestParam("password") @NotBlank String authPassword,
            @Valid @RequestBody ActivateProfileRequestDto request
    ) throws AuthenticationException {
        trainerService.activateDeactivateTrainer(username, request.getIsActive(), authUsername, authPassword);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping("/{username}/password")
    @Timed(value = "gym.api.request.latency", description = "Change trainer password latency", percentiles = {0.5, 0.95, 0.99})
    @Operation(summary = "Change trainer password",
            description = "Changes the trainer's password. Requires current password for verification.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Password changed successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request - missing parameters or invalid old password",
                    content = @Content(schema = @Schema(implementation = Void.class))),
            @ApiResponse(responseCode = "404", description = "User not found",
                    content = @Content(schema = @Schema(implementation = Void.class)))
    })
    public ResponseEntity<Void> changePassword(
            @Parameter(description = "Trainer username", example = "jane.trainer")
            @PathVariable("username") String username,
            @Parameter(description = "Current password", example = "secret123")
            @RequestParam("oldPassword") @NotBlank String oldPassword,
            @Parameter(description = "New password", example = "newSecret456")
            @RequestParam("newPassword") @NotBlank String newPassword
    ) {
        authService.changeLogin(username, oldPassword, newPassword);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}