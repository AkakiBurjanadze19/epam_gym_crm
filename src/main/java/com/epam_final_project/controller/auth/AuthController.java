package com.epam_final_project.controller.auth;

import com.epam_final_project.dto.auth.request.ChangeLoginRequestDto;
import com.epam_final_project.service.AuthService;
import io.micrometer.core.annotation.Timed;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.naming.AuthenticationException;

@RestController
@RequestMapping("/api/auth")
@Tag(name = "Authentication", description = "Authentication APIs")
public class AuthController {
    private final AuthService authService;

    @Autowired
    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    @Timed(value = "gym.api.request.latency", description = "Login latency", percentiles = {0.5, 0.95, 0.99})
    @Operation(summary = "Login with username and password",
            description = "Authenticates a trainee or trainer using username and password. Returns 200 OK on success, 401 Unauthorized on failure.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Authentication successful"),
            @ApiResponse(responseCode = "401", description = "Invalid username or password",
                    content = @Content(schema = @Schema(implementation = Void.class))),
            @ApiResponse(responseCode = "400", description = "Username and password are required",
                    content = @Content(schema = @Schema(implementation = Void.class)))
    })
    public ResponseEntity<Void> login(
            @RequestParam("username") @NotBlank @Schema(description = "Username", example = "john.smith") String username,
            @RequestParam("password") @NotBlank @Schema(description = "Password", example = "secret123") String password
    ) throws AuthenticationException {
        boolean valid = authService.checkTraineeUsernameAndPassword(username, password)
                || authService.checkTrainerUsernameAndPassword(username, password);

        if (!valid) {
            throw new AuthenticationException("Invalid username or password");
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping("/password")
    @Timed(value = "gym.api.request.latency", description = "Change password latency", percentiles = {0.5, 0.95, 0.99})
    @Operation(summary = "Change password",
            description = "Changes the password for a trainee or trainer. Requires current password for verification.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Password changed successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request - missing parameters, validation failed, or invalid old password",
                    content = @Content(schema = @Schema(implementation = Void.class))),
            @ApiResponse(responseCode = "404", description = "User not found",
                    content = @Content(schema = @Schema(implementation = Void.class)))
    })
    public ResponseEntity<Void> changePassword(
            @Valid @RequestBody ChangeLoginRequestDto request
    ) {
        authService.changeLogin(request.getUsername(), request.getOldPassword(), request.getNewPassword());
        return new ResponseEntity<>(HttpStatus.OK);
    }
}