package myy803.traineeship_app.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import myy803.traineeship_app.domain.Student;
import myy803.traineeship_app.service.StudentService;


@Controller
public class StudentController {

    private final StudentService studentService;

    @Autowired
    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    // ---------- Student User Stories

    @RequestMapping("/student/dashboard")
    public String getStudentDashboard() {

        return "student/dashboard";
    }

    @RequestMapping("/student/profile")
    public String retrieveStudentProfile(Model model) {

        Student student = studentService.retrieveProfile();
        model.addAttribute("student", student);

        return "student/profile";
    }

    @RequestMapping("/student/save_profile")
    public String saveProfile(@ModelAttribute("student") Student student, Model theModel) {

        studentService.saveProfile(student);

        return "student/dashboard";
    }

}