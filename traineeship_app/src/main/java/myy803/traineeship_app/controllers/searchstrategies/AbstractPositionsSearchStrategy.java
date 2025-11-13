package myy803.traineeship_app.controllers.searchstrategies;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;

import myy803.traineeship_app.domain.Student;
import myy803.traineeship_app.domain.TraineeshipPosition;
import myy803.traineeship_app.mappers.StudentMapper;

public abstract class AbstractPositionsSearchStrategy implements PositionsSearchStrategy {

    @Autowired
    private StudentMapper studentMapper;

    @Override
    public List<TraineeshipPosition> search(String applicantUsername){
        Student applicant = studentMapper.findByUsername(applicantUsername);
        Set<TraineeshipPosition> matchingPositionsSet = new HashSet<TraineeshipPosition>();

        List<TraineeshipPosition> matchingPositions = findMatchingPositions(applicant);
        matchingPositionsSet.addAll(matchingPositions);

        return new ArrayList<>(matchingPositionsSet);
    }

    protected abstract List<TraineeshipPosition> findMatchingPositions(Student applicant);
}