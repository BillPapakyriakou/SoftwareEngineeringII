package myy803.traineeship_app.controllers;

import java.util.List;

import myy803.traineeship_app.service.CommitteeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import myy803.traineeship_app.domain.Student;
import myy803.traineeship_app.domain.TraineeshipPosition;


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

}