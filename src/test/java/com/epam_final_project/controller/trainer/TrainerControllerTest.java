package com.epam_final_project.controller.trainer;

import com.epam_final_project.dto.common.TraineeSummaryDto;
import com.epam_final_project.dto.trainer.request.CreateTrainerRequest;
import com.epam_final_project.dto.trainer.request.UpdateTrainerProfileRequestDto;
import com.epam_final_project.dto.trainer.response.TrainerProfileDto;
import com.epam_final_project.dto.trainer.response.TrainerRegistrationResponseDto;
import com.epam_final_project.dto.trainer.response.TrainerSummaryDto;
import com.epam_final_project.dto.trainer.response.UpdateTrainerProfileResponseDto;
import com.epam_final_project.dto.training.response.TrainerTrainingResponseDto;
import com.epam_final_project.service.TrainerService;
import com.epam_final_project.service.TraineeService;
import com.epam_final_project.service.AuthService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import tools.jackson.databind.ObjectMapper;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class TrainerControllerTest {
    @Mock
    private TrainerService trainerService;

    @Mock
    private TraineeService traineeService;

    @Mock
    private AuthService authService;

    @InjectMocks
    private TrainerController trainerController;

    private ObjectMapper objectMapper;

    private MockMvc mockMvc;

    @BeforeEach
    public void setUp() {
        objectMapper = new ObjectMapper();
        mockMvc = MockMvcBuilders.standaloneSetup(trainerController).build();
    }

    @Test
    public void registerTrainer_shouldReturn201() throws Exception {
        CreateTrainerRequest request = new CreateTrainerRequest();
        request.setFirstName("Joel");
        request.setLastName("Miller");
        request.setSpecialization("Strength Training");

        TrainerRegistrationResponseDto response = new TrainerRegistrationResponseDto(
                "Joel.Miller",
                "secret"
        );

        when(trainerService.createTrainer(any(CreateTrainerRequest.class))).thenReturn(response);

        mockMvc.perform(post("/api/trainers")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated());
    }

    @Test
    public void registerTrainer_shouldReturn201AndBody() throws Exception {
        CreateTrainerRequest request = new CreateTrainerRequest();
        request.setFirstName("Joel");
        request.setLastName("Miller");
        request.setSpecialization("Strength Training");

        TrainerRegistrationResponseDto response = new TrainerRegistrationResponseDto(
                "Joel.Miller",
                "secret"
        );

        when(trainerService.createTrainer(any(CreateTrainerRequest.class))).thenReturn(response);

        mockMvc.perform(post("/api/trainers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.username").value("Joel.Miller"))
                .andExpect(jsonPath("$.password").value("secret"));
    }

    @Test
    public void getTrainerProfile_returns200() throws Exception {
        String username = "Joel.Miller";

        TrainerProfileDto trainerProfileDto = new TrainerProfileDto();
        trainerProfileDto.setFirstName("Joel");
        trainerProfileDto.setLastName("Miller");
        trainerProfileDto.setSpecialization("Balance Training");
        trainerProfileDto.setActive(true);

        when(trainerService.selectTrainerByUsername(username)).thenReturn(Optional.of(trainerProfileDto));

        mockMvc.perform(get("/api/trainers/{username}", username))
                .andExpect(status().isOk());
    }

    @Test
    public void updateTrainerProfile_returns200() throws Exception {
        String username = "Joel.Miller";
        String password = "secret";

        UpdateTrainerProfileRequestDto request = new UpdateTrainerProfileRequestDto();
        request.setFirstName("Joel");
        request.setLastName("Miller");
        request.setIsActive(true);

        TraineeSummaryDto traineeSummaryDto = new TraineeSummaryDto();
        traineeSummaryDto.setUsername("jane.trainee");
        traineeSummaryDto.setFirstName("Jane");
        traineeSummaryDto.setLastName("Doe");

        UpdateTrainerProfileResponseDto response =
                new UpdateTrainerProfileResponseDto(
                        username,
                        request.getFirstName(),
                        request.getLastName(),
                        "Strength Training",
                        request.getIsActive(),
                        List.of(traineeSummaryDto)
                );

        when(trainerService.updateTrainer(
                any(UpdateTrainerProfileRequestDto.class),
                eq(username),
                eq(password)
        )).thenReturn(response);

        mockMvc.perform(put("/api/trainers/{username}", username)
                        .param("password", password)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value(username))
                .andExpect(jsonPath("$.firstName").value("Joel"))
                .andExpect(jsonPath("$.lastName").value("Miller"))
                .andExpect(jsonPath("$.isActive").value(true));
    }

    @Test
    public void activateDeactivateTrainer_returns200() throws Exception {
        String username = "Joel.Miller";
        String authUsername = "Joel.Miller";
        String password = "secret";

        mockMvc.perform(patch("/api/trainers/{username}/status", username)
                        .param("username", authUsername)
                        .param("password", password)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"isActive\": true}"))
                .andExpect(status().isOk());
    }

    @Test
    public void changePassword_returns200() throws Exception {
        String username = "Joel.Miller";
        String oldPassword = "secret";
        String newPassword = "newSecret";

        mockMvc.perform(put("/api/trainers/{username}/password", username)
                        .param("oldPassword", oldPassword)
                        .param("newPassword", newPassword))
                .andExpect(status().isOk());
    }

    @Test
    public void getTrainerTrainings_returns200() throws Exception {
        String username = "Joel.Miller";

        TrainerTrainingResponseDto training1 = new TrainerTrainingResponseDto(
                "Morning Cardio",
                LocalDate.of(2024, 1, 15),
                "Cardio",
                60,
                "john.smith"
        );
        TrainerTrainingResponseDto training2 = new TrainerTrainingResponseDto(
                "Evening Strength",
                LocalDate.of(2024, 1, 16),
                "Strength",
                45,
                "mary.trainee"
        );

        when(trainerService.selectTrainerByUsername(username)).thenReturn(Optional.of(new TrainerProfileDto()));
        when(trainerService.getTrainingsByUsernameAndCriteria(
                eq(username), any(), any(), any()
        )).thenReturn(List.of(training1, training2));

        mockMvc.perform(get("/api/trainers/{username}/trainings", username)
                        .param("period_from", "2024-01-01")
                        .param("period_to", "2024-12-31")
                        .param("trainee_name", "john"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].trainingName").value("Morning Cardio"))
                .andExpect(jsonPath("$[0].traineeName").value("john.smith"))
                .andExpect(jsonPath("$[1].trainingName").value("Evening Strength"));
    }

    @Test
    public void getUnassignedTrainers_returns200() throws Exception {
        String username = "John.Smith";

        TrainerSummaryDto trainer1 = new TrainerSummaryDto();
        trainer1.setUsername("jane.trainer");
        trainer1.setFirstName("Jane");
        trainer1.setLastName("Doe");
        trainer1.setSpecialization("Strength Training");

        TrainerSummaryDto trainer2 = new TrainerSummaryDto();
        trainer2.setUsername("bob.trainer");
        trainer2.setFirstName("Bob");
        trainer2.setLastName("Smith");
        trainer2.setSpecialization("Cardio");

        when(traineeService.selectTraineeByUsername(username)).thenReturn(Optional.of(new com.epam_final_project.dto.trainee.response.TraineeProfileDto()));
        when(trainerService.getTrainersNotAssignedOnTraineeByTraineeUsername(username))
                .thenReturn(List.of(trainer1, trainer2));

        mockMvc.perform(get("/api/trainers/{username}/unassigned-trainers", username))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].username").value("jane.trainer"))
                .andExpect(jsonPath("$[1].username").value("bob.trainer"));
    }
}