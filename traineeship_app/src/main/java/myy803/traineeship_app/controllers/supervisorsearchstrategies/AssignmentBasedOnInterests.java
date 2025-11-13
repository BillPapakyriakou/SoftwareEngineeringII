package myy803.traineeship_app.controllers.supervisorsearchstrategies;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import myy803.traineeship_app.domain.Professor;
import myy803.traineeship_app.domain.TraineeshipPosition;
import myy803.traineeship_app.mappers.ProfessorMapper;
import myy803.traineeship_app.mappers.TraineeshipPositionsMapper;

@Component
public class AssignmentBasedOnInterests extends AbstractSupervisorAssignmentStrategy {

	@Override
	protected Professor selectSupervisor(TraineeshipPosition position, List<Professor> professors) {
		if (position.getTopics() == null || position.getTopics().isBlank())
			return null;

		String[] topics = position.getTopics().split("[,\\s+\\.]");
		Professor candidateSupervisor = null;
		for(Professor professor : professors) {
			if(professor.match(topics) == true)
				candidateSupervisor = professor;
		}

		return candidateSupervisor;
	}
}
