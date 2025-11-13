package myy803.traineeship_app.controllers.supervisorsearchstrategies;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import myy803.traineeship_app.domain.Professor;
import myy803.traineeship_app.domain.TraineeshipPosition;
import myy803.traineeship_app.mappers.ProfessorMapper;
import myy803.traineeship_app.mappers.TraineeshipPositionsMapper;

@Component
public class AssignmentBasedOnLoad extends AbstractSupervisorAssignmentStrategy {
	
	@Override
	protected Professor selectSupervisor(TraineeshipPosition position, List<Professor> professors) {
		if (professors.isEmpty())
			return null;

		Professor candidateSupervisor = professors.get(0);
		for(Professor professor : professors) {
			if(professor.compareLoad(candidateSupervisor) >= 0) {
				candidateSupervisor = professor;
			}
		}

		return candidateSupervisor;
	}
}
