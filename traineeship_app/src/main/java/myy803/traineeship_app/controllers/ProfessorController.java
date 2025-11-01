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