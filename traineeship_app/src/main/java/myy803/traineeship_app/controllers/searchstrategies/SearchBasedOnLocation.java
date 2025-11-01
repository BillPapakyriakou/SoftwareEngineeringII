package myy803.traineeship_app.controllers.searchstrategies;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import myy803.traineeship_app.domain.Company;
import myy803.traineeship_app.domain.Student;
import myy803.traineeship_app.domain.TraineeshipPosition;
import myy803.traineeship_app.mappers.CompanyMapper;
import myy803.traineeship_app.mappers.StudentMapper;

@Component
public class SearchBasedOnLocation implements PositionsSearchStrategy {

	@Autowired
	private CompanyMapper companyMapper;
	
	@Autowired
	private StudentMapper studentMapper;
	
	@Override
	public List<TraineeshipPosition> search(String applicantUsername) {
		
		Student applicant = studentMapper.findByUsername(applicantUsername);
		Set<TraineeshipPosition> matchingPositionsSet = new HashSet<TraineeshipPosition>();

		List<Company> companies = companyMapper.findByCompanyLocation(
				applicant.getPreferredLocation()
				);
		
		for(Company company : companies)
			matchingPositionsSet.addAll(company.getAvailablePositions());
		
		return new ArrayList<TraineeshipPosition>(matchingPositionsSet);
	}

}
