package myy803.traineeship_app.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import myy803.traineeship_app.domain.Professor;
import myy803.traineeship_app.mappers.ProfessorMapper;

@Controller
@RequestMapping("/professor")
public class ProfessorController {

    @Autowired
    private ProfessorMapper professorMapper;

    // ---------- Professor User Stories

    @RequestMapping("/professor/dashboard")
    public String getProfessorDashboard() {

        return "professor/dashboard";
    }

    @RequestMapping("/professor/profile")
    public String retrieveProfessorProfile(Model model) {
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
}