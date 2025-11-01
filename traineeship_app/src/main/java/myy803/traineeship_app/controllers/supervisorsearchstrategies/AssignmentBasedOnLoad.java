package myy803.traineeship_app.controllers.supervisorsearchstrategies;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import myy803.traineeship_app.domain.Professor;
import myy803.traineeship_app.domain.TraineeshipPosition;
import myy803.traineeship_app.mappers.ProfessorMapper;
import myy803.traineeship_app.mappers.TraineeshipPositionsMapper;

@Component
public class AssignmentBasedOnLoad implements SupervisorAssignmentStrategy{
	@Autowired
	TraineeshipPositionsMapper positionsMapper;
	
	@Autowired
	ProfessorMapper professorMapper;
	
	@Override
	public void assign(Integer positionId) {
		TraineeshipPosition position = positionsMapper.findById(positionId).get();
		List<Professor> professors = professorMapper.findAll();
		
		Professor candidateSupervisor = professors.get(0);
		for(Professor professor : professors) {
			if(professor.compareLoad(candidateSupervisor) >= 0) {
				candidateSupervisor = professor;
			}
		}
		
		position.setSupervisor(candidateSupervisor);
		candidateSupervisor.addPosition(position);
		positionsMapper.save(position);
	}

}
