package myy803.traineeship_app.controllers.searchstrategies;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import myy803.traineeship_app.domain.Company;
import myy803.traineeship_app.domain.Student;
import myy803.traineeship_app.domain.TraineeshipPosition;
import myy803.traineeship_app.mappers.CompanyMapper;

@Component
public class SearchBasedOnLocation extends AbstractPositionsSearchStrategy {

	@Autowired
	private CompanyMapper companyMapper;

	@Override
	protected List<TraineeshipPosition> findMatchingPositions(Student applicant){
		List<TraineeshipPosition> matchingPositions = new ArrayList<>();

		List<Company> companies = companyMapper.findByCompanyLocation(applicant.getPreferredLocation());

		for(Company company : companies)
			matchingPositions.addAll(company.getAvailablePositions());

		return matchingPositions;
	}
}
