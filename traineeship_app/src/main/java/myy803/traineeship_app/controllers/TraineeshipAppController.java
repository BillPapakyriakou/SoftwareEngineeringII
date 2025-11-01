package myy803.traineeship_app.controllers;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import myy803.traineeship_app.controllers.searchstrategies.*;
import myy803.traineeship_app.controllers.supervisorsearchstrategies.*;

import myy803.traineeship_app.domain.Company;
import myy803.traineeship_app.domain.Professor;
import myy803.traineeship_app.domain.Student;
import myy803.traineeship_app.domain.TraineeshipPosition;

import myy803.traineeship_app.mappers.CompanyMapper;
import myy803.traineeship_app.mappers.ProfessorMapper;
import myy803.traineeship_app.mappers.StudentMapper;
import myy803.traineeship_app.mappers.TraineeshipPositionsMapper;

@Controller
public class TraineeshipAppController {

	@Autowired
	private PositionsSearchFactory positionsSearchFactory;
	
	@Autowired
	SupervisorAssigmentFactory  supervisorAssigmentFactory;
	
	@Autowired
	private StudentMapper studentMapper;
	
	@Autowired
	private ProfessorMapper professorMapper;
	
	@Autowired
	private TraineeshipPositionsMapper positionsMapper;
	
	@Autowired
	CompanyMapper companyMapper;
	
	// ---------- Committee User Stories
	
	@RequestMapping("/committee/dashboard")
    public String getCommitteeDashboard(){
       
    	return "committee/dashboard";
    }
    
	@RequestMapping("/committee/list_traineeship_applications")
	public String listTraineeshipApplications(Model model) {
		List<Student> traineeshipApplications = studentMapper.findByLookingForTraineeshipTrue();

		model.addAttribute("traineeship_applications", traineeshipApplications);
		return "committee/traineeship_applications";
	}
	
	@RequestMapping("/committee/find_positions")
	public String findPositions(
			@RequestParam("selected_student_id") String studentUsername, 
			@RequestParam("strategy") String strategy, Model model) {
				
		PositionsSearchStrategy searchStrategy = positionsSearchFactory.create(strategy);
		List<TraineeshipPosition> positions = searchStrategy.search(studentUsername);
		
		model.addAttribute("positions", positions);
		model.addAttribute("student_username", studentUsername);
		
		return "committee/available_positions";
	}
	
	@RequestMapping("/committee/assign_position")
	public String assignPosition(
			@RequestParam("selected_position_id") Integer positionId, 
			@RequestParam("applicant_username") String studentUsername, 
			Model model) {
				
		Student student = studentMapper.findByUsername(studentUsername);
		TraineeshipPosition position = positionsMapper.findById(positionId).get();
		
		position.setAssigned(true);
		position.setStudent(student);
		
		student.setAssignedTraineeship(position);
		student.setLookingForTraineeship(false);
		
		positionsMapper.save(position);
		
		model.addAttribute("position_id", positionId);
		
		return "committee/supervisor_assignment";
	}

	@RequestMapping("/committee/assign_supervisor")
	public String assignSupervisor(
			@RequestParam("selected_position_id") Integer positionId, 
			@RequestParam("strategy") String strategy, 
			Model model) {
		
		SupervisorAssignmentStrategy assignmentStrategy = supervisorAssigmentFactory.create(strategy);
		assignmentStrategy.assign(positionId);
		
    	return "committee/dashboard";
	}
	
	// ---------- Student User Stories
	
    @RequestMapping("/student/dashboard")
    public String getStudentDashboard(){
       
    	return "student/dashboard";
    }
    
    @RequestMapping("/student/profile")
    public String retrieveStudentProfile(Model model){
    	Authentication authentication = SecurityContextHolder.getContext().getAuthentication();	
    	String studentUsername = authentication.getName();
    	System.err.println("Logged use: " + studentUsername);
    	    	
    	Student student = studentMapper.findByUsername(studentUsername);
		if (student == null)
			student = new Student(studentUsername);
		
    	model.addAttribute("student", student);
       	
    	return "student/profile";
    }
    
    @RequestMapping("/student/save_profile")
    public String saveProfile(@ModelAttribute("student") Student student, Model theModel) {
    	
    	student.setLookingForTraineeship(true);
		studentMapper.save(student);
		
    	return "student/dashboard";
    }
    
	
	// ---------- Professor User Stories

    @RequestMapping("/professor/dashboard")
    public String getProfessorDashboard(){
       
    	return "professor/dashboard";
    }
    
    @RequestMapping("/professor/profile")
    public String retrieveProfessorProfile(Model model){
    	Authentication authentication = SecurityContextHolder.getContext().getAuthentication();	
    	String username = authentication.getName();
    	System.err.println("Logged use: " + username);
    	    	
    	Professor professor = professorMapper.findByUsername(username);
		if (professor == null)
			professor = new Professor(username);
    	
    	model.addAttribute("professor", professor);
    	
    	return "professor/profile";
    }
    
    @RequestMapping("/professor/save_profile")
    public String saveProfile(@ModelAttribute("profile") Professor professor, Model theModel) {
    	
		professorMapper.save(professor);
    	
    	return "professor/dashboard";
    }
    
	// ---------- Company User Stories
    	
    @RequestMapping("/company/dashboard")
    public String getCompanyDashboard(){
       
    	return "company/dashboard";
    }
    
    @RequestMapping("/company/profile")
    public String retrieveCompanyProfile(Model model){
    	Authentication authentication = SecurityContextHolder.getContext().getAuthentication();	
    	String username = authentication.getName();
    	System.err.println("Logged use: " + username);    	
    	
    	Company company = companyMapper.findByUsername(username);
		if (company == null)
			company = new Company(username);
		
		model.addAttribute("company", company);
    	
    	return "company/profile";
    }
    
    @RequestMapping("/company/save_profile")
    public String saveProfile(@ModelAttribute("profile") Company company, Model theModel) {
    	
		companyMapper.save(company);
		
    	return "company/dashboard";
    }
    
    @RequestMapping("/company/list_available_positions")
    public String listAvailablePositions(Model model){
    	Authentication authentication = SecurityContextHolder.getContext().getAuthentication();	
    	String username = authentication.getName();
    	System.err.println("Logged use: " + username);
    	    	
    	Company company = companyMapper.findByUsername(username);
    	List<TraineeshipPosition> positions = company.getAvailablePositions();
    	
    	model.addAttribute("positions", positions);
    	
    	return "company/available_positions";
    }

    @RequestMapping("/company/show_position_form")
    String showPositionForm(Model model) {
    	
    	TraineeshipPosition position = new TraineeshipPosition();  
    
    	model.addAttribute("position", position);
    	
		return "company/position";
    	
    }
    
    @RequestMapping("/company/save_position")
    public String savePosition(@ModelAttribute("position") TraineeshipPosition position, Model model) {
    	Authentication authentication = SecurityContextHolder.getContext().getAuthentication();	
    	String username = authentication.getName();

    	Company company = companyMapper.findByUsername(username);
		position.setCompany(company);
		company.addPosition(position);
		companyMapper.save(company);
		
		return "redirect:/company/dashboard";
    }

}
