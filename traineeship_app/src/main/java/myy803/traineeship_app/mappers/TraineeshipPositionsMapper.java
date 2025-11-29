package myy803.traineeship_app.mappers;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import myy803.traineeship_app.domain.TraineeshipPosition;

import java.util.List;

@Repository
public interface TraineeshipPositionsMapper extends JpaRepository<TraineeshipPosition, Integer> {
	List<TraineeshipPosition> findByTopicsContaining(String username);

	List<TraineeshipPosition> findByTopicsContainingAndIsAssignedFalse(String username);

	List<TraineeshipPosition> findBySupervisorUsername(String username);

	List<TraineeshipPosition> findByIsAssignedTrue();
}
