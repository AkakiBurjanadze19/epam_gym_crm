package com.epam_final_project.controller.training;

import com.epam_final_project.dto.training.request.CreateTrainingRequestDto;
import com.epam_final_project.dto.training.response.TraineeTrainingResponseDto;
import com.epam_final_project.dto.training.response.TrainerTrainingResponseDto;
import com.epam_final_project.service.TraineeService;
import com.epam_final_project.service.TrainerService;
import com.epam_final_project.service.TrainingService;
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
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class TrainingControllerTest {
    @Mock
    private TrainingService trainingService;

    @Mock
    private TraineeService traineeService;

    @Mock
    private TrainerService trainerService;

    @InjectMocks
    private TrainingController trainingController;

    private ObjectMapper objectMapper;

    private MockMvc mockMvc;

    @BeforeEach
    public void setUp() {
        objectMapper = new ObjectMapper();
        mockMvc = MockMvcBuilders.standaloneSetup(trainingController).build();
    }

    @Test
    public void createTraining_shouldReturn200() throws Exception {
        CreateTrainingRequestDto request = new CreateTrainingRequestDto();
        request.setTraineeUsername("john.smith");
        request.setTrainerUsername("jane.trainer");
        request.setTrainingName("Morning Cardio");
        request.setTrainingDate(LocalDate.of(2024, 1, 15));
        request.setTrainingDuration(60);

        mockMvc.perform(post("/api/trainings")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());
    }

    @Test
    public void getTraineeTrainings_shouldReturn200() throws Exception {
        String username = "john.smith";

        TraineeTrainingResponseDto response1 = new TraineeTrainingResponseDto(
                "Morning Cardio",
                LocalDate.of(2024, 1, 15),
                "Cardio",
                60,
                "jane.trainer"
        );
        TraineeTrainingResponseDto response2 = new TraineeTrainingResponseDto(
                "Evening Strength",
                LocalDate.of(2024, 1, 16),
                "Strength",
                45,
                "bob.trainer"
        );

        when(traineeService.selectTraineeByUsername(username)).thenReturn(Optional.of(new com.epam_final_project.dto.trainee.response.TraineeProfileDto()));
        when(traineeService.getTrainingsByUsernameAndCriteria(
                username, null, null, null, null))
                .thenReturn(List.of(response1, response2));

        mockMvc.perform(get("/api/trainings/trainee/{username}", username))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].trainingName").value("Morning Cardio"))
                .andExpect(jsonPath("$[0].trainerName").value("jane.trainer"))
                .andExpect(jsonPath("$[1].trainingName").value("Evening Strength"))
                .andExpect(jsonPath("$[1].trainerName").value("bob.trainer"));
    }

    @Test
    public void getTrainerTrainings_shouldReturn200() throws Exception {
        String username = "jane.trainer";

        TrainerTrainingResponseDto response1 = new TrainerTrainingResponseDto(
                "Morning Cardio",
                LocalDate.of(2024, 1, 15),
                "Cardio",
                60,
                "john.smith"
        );
        TrainerTrainingResponseDto response2 = new TrainerTrainingResponseDto(
                "Evening Strength",
                LocalDate.of(2024, 1, 16),
                "Strength",
                45,
                "mary.trainee"
        );

        when(trainerService.selectTrainerByUsername(username)).thenReturn(Optional.of(new com.epam_final_project.dto.trainer.response.TrainerProfileDto()));
        when(trainerService.getTrainingsByUsernameAndCriteria(
                username, null, null, null))
                .thenReturn(List.of(response1, response2));

        mockMvc.perform(get("/api/trainings/trainer/{username}", username))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].trainingName").value("Morning Cardio"))
                .andExpect(jsonPath("$[0].traineeName").value("john.smith"))
                .andExpect(jsonPath("$[1].trainingName").value("Evening Strength"))
                .andExpect(jsonPath("$[1].traineeName").value("mary.trainee"));
    }
}