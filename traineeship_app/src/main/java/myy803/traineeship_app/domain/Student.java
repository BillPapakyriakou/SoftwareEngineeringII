package myy803.traineeship_app.domain;

import jakarta.persistence.*;

@Entity
@Table(name="students")
public class Student {
	@Id
	@Column(name="username")
	private String username;
	
	@Column(name="student_name")
	private String studentName;
	
	@Column(name="AM")
	private String AM;
	
	@Column(name="average_grade")
	private double avgGrade;
	
	@Column(name="preferred_location")
	private String preferredLocation;
	
	@Column(name="interests")
	private String interests;
	
	@Column(name="skills")
	private String skills;
	
	@Column(name="looking_for_traineeship")
	private boolean lookingForTraineeship;

	@OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "position_id", referencedColumnName = "id")
	private TraineeshipPosition assignedTraineeship;
	
	public Student() {
		super();
	}

	public Student(String username, String studentName, String aM, double avgGrade, String preferredLocation,
			String interests, String skills, boolean lookingForTraineeship, TraineeshipPosition assignedTraineeship) {
		super();
		this.username = username;
		this.studentName = studentName;
		AM = aM;
		this.avgGrade = avgGrade;
		this.preferredLocation = preferredLocation;
		this.interests = interests;
		this.skills = skills;
		this.lookingForTraineeship = lookingForTraineeship;
		this.assignedTraineeship = assignedTraineeship;
	}

	public Student(String studentUsername) {
		this.username = studentUsername;
		this.lookingForTraineeship = true;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getStudentName() {
		return studentName;
	}

	public void setStudentName(String studentName) {
		this.studentName = studentName;
	}

	public String getAM() {
		return AM;
	}

	public void setAM(String aM) {
		AM = aM;
	}

	public double getAvgGrade() {
		return avgGrade;
	}

	public void setAvgGrade(double avgGrade) {
		this.avgGrade = avgGrade;
	}

	public String getPreferredLocation() {
		return preferredLocation;
	}

	public void setPreferredLocation(String preferredLocation) {
		this.preferredLocation = preferredLocation;
	}

	public String getInterests() {
		return interests;
	}

	public void setInterests(String interests) {
		this.interests = interests;
	}

	public String getSkills() {
		return skills;
	}

	public void setSkills(String skills) {
		this.skills = skills;
	}

	public boolean isLookingForTraineeship() {
		return lookingForTraineeship;
	}

	public void setLookingForTraineeship(boolean lookingForTraineeship) {
		this.lookingForTraineeship = lookingForTraineeship;
	}

	public TraineeshipPosition getAssignedTraineeship() {
		return assignedTraineeship;
	}

	public void setAssignedTraineeship(TraineeshipPosition assignedTraineeship) {
		this.assignedTraineeship = assignedTraineeship;
	}
}

//No need to keep that list. A recommendations button returns 
//a list of positions that the student can accept, the question 
//is what happens if two students press the button at that same time ?? 	
//@OneToMany(
//			cascade=CascadeType.ALL, 
//			fetch= FetchType.LAZY, 
//			mappedBy="applicant")	// this is bidirectional 
//	private List<Application> applications;
//	
	
//Every student has his own preferred strategy for finding a position. 
	// The committee gets a list of matching positions and decides which one to assign.  