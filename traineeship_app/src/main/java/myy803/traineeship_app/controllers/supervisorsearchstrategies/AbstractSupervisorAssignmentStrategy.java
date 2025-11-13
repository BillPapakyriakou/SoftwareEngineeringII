package myy803.traineeship_app.controllers.supervisorsearchstrategies;

import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import myy803.traineeship_app.domain.Professor;
import myy803.traineeship_app.domain.TraineeshipPosition;
import myy803.traineeship_app.mappers.ProfessorMapper;
import myy803.traineeship_app.mappers.TraineeshipPositionsMapper;

public abstract class AbstractSupervisorAssignmentStrategy implements SupervisorAssignmentStrategy{

    @Autowired
    TraineeshipPositionsMapper positionsMapper;

    @Autowired
    ProfessorMapper professorMapper;

    @Override
    public void assign(Integer positionId){
        TraineeshipPosition position = positionsMapper.findById(positionId).get();
        List<Professor> professors = professorMapper.findAll();

        Professor candidateSupervisor = selectSupervisor(position, professors);

        position.setSupervisor(candidateSupervisor);
        candidateSupervisor.addPosition(position);
        positionsMapper.save(position);
    }

    protected abstract Professor selectSupervisor(TraineeshipPosition position, List<Professor> professors);
}