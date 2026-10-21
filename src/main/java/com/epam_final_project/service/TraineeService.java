package com.epam_final_project.service;

import com.epam_final_project.domain.Trainee;
import com.epam_final_project.domain.Training;

import javax.naming.AuthenticationException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface TraineeService {
    void createTrainee(Trainee trainee);
    void updateTrainee(Trainee updatedTrainee, Integer traineeId, String username, String password)
            throws AuthenticationException;
    void deleteTrainee(Integer traineeId, String username, String password)
            throws AuthenticationException;
    Optional<Trainee> selectTrainee(Integer traineeId);
    Optional<Trainee> selectTraineeByUsername(String username);
    void changeTraineePassword(String updatedPassword, Integer traineeId, String username, String password)
            throws AuthenticationException;
    void activateTrainee(Integer traineeId, String username, String password) throws AuthenticationException;
    void deActivateTrainee(Integer traineeId, String username, String password) throws AuthenticationException;
    void deleteTraineeByUsername(String username);
    List<Training> getTrainingsByUsernameAndCriteria(
            String username,
            LocalDate fromDate,
            LocalDate toDate,
            String trainerName,
            String trainingTypeName
    );
    void updateTraineeTrainers(Integer traineeId, List<Integer> updatedTrainerIds);
}
