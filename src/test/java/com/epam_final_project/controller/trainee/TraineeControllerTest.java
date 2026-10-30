package com.epam_final_project.controller.trainee;

import com.epam_final_project.dto.trainee.request.CreateTraineeRequest;
import com.epam_final_project.dto.trainee.request.UpdateTraineeProfileRequestDto;
import com.epam_final_project.dto.trainee.response.TraineeProfileDto;
import com.epam_final_project.dto.trainee.response.UpdateTraineeProfileResponseDto;
import com.epam_final_project.dto.trainee.response.TraineeRegistrationResponseDto;
import com.epam_final_project.dto.trainer.response.TrainerSummaryDto;
import com.epam_final_project.service.TraineeService;
import com.epam_final_project.service.TrainerService;
import com.epam_final_project.service.AuthService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import tools.jackson.databind.ObjectMapper;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import org.springframework.http.MediaType;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class TraineeControllerTest {
    @Mock
    private TraineeService traineeService;

    @Mock
    private TrainerService trainerService;

    @Mock
    private AuthService authService;

    @InjectMocks
    private TraineeController traineeController;

    private ObjectMapper objectMapper;

    private MockMvc mockMvc;

    @BeforeEach
    public void setUp() {
        objectMapper = new ObjectMapper();
        mockMvc = MockMvcBuilders.standaloneSetup(traineeController).build();
    }

    @Test
    public void registerTrainee_returns201() throws Exception {
        CreateTraineeRequest request = new CreateTraineeRequest();
        request.setFirstName("John");
        request.setLastName("Smith");

        TraineeRegistrationResponseDto response = new TraineeRegistrationResponseDto(
                "John.Smith",
                "secret"
        );

        when(traineeService.createTrainee(any(CreateTraineeRequest.class))).thenReturn(response);

        mockMvc.perform(post("/api/trainees")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated());
    }

    @Test
    public void registerTrainee_returns201AndBody() throws Exception {
        CreateTraineeRequest request = new CreateTraineeRequest();
        request.setFirstName("John");
        request.setLastName("Smith");

        TraineeRegistrationResponseDto response = new TraineeRegistrationResponseDto(
                "John.Smith",
                "secret"
        );

        when(traineeService.createTrainee(any(CreateTraineeRequest.class))).thenReturn(response);
        mockMvc.perform(post("/api/trainees")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.username").value("John.Smith"))
                .andExpect(jsonPath("$.password").value("secret"));
    }

    @Test
    public void getTraineeProfile_returns200() throws Exception {
        TraineeProfileDto traineeProfileDto = new TraineeProfileDto();
        traineeProfileDto.setFirstName("John");
        traineeProfileDto.setLastName("Smith");
        traineeProfileDto.setDateOfBirth(LocalDate.of(1998, 3, 24));
        traineeProfileDto.setAddress("Tbilisi");
        traineeProfileDto.setActive(true);

        TrainerSummaryDto trainerSummaryDto = new TrainerSummaryDto();
        trainerSummaryDto.setFirstName("Joel");
        trainerSummaryDto.setLastName("Miller");
        trainerSummaryDto.setSpecialization("Strength Training");

        traineeProfileDto.setTrainers(List.of(trainerSummaryDto));

        when(traineeService.selectTraineeByUsername("John.Smith", "John.Smith", "secret")).thenReturn(Optional.of(traineeProfileDto));

        mockMvc.perform(get("/api/trainees/John.Smith")
                .param("username", "John.Smith")
                .param("password", "secret"))
                .andExpect(status().isOk());
    }

    @Test
    public void getTraineeProfile_returns200AndBody() throws Exception {
        TraineeProfileDto traineeProfileDto = new TraineeProfileDto();
        traineeProfileDto.setFirstName("John");
        traineeProfileDto.setLastName("Smith");
        traineeProfileDto.setDateOfBirth(LocalDate.of(1998, 3, 24));
        traineeProfileDto.setAddress("Tbilisi");
        traineeProfileDto.setActive(true);

        TrainerSummaryDto trainerSummaryDto = new TrainerSummaryDto();
        trainerSummaryDto.setUsername("Joel.Miller");
        trainerSummaryDto.setFirstName("Joel");
        trainerSummaryDto.setLastName("Miller");
        trainerSummaryDto.setSpecialization("Strength Training");

        traineeProfileDto.setTrainers(List.of(trainerSummaryDto));

        when(traineeService.selectTraineeByUsername("John.Smith", "John.Smith", "secret")).thenReturn(Optional.of(traineeProfileDto));

        mockMvc.perform(get("/api/trainees/John.Smith")
                .param("username", "John.Smith")
                .param("password", "secret"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value("John"))
                .andExpect(jsonPath("$.lastName").value("Smith"))
                .andExpect(jsonPath("$.address").value("Tbilisi"))
                .andExpect(jsonPath("$.trainers[0].firstName").value("Joel"))
                .andExpect(jsonPath("$.trainers[0].lastName").value("Miller"))
                .andExpect(jsonPath("$.trainers[0].specialization").value("Strength Training"));
    }

    @Test
    public void updateTraineeProfileReturns200() throws Exception {
        String username = "John.Smith";
        String password = "secret";

        UpdateTraineeProfileRequestDto request = new UpdateTraineeProfileRequestDto();
        request.setFirstName("Michael");
        request.setLastName("Desanta");
        request.setIsActive(true);

        TrainerSummaryDto trainerSummaryDto = new TrainerSummaryDto();
        trainerSummaryDto.setUsername("Joel.Miller");
        trainerSummaryDto.setFirstName("Joel");
        trainerSummaryDto.setLastName("Miller");
        trainerSummaryDto.setSpecialization("Strength Training");

        UpdateTraineeProfileResponseDto response = new UpdateTraineeProfileResponseDto(
                username,
                request.getFirstName(),
                request.getLastName(),
                request.getDateOfBirth(),
                request.getAddress(),
                request.getIsActive(),
                List.of(trainerSummaryDto)
        );

        when(traineeService.updateTrainee(
                any(UpdateTraineeProfileRequestDto.class),
                eq(username),
                eq(password))
        ).thenReturn(response);

        mockMvc.perform(put("/api/trainees/{username}", username)
                        .param("password", password)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());
    }

    @Test
    public void updateTraineeProfileReturns200AndBody() throws Exception {
        String username = "John.Smith";
        String password = "secret";

        UpdateTraineeProfileRequestDto request = new UpdateTraineeProfileRequestDto();
        request.setFirstName("Michael");
        request.setLastName("Desanta");
        request.setIsActive(true);

        TrainerSummaryDto trainerSummaryDto = new TrainerSummaryDto();
        trainerSummaryDto.setUsername("Joel.Miller");
        trainerSummaryDto.setFirstName("Joel");
        trainerSummaryDto.setLastName("Miller");
        trainerSummaryDto.setSpecialization("Strength Training");

        UpdateTraineeProfileResponseDto response = new UpdateTraineeProfileResponseDto(
                username,
                request.getFirstName(),
                request.getLastName(),
                request.getDateOfBirth(),
                request.getAddress(),
                request.getIsActive(),
                List.of(trainerSummaryDto)
        );

        when(traineeService.updateTrainee(
                any(UpdateTraineeProfileRequestDto.class),
                eq(username),
                eq(password))
        ).thenReturn(response);

        mockMvc.perform(put("/api/trainees/{username}", username)
                        .param("password", password)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value(username))
                .andExpect(jsonPath("$.firstName").value("Michael"))
                .andExpect(jsonPath("$.lastName").value("Desanta"))
                .andExpect(jsonPath("$.trainers[0].firstName").value("Joel"))
                .andExpect(jsonPath("$.trainers[0].lastName").value("Miller"))
                .andExpect(jsonPath("$.trainers[0].specialization").value("Strength Training"));
    }

    @Test
    public void deleteTraineeProfile_returns200() throws Exception {
        String traineeUsername = "John.Smith";
        String username = "John.Smith";
        String password = "secret";

        mockMvc.perform(delete("/api/trainees/{username}", traineeUsername)
                        .param("username", username)
                        .param("password", password))
                .andExpect(status().isOk());
    }

    @Test
    public void updateTraineeTrainerList_returns200() throws Exception {
        String traineeUsername = "John.Smith";
        String username = "John.Smith";
        String password = "secret";

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

        List<TrainerSummaryDto> expectedTrainers = List.of(trainer1, trainer2);

        when(traineeService.updateTraineeTrainers(
                eq(traineeUsername),
                anyList(),
                eq(username),
                eq(password)
        )).thenReturn(expectedTrainers);

        mockMvc.perform(put("/api/trainees/{traineeUsername}/trainers", traineeUsername)
                        .param("username", username)
                        .param("password", password)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("[\"jane.trainer\", \"bob.trainer\"]"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].username").value("jane.trainer"))
                .andExpect(jsonPath("$[1].username").value("bob.trainer"));
    }

    @Test
    public void activateDeactivateTrainee_returns200() throws Exception {
        String username = "John.Smith";
        String authUsername = "John.Smith";
        String password = "secret";

        mockMvc.perform(patch("/api/trainees/{username}/status", username)
                        .param("username", authUsername)
                        .param("password", password)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"isActive\": true}"))
                .andExpect(status().isOk());
    }

    @Test
    public void changePassword_returns200() throws Exception {
        String username = "John.Smith";
        String oldPassword = "secret";
        String newPassword = "newSecret";

        mockMvc.perform(put("/api/trainees/{username}/password", username)
                        .param("oldPassword", oldPassword)
                        .param("newPassword", newPassword))
                .andExpect(status().isOk());
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

        when(traineeService.selectTraineeByUsername(username, username, "secret")).thenReturn(Optional.of(new TraineeProfileDto()));
        when(trainerService.getTrainersNotAssignedOnTraineeByTraineeUsername(username, username, "secret"))
                .thenReturn(List.of(trainer1, trainer2));

        mockMvc.perform(get("/api/trainees/{username}/unassigned-trainers", username)
                        .param("username", username)
                        .param("password", "secret"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].username").value("jane.trainer"))
                .andExpect(jsonPath("$[1].username").value("bob.trainer"));
    }
}