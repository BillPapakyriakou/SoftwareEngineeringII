package myy803.traineeship_app.controllers.searchstrategies;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import myy803.traineeship_app.domain.Student;
import myy803.traineeship_app.domain.TraineeshipPosition;
import myy803.traineeship_app.mappers.TraineeshipPositionsMapper;

@Component
public class SearchBasedOnInterests extends AbstractPositionsSearchStrategy {

	@Autowired
	private TraineeshipPositionsMapper positionsMapper;

	@Override
	protected List<TraineeshipPosition> findMatchingPositions(Student applicant) {
		List<TraineeshipPosition> matchingPositions = new ArrayList<>();

		if (applicant.getInterests() == null || applicant.getInterests().isBlank()) {
			return matchingPositions;
		}

		String[] interests = applicant.getInterests().split("\\s*,\\s*|\\s+|\\.");

		List<TraineeshipPosition> allPositions = positionsMapper.findByTopicsContainingAndIsAssignedFalse("");

		for (TraineeshipPosition position : allPositions) {
			if (position.getTopics() == null || position.getTopics().isBlank())
				continue;

			String[] positionTopics = position.getTopics().split("\\s*,\\s*");

			boolean matched = false;
			for (String interest : interests) {
				interest = interest.trim();
				if (interest.isEmpty())
					continue;

				for (String topic : positionTopics) {
					if (topic.trim().equalsIgnoreCase(interest)) {
						matchingPositions.add(position);
						matched = true;
						break;
					}
				}
				if (matched)
					break;
			}
		}
		return matchingPositions;
	}

}
