package com.epam_final_project.service;

public interface AuthService {
    boolean checkTraineeUsernameAndPassword(Integer traineeId, String username, String password);
    boolean checkTrainerUsernameAndPassword(Integer trainerId, String username, String password);
}