package myy803.traineeship_app.controllers.searchstrategies;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import myy803.traineeship_app.domain.Student;
import myy803.traineeship_app.domain.TraineeshipPosition;
import myy803.traineeship_app.mappers.StudentMapper;
import myy803.traineeship_app.mappers.TraineeshipPositionsMapper;

@Component
public class SearchBasedOnInterests implements PositionsSearchStrategy {
	@Autowired
	private TraineeshipPositionsMapper positionsMapper;
	
	@Autowired
	private StudentMapper studentMapper;
	
	@Override
	public List<TraineeshipPosition> search(String applicantUsername) {
		
		Student applicant = studentMapper.findByUsername(applicantUsername);
		Set<TraineeshipPosition> matchingPositionsSet = new HashSet<TraineeshipPosition>();
		
		String[] interests = applicant.getInterests().split("[,\\s+\\.]");
		for(int i = 0; i < interests.length; i++) {
			List<TraineeshipPosition> positions = positionsMapper.findByTopicsContainingAndIsAssignedFalse(
					interests[i]
							);
			matchingPositionsSet.addAll(positions);
		}
		
		return new ArrayList<TraineeshipPosition>(matchingPositionsSet);
	}

}
