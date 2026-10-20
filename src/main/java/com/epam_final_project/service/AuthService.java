package com.epam_final_project.service;

public interface AuthService {
    boolean checkTraineeUsernameAndPassword(String username, String password);
    boolean checkTrainerUsernameAndPassword(String username, String password);
}