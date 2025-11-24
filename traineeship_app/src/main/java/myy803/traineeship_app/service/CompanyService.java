package myy803.traineeship_app.service;

import myy803.traineeship_app.domain.Company;
import myy803.traineeship_app.domain.Evaluation;
import myy803.traineeship_app.domain.TraineeshipPosition;

import java.util.List;

public interface CompanyService {

    void saveProfile(Company company);

    Company retrieveProfile();

    List<TraineeshipPosition> listAvailablePositions();

    TraineeshipPosition showPositionForm();

    void savePosition(TraineeshipPosition traineeshipPosition);

    List<TraineeshipPosition> retrieveAssignedPositions();

    void deletePosition(Integer positionId);

    void saveEvaluation(Evaluation evaluation, Integer positionId);
}