package com.epam_final_project.controller.trainingtype;

import com.epam_final_project.dto.trainingtype.response.TrainingTypeResponseDto;
import com.epam_final_project.service.TrainingTypeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class TrainingTypeControllerTest {
    @Mock
    private TrainingTypeService trainingTypeService;

    @InjectMocks
    private TrainingTypeController trainingTypeController;

    private MockMvc mockMvc;

    @BeforeEach
    public void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(trainingTypeController).build();
    }

    @Test
    public void getTrainingTypes_shouldReturn200() throws Exception {
        TrainingTypeResponseDto type1 = new TrainingTypeResponseDto("Functional Training", 1);
        TrainingTypeResponseDto type2 = new TrainingTypeResponseDto("Strength Training", 2);
        TrainingTypeResponseDto type3 = new TrainingTypeResponseDto("Cardio Training", 3);

        when(trainingTypeService.getAllTrainingTypes()).thenReturn(List.of(type1, type2, type3));

        mockMvc.perform(get("/api/training_types"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].trainingTypeName").value("Functional Training"))
                .andExpect(jsonPath("$[0].trainingTypeId").value(1))
                .andExpect(jsonPath("$[1].trainingTypeName").value("Strength Training"))
                .andExpect(jsonPath("$[1].trainingTypeId").value(2))
                .andExpect(jsonPath("$[2].trainingTypeName").value("Cardio Training"))
                .andExpect(jsonPath("$[2].trainingTypeId").value(3));
    }
}