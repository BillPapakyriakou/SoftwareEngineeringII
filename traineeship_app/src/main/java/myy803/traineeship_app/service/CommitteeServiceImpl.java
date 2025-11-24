package myy803.traineeship_app.service;

import myy803.traineeship_app.controllers.searchstrategies.*;
import myy803.traineeship_app.controllers.supervisorsearchstrategies.*;

import org.springframework.stereotype.Service;

import myy803.traineeship_app.domain.Student;
import myy803.traineeship_app.domain.TraineeshipPosition;
import myy803.traineeship_app.mappers.StudentMapper;
import myy803.traineeship_app.mappers.TraineeshipPositionsMapper;

import java.util.List;

@Service
public class CommitteeServiceImpl implements CommitteeService {

    private final PositionsSearchFactory positionsSearchFactory;
    private final SupervisorAssigmentFactory supervisorAssigmentFactory;
    private final StudentMapper studentMapper;
    private final TraineeshipPositionsMapper positionsMapper;

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
}
