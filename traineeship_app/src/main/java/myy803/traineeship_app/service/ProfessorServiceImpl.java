package myy803.traineeship_app.service;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import myy803.traineeship_app.domain.Professor;
import myy803.traineeship_app.domain.Evaluation;
import myy803.traineeship_app.domain.EvaluationType;
import myy803.traineeship_app.domain.TraineeshipPosition;
import myy803.traineeship_app.mappers.ProfessorMapper;
import myy803.traineeship_app.mappers.TraineeshipPositionsMapper;

import java.util.List;

@Service
public class ProfessorServiceImpl implements ProfessorService {

    private final ProfessorMapper professorMapper;
    private final TraineeshipPositionsMapper positionsMapper;

    @Autowired
    public ProfessorServiceImpl(ProfessorMapper professorMapper, TraineeshipPositionsMapper positionsMapper) {
        this.professorMapper = professorMapper;
        this.positionsMapper = positionsMapper;
    }

    @Override
    public void saveProfile(Professor professor) {
        professorMapper.save(professor);
    }

    @Override
    public Professor retrieveProfile() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String professorUsername = authentication.getName();
        System.err.println("Logged user: " + professorUsername);

        Professor professor = professorMapper.findByUsername(professorUsername);
        if (professor == null) {
            professor = new Professor(professorUsername);
        }

        return professor;
    }

    @Override
    public List<TraineeshipPosition> retrieveSupervisedPositions(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String professorUsername = authentication.getName();

        List<TraineeshipPosition> supervisedPositions = positionsMapper.findBySupervisorUsername(professorUsername);

        return supervisedPositions;
    }

    @Override
    public void saveEvaluation(Evaluation evaluation, Integer positionId){
        TraineeshipPosition position = positionsMapper.findById(positionId)
                .orElseThrow(() -> new IllegalArgumentException("Position not found with id: " + positionId));

        if (!position.isAssigned()) {
            throw new IllegalStateException("Evaluation can only be submitted for positions in progress.");
        }

        evaluation.setEvaluationType(EvaluationType.PROFESSOR_EVALUATION);
        position.getEvaluations().add(evaluation);
        positionsMapper.save(position);
    }

}