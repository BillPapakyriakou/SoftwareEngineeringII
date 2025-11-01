package myy803.traineeship_app.controllers.searchstrategies;

import java.util.List;

import myy803.traineeship_app.domain.*;

public interface PositionsSearchStrategy {
	List<TraineeshipPosition> search(String applicantUsername);
}
