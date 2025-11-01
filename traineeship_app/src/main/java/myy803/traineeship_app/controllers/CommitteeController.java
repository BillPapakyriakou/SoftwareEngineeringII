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
@RequestMapping("/committee")
public class CommitteeController {

    @Autowired
    private PositionsSearchFactory positionsSearchFactory;

    @Autowired
    SupervisorAssigmentFactory  supervisorAssigmentFactory;

    @Autowired
    private StudentMapper studentMapper;

    @Autowired
    private TraineeshipPositionsMapper positionsMapper;


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

}