package myy803.traineeship_app.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import myy803.traineeship_app.domain.Student;
import myy803.traineeship_app.mappers.StudentMapper;


@Controller
public class StudentController {

    @Autowired
    private StudentMapper studentMapper;


    // ---------- Student User Stories

    @RequestMapping("/student/dashboard")
    public String getStudentDashboard() {

        return "student/dashboard";
    }

    @RequestMapping("/student/profile")
    public String retrieveStudentProfile(Model model) {
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

}