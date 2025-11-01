package myy803.traineeship_app.domain;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.*;

//change
@Entity
@Table(name="companies")
public class Company {
	@Id
	@Column(name="username")
	private String username;
	
	@Column(name="company_name")
	private String companyName;
	
	@Column(name="company_location")
	private String companyLocation;
	
	@OneToMany(
			mappedBy = "company",
			cascade=CascadeType.ALL, 
			fetch= FetchType.LAZY) // bidirectional
//    @JoinColumn(name = "company_username")	
	private List<TraineeshipPosition> positions;

	public Company(String username, String companyName, String companyLocation, List<TraineeshipPosition> positions) {
		super();
		this.username = username;
		this.companyName = companyName;
		this.companyLocation = companyLocation;
		this.positions = positions;
	}

	public Company() {
		super();
	}

	public Company(String username) {
		this.username = username;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public String getCompanyLocation() {
		return companyLocation;
	}

	public void setCompanyLocation(String companyLocation) {
		this.companyLocation = companyLocation;
	}

	public List<TraineeshipPosition> getPositions() {
		return positions;
	}

	public void setPositions(List<TraineeshipPosition> positions) {
		this.positions = positions;
	}

	public void addPosition(TraineeshipPosition position) {
		positions.add(position);		
	}

	public List<TraineeshipPosition> getAvailablePositions() {
		List<TraineeshipPosition> availablePositions = new ArrayList<TraineeshipPosition>();
		for(TraineeshipPosition position : positions)
			if(position.isAssigned() == false)
				availablePositions.add(position);
		
		return availablePositions;
	}
	
	public List<TraineeshipPosition> getAssignedPositions() {
		List<TraineeshipPosition> assignedPositions = new ArrayList<TraineeshipPosition>();
		for(TraineeshipPosition position : positions)
			if(position.isAssigned() == true)
				assignedPositions.add(position);
		
		return assignedPositions;
	}
}
