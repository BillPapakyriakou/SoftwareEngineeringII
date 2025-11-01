package myy803.traineeship_app.domain;

import java.util.List;

import jakarta.persistence.*;

@Entity
@Table(name="professors")
public class Professor {
	
	@Id
	@Column(name="username")
	private String username;
	
	@Column(name="professor_name")
	private String professorName;
	
	@Column(name="interests")
	private String interests;
	
	@OneToMany(
			mappedBy = "supervisor",
			cascade=CascadeType.ALL, 
			fetch= FetchType.LAZY) 
	private List<TraineeshipPosition> supervisedPositions;

	public Professor() {
		super();
	}

	public Professor(String username) {
		super();
		this.username = username;
	}

	public Professor(String username, String professorName, String interests,
			List<TraineeshipPosition> supervisedPositions) {
		super();
		this.username = username;
		this.professorName = professorName;
		this.interests = interests;
		this.supervisedPositions = supervisedPositions;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getProfessorName() {
		return professorName;
	}

	public void setProfessorName(String professorName) {
		this.professorName = professorName;
	}

	public String getInterests() {
		return interests;
	}

	public void setInterests(String interests) {
		this.interests = interests;
	}


	public List<TraineeshipPosition> getSupervisedPositions() {
		return supervisedPositions;
	}

	public void setSupervisedPositions(List<TraineeshipPosition> supervisedPositions) {
		this.supervisedPositions = supervisedPositions;
	}

	public int compareLoad(Professor candidateSupervisor) {
		if(candidateSupervisor.supervisedPositions.size() < supervisedPositions.size())
			return -1;
		return 0;
	}

	public void addPosition(TraineeshipPosition position) {
		supervisedPositions.add(position);
	}

	public boolean match(String[] topics) {
		for(int i = 0; i < topics.length; i++)
			if(interests.contains(topics[i]))
				return true;
			
		return false;
	}
}
