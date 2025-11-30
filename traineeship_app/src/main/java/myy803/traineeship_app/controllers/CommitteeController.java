package myy803.traineeship_app.controllers;

import myy803.traineeship_app.service.CommitteeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PathVariable;

import myy803.traineeship_app.domain.Student;
import myy803.traineeship_app.domain.Evaluation;
import myy803.traineeship_app.domain.TraineeshipPosition;

import java.util.*;

@Controller
public class CommitteeController {

    private final CommitteeService committeeService;

    @Autowired
    public CommitteeController(CommitteeService committeeService) {
        this.committeeService = committeeService;
    }

    // ---------- Committee User Stories

    @RequestMapping("/committee/dashboard")
    public String getCommitteeDashboard(){

        return "committee/dashboard";
    }

    @RequestMapping("/committee/list_traineeship_applications")
    public String listTraineeshipApplications(Model model) {
        List<Student> traineeshipApplications = committeeService.listTraineeshipApplications();

        model.addAttribute("traineeship_applications", traineeshipApplications);
        return "committee/traineeship_applications";
    }

    @RequestMapping("/committee/find_positions")
    public String findPositions(
            @RequestParam("selected_student_id") String studentUsername,
            @RequestParam("strategy") String strategy, Model model) {

        List<TraineeshipPosition> positions = committeeService.findPositions(studentUsername, strategy);

        model.addAttribute("positions", positions);
        model.addAttribute("student_username", studentUsername);

        return "committee/available_positions";
    }

    @RequestMapping("/committee/assign_position")
    public String assignPosition(
            @RequestParam("selected_position_id") Integer positionId,
            @RequestParam("applicant_username") String studentUsername,
            Model model) {

        committeeService.assignPosition(positionId, studentUsername);

        model.addAttribute("position_id", positionId);

        return "committee/supervisor_assignment";
    }

    @RequestMapping("/committee/assign_supervisor")
    public String assignSupervisor(
            @RequestParam("selected_position_id") Integer positionId,
            @RequestParam("strategy") String strategy,
            Model model) {

        committeeService.assignSupervisor(positionId, strategy);

        return "committee/dashboard";
    }

    @RequestMapping("/committee/list_assigned_traineeships")
    public String listAssignedPositions(Model model) {
        List<TraineeshipPosition> assignedPositions = committeeService.showAssignedPositions();

        model.addAttribute("positions", assignedPositions);

        return "committee/assigned_positions";
    }

    @GetMapping("/committee/monitor_position")
    public String monitorPosition(
            @RequestParam("positionId") Integer positionId,
            Model model) {

        TraineeshipPosition position = committeeService.findById(positionId);

        List<Evaluation> companyEvaluations = committeeService.getCompanyEvaluations(position);
        List<Evaluation> professorEvaluations = committeeService.getProfessorEvaluations(position);

        model.addAttribute("position", position);
        model.addAttribute("companyEvaluations", companyEvaluations);
        model.addAttribute("professorEvaluations", professorEvaluations);

        return "committee/monitor_view";
    }
    /*
    @PostMapping("/committee/submit_grade")
    public String submitGrade(@RequestParam("positionId") Integer positionId,
                              @RequestParam("grade") boolean grade,
                              Model model) {

        committeeService.submitFinalGrade(positionId, grade);

        List<TraineeshipPosition> assignedPositions = committeeService.showAssignedPositions();

        model.addAttribute("positions", assignedPositions);

        return "committee/assigned_positions";
    }
    */
    @PostMapping("/committee/pass/{positionId}")
    public String pass(@PathVariable("positionId") Integer positionId) {
        committeeService.completeTraineeship(positionId, true);
        return "redirect:/committee/list_assigned_traineeships";
        //return "redirect:/committee/assigned_positions/" + positionId + "?continue";
        //return "redirect:/committee/show-evaluations/" + positionId + "?continue";
    }

    @PostMapping("/committee/fail/{positionId}")
    public String fail(@PathVariable("positionId") Integer positionId) {
        committeeService.completeTraineeship(positionId, false);
        return "redirect:/committee/list_assigned_traineeships";
        //return "redirect:/committee/show-evaluations/" + positionId + "?continue";
    }
}