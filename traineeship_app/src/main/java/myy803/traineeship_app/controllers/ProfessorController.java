package myy803.traineeship_app.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import myy803.traineeship_app.domain.Professor;
import myy803.traineeship_app.domain.Evaluation;
import myy803.traineeship_app.domain.TraineeshipPosition;
import myy803.traineeship_app.service.ProfessorService;

import java.util.List;

@Controller
public class ProfessorController {

    private ProfessorService professorService;

    @Autowired
    public ProfessorController(ProfessorService professorService) {
        this.professorService = professorService;
    }

    // ---------- Professor User Stories

    @RequestMapping("/professor/dashboard")
    public String getProfessorDashboard() {

        return "professor/dashboard";
    }

    @RequestMapping("/professor/profile")
    public String retrieveProfessorProfile(Model model) {

        Professor professor = professorService.retrieveProfile();
        model.addAttribute("professor", professor);

        return "professor/profile";
    }

    @RequestMapping("/professor/save_profile")
    public String saveProfile(@ModelAttribute("profile") Professor professor, Model theModel) {

        professorService.saveProfile(professor);

        return "professor/dashboard";
    }

    @RequestMapping("/professor/list_supervised_positions")
    public String listSupervisedPositions(Model model){
        List<TraineeshipPosition> positions = professorService.retrieveSupervisedPositions();

        model.addAttribute("positions", positions);

        return "professor/supervised_positions";
    }

    @RequestMapping("/professor/view_evaluation_form")
    public String viewEvaluationForm(@RequestParam("positionId") Integer positionId,
                                     Model model) {
        model.addAttribute("positionId", positionId);
        model.addAttribute("evaluation", new Evaluation());

        return "professor/evaluation_form";
    }

    @RequestMapping("/professor/save_evaluation")
    public String saveEvaluation(@RequestParam("positionId") Integer positionId,
                                 @ModelAttribute("evaluation") Evaluation evaluation,
                                 Model model) {
        professorService.saveEvaluation(evaluation, positionId);
        List<TraineeshipPosition> supervisedPositions = professorService.retrieveSupervisedPositions();
        model.addAttribute("positions", supervisedPositions);
        model.addAttribute("successMessage", "Evaluation submitted successfully!");

        return "professor/supervised_positions";
    }
}