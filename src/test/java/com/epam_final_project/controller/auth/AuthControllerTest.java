package com.epam_final_project.controller.auth;

import com.epam_final_project.dto.auth.request.ChangeLoginRequestDto;
import com.epam_final_project.exception.GlobalExceptionHandler;
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

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class AuthControllerTest {
    @Mock
    private AuthService authService;

    @InjectMocks
    private AuthController authController;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @BeforeEach
    public void setUp() {
        objectMapper = new ObjectMapper();
        mockMvc = MockMvcBuilders.standaloneSetup(authController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }

    @Test
    public void successfulTraineeLogin_shouldReturn200() throws Exception {
        String username = "Joel.Miller";
        String password = "secret";

        when(authService.checkTraineeUsernameAndPassword(username, password)).thenReturn(true);

        mockMvc.perform(post("/api/auth/login")
                        .param("username", username)
                        .param("password", password))
                .andExpect(status().isOk());
    }

    @Test
    public void unsuccessfulTraineeLogin_shouldReturn401() throws Exception {
        String username = "Random.Random";
        String password = "wrong";

        when(authService.checkTraineeUsernameAndPassword(username, password)).thenReturn(false);
        when(authService.checkTrainerUsernameAndPassword(username, password)).thenReturn(false);

        mockMvc.perform(post("/api/auth/login")
                        .param("username", username)
                        .param("password", password))
                .andExpect(status().isUnauthorized());
    }

    @Test
    public void successfulTrainerLogin_shouldReturn200() throws Exception {
        String username = "John.Smith";
        String password = "secret";

        when(authService.checkTrainerUsernameAndPassword(username, password)).thenReturn(true);

        mockMvc.perform(post("/api/auth/login")
                        .param("username", username)
                        .param("password", password))
                .andExpect(status().isOk());
    }

    @Test
    public void unsuccessfulTrainerLogin_shouldReturn401() throws Exception {
        String username = "Random.Random";
        String password = "wrong";

        when(authService.checkTraineeUsernameAndPassword(username, password)).thenReturn(false);
        when(authService.checkTrainerUsernameAndPassword(username, password)).thenReturn(false);

        mockMvc.perform(post("/api/auth/login")
                        .param("username", username)
                        .param("password", password))
                .andExpect(status().isUnauthorized());
    }

    @Test
    public void changePassword_shouldReturn200_whenValidRequest() throws Exception {
        ChangeLoginRequestDto request = new ChangeLoginRequestDto();
        request.setUsername("john.smith");
        request.setOldPassword("oldSecret");
        request.setNewPassword("newSecret");

        doNothing().when(authService).changeLogin("john.smith", "oldSecret", "newSecret");

        mockMvc.perform(put("/api/auth/password")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());
    }

    @Test
    public void changePassword_shouldReturn400_whenInvalidOldPassword() throws Exception {
        ChangeLoginRequestDto request = new ChangeLoginRequestDto();
        request.setUsername("john.smith");
        request.setOldPassword("wrongOldPassword");
        request.setNewPassword("newSecret");

        doThrow(new IllegalArgumentException("Invalid old password"))
                .when(authService).changeLogin("john.smith", "wrongOldPassword", "newSecret");

        mockMvc.perform(put("/api/auth/password")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void changePassword_shouldReturn400_whenUsernameIsMissing() throws Exception {
        ChangeLoginRequestDto request = new ChangeLoginRequestDto();
        request.setOldPassword("oldSecret");
        request.setNewPassword("newSecret");

        mockMvc.perform(put("/api/auth/password")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void changePassword_shouldReturn400_whenOldPasswordIsMissing() throws Exception {
        ChangeLoginRequestDto request = new ChangeLoginRequestDto();
        request.setUsername("john.smith");
        request.setNewPassword("newSecret");

        mockMvc.perform(put("/api/auth/password")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void changePassword_shouldReturn400_whenNewPasswordIsMissing() throws Exception {
        ChangeLoginRequestDto request = new ChangeLoginRequestDto();
        request.setUsername("john.smith");
        request.setOldPassword("oldSecret");

        mockMvc.perform(put("/api/auth/password")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }
}