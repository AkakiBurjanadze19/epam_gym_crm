package com.epam_final_project.controller.auth;

import com.epam_final_project.exception.GlobalExceptionHandler;
import com.epam_final_project.service.AuthService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class AuthControllerTest {
    @Mock
    private AuthService authService;

    @InjectMocks
    private AuthController authController;

    private MockMvc mockMvc;

    @BeforeEach
    public void setUp() {
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
}