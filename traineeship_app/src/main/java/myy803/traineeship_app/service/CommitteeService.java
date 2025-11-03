package myy803.traineeship_app.service;

import myy803.traineeship_app.domain.Student;
import myy803.traineeship_app.domain.TraineeshipPosition;

import java.util.List;

public interface CommitteeService {

    List<Student> listTraineeshipApplications();

    List<TraineeshipPosition> findPositions(String studentUsername, String strategy);

    void assignPosition(Integer positionId, String studentUsername);

    void assignSupervisor(Integer positionId, String strategy);
}
