package myy803.traineeship_app.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import myy803.traineeship_app.domain.Professor;
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
}