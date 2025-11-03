package myy803.traineeship_app.service;


import java.util.List;
import myy803.traineeship_app.domain.Company;
import myy803.traineeship_app.domain.TraineeshipPosition;

public interface CompanyService {

    void saveProfile(Company company);

    Company retrieveProfile();

    List<TraineeshipPosition> listAvailablePositions();

    TraineeshipPosition showPositionForm();

    void savePosition(TraineeshipPosition traineeshipPosition);

}