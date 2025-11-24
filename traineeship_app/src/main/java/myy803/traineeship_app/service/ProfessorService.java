package myy803.traineeship_app.service;

import myy803.traineeship_app.domain.Professor;
import myy803.traineeship_app.domain.Evaluation;
import myy803.traineeship_app.domain.TraineeshipPosition;

import java.util.List;

public interface ProfessorService {

    void saveProfile(Professor professor);

    Professor retrieveProfile();

    List<TraineeshipPosition> retrieveSupervisedPositions();

    void saveEvaluation(Evaluation evaluation, Integer positionId);
}