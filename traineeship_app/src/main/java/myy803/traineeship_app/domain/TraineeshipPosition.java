package myy803.traineeship_app.domain;

import java.time.LocalDate;
import java.util.List;

import jakarta.persistence.*;

@Entity
@Table(name="traneeship_positions")
public class TraineeshipPosition {
	@Id
	@Column(name="id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@Column(name="title")
	private String title;
	
	@Column(name="description")
	private String description;
	
	@Column(name="from_date")
	private LocalDate fromDate;

	@Column(name="to_date")
	private LocalDate toDate;

	@Column(name="topics")
	private String topics;
	
	@Column(name="skills")
	private String skills;
	
	@Column(name="is_assigned")
	private boolean isAssigned;
	
	@Column(name="student_log_book")
	private String studentLogbook;
	
	@Column(name="pass_fail")
	private boolean passFailGrade;
	
	@OneToOne(fetch = FetchType.EAGER, 
			mappedBy = "assignedTraineeship",
			cascade = {CascadeType.PERSIST,CascadeType.MERGE,CascadeType.REFRESH})
	private Student student;
	
	@ManyToOne(fetch = FetchType.EAGER, cascade = {CascadeType.PERSIST,CascadeType.MERGE,CascadeType.REFRESH}) 
    @JoinColumn(name = "professor_username")	
	private Professor supervisor;
	
	@ManyToOne(fetch = FetchType.EAGER, cascade = {CascadeType.PERSIST,CascadeType.MERGE,CascadeType.REFRESH}) 
    @JoinColumn(name = "company_username", referencedColumnName = "username")	
	private Company company;

	@OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "position_id")
	private List<Evaluation> evaluations;
	
	public TraineeshipPosition() {
		super();
	}

	public TraineeshipPosition(Integer id, String title, String description, LocalDate fromDate, LocalDate toDate,
			String topics, String skills, boolean isAssigned, String studentLogBook, boolean passFailGrade,
			Student student, Professor supervisor, Company company, List<Evaluation> evaluations) {
		super();
		this.id = id;
		this.title = title;
		this.description = description;
		this.fromDate = fromDate;
		this.toDate = toDate;
		this.topics = topics;
		this.skills = skills;
		this.isAssigned = isAssigned;
		this.studentLogbook = studentLogBook;
		this.passFailGrade = passFailGrade;
		this.student = student;
		this.supervisor = supervisor;
		this.company = company;
		this.evaluations = evaluations;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public LocalDate getFromDate() {
		return fromDate;
	}

	public void setFromDate(LocalDate fromDate) {
		this.fromDate = fromDate;
	}

	public LocalDate getToDate() {
		return toDate;
	}

	public void setToDate(LocalDate toDate) {
		this.toDate = toDate;
	}

	public String getTopics() {
		return topics;
	}

	public void setTopics(String topics) {
		this.topics = topics;
	}

	public String getSkills() {
		return skills;
	}

	public void setSkills(String skills) {
		this.skills = skills;
	}

	public boolean isAssigned() {
		return isAssigned;
	}

	public void setAssigned(boolean isAssigned) {
		this.isAssigned = isAssigned;
	}

	public String getStudentLogbook() {
		return studentLogbook;
	}

	public void setStudentLogbook(String studentLogBook) {
		this.studentLogbook = studentLogBook;
	}

	public boolean isPassFailGrade() {
		return passFailGrade;
	}

	public void setPassFailGrade(boolean passFailGrade) {
		this.passFailGrade = passFailGrade;
	}

	public Student getStudent() {
		return student;
	}

	public void setStudent(Student student) {
		this.student = student;
	}

	public Professor getSupervisor() {
		return supervisor;
	}

	public void setSupervisor(Professor supervisor) {
		this.supervisor = supervisor;
	}

	public Company getCompany() {
		return company;
	}

	public void setCompany(Company company) {
		this.company = company;
	}

	public List<Evaluation> getEvaluations() {
		return evaluations;
	}

	public void setEvaluations(List<Evaluation> evaluations) {
		this.evaluations = evaluations;
	}

}
