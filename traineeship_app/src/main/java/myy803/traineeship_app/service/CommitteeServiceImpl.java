package myy803.traineeship_app.service;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import myy803.traineeship_app.domain.Student;
import myy803.traineeship_app.domain.Evaluation;
import myy803.traineeship_app.domain.EvaluationType;
import myy803.traineeship_app.domain.TraineeshipPosition;
import myy803.traineeship_app.mappers.StudentMapper;
import myy803.traineeship_app.mappers.TraineeshipPositionsMapper;
import myy803.traineeship_app.controllers.searchstrategies.*;
import myy803.traineeship_app.controllers.supervisorsearchstrategies.*;

import java.util.*;

@Service
public class CommitteeServiceImpl implements CommitteeService {

    private final PositionsSearchFactory positionsSearchFactory;
    private final SupervisorAssigmentFactory supervisorAssigmentFactory;
    private final StudentMapper studentMapper;
    private final TraineeshipPositionsMapper positionsMapper;

    @Autowired
    public CommitteeServiceImpl(PositionsSearchFactory positionsSearchFactory,
                                SupervisorAssigmentFactory supervisorAssigmentFactory,
                                StudentMapper studentMapper,
                                TraineeshipPositionsMapper positionsMapper) {
        this.positionsSearchFactory = positionsSearchFactory;
        this.supervisorAssigmentFactory = supervisorAssigmentFactory;
        this.studentMapper = studentMapper;
        this.positionsMapper = positionsMapper;
    }

    @Override
    public List<Student> listTraineeshipApplications() {
        return studentMapper.findByLookingForTraineeshipTrue();
    }

    @Override
    public List<TraineeshipPosition> findPositions(String studentUsername, String strategy) {
        PositionsSearchStrategy searchStrategy = positionsSearchFactory.create(strategy);
        return searchStrategy.search(studentUsername);
    }

    @Override
    public void assignPosition(Integer positionId, String studentUsername) {
        Student student = studentMapper.findByUsername(studentUsername);
        TraineeshipPosition position = positionsMapper.findById(positionId).get();

        position.setAssigned(true);
        position.setStudent(student);

        student.setAssignedTraineeship(position);
        student.setLookingForTraineeship(false);

        positionsMapper.save(position);
    }

    @Override
    public void assignSupervisor(Integer positionId, String strategy) {
        SupervisorAssignmentStrategy assignmentStrategy = supervisorAssigmentFactory.create(strategy);
        assignmentStrategy.assign(positionId);
    }

    @Override
    public List<TraineeshipPosition> showAssignedPositions() {

        return positionsMapper.findByIsAssignedTrue();
    }

    @Override
    public TraineeshipPosition findById(Integer id) {
        return positionsMapper.findById(id).orElse(null);
    }

    @Override
    public List<Evaluation> getCompanyEvaluations(TraineeshipPosition position) {
        List<Evaluation> companyEvals = new ArrayList<>();
        for (Evaluation ev : position.getEvaluations()) {
            if (ev.getEvaluationType() == EvaluationType.COMPANY_EVALUATION) {
                companyEvals.add(ev);
            }
        }
        return companyEvals;
    }

    @Override
    public List<Evaluation> getProfessorEvaluations(TraineeshipPosition position) {
        List<Evaluation> professorEvals = new ArrayList<>();
        for (Evaluation ev : position.getEvaluations()) {
            if (ev.getEvaluationType() == EvaluationType.PROFESSOR_EVALUATION) {
                professorEvals.add(ev);
            }
        }
        return professorEvals;
    }

    @Override
    public void submitFinalGrade(Integer positionId, boolean grade) {
        TraineeshipPosition position = positionsMapper.findById(positionId).orElse(null);

        position.setPassFailGrade(grade);

        positionsMapper.save(position);
    }
}
