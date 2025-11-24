package myy803.traineeship_app.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import myy803.traineeship_app.domain.Student;
import myy803.traineeship_app.domain.TraineeshipPosition;
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

    @GetMapping("/student/logbook")
    public String viewLogbook(Model model){
        Student student = studentService.retrieveProfile();

        if (student.getAssignedTraineeship() == null){
            model.addAttribute("error", "Not assigned position");
            model.addAttribute("student", student);
            return "student/dashboard";
        }

        TraineeshipPosition position = student.getAssignedTraineeship();
        model.addAttribute("traineeshipPosition", position);

        return "student/logbook_form";
    }

    @PostMapping("/student/logbook")
    public String fillLogbook(
            @RequestParam("positionId") Integer positionId,
            @RequestParam("newLogbook") String newLogbook,
            Model model) {

        Student student = studentService.retrieveProfile();

        try {
            studentService.saveLogbook(positionId, newLogbook);
            model.addAttribute("successMessage", "Logbook saved");
        } catch (Exception e) {
            model.addAttribute("errorMessage", "Error saving Logbook: " + e.getMessage());
        }

        model.addAttribute("student", student);

        return "student/dashboard";
    }
}