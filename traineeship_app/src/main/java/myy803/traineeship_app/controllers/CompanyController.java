package myy803.traineeship_app.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import myy803.traineeship_app.domain.Company;
import myy803.traineeship_app.domain.Evaluation;
import myy803.traineeship_app.domain.TraineeshipPosition;
import myy803.traineeship_app.service.CompanyService;

@Controller
public class CompanyController {

    private final CompanyService companyService;

    @Autowired
    public CompanyController(CompanyService companyService) {
        this.companyService = companyService;
    }

    // ---------- Company User Stories

    @RequestMapping("/company/dashboard")
    public String getCompanyDashboard(){

        return "company/dashboard";
    }

    @RequestMapping("/company/profile")
    public String retrieveCompanyProfile(Model model){
        Company company = companyService.retrieveProfile();

        model.addAttribute("company", company);

        return "company/profile";
    }

    @RequestMapping("/company/save_profile")
    public String saveProfile(@ModelAttribute("profile") Company company, Model theModel) {

        companyService.saveProfile(company);

        return "company/dashboard";
    }

    @RequestMapping("/company/list_available_positions")
    public String listAvailablePositions(Model model){
        List<TraineeshipPosition> availablePositionss = companyService.listAvailablePositions();

        model.addAttribute("positions", availablePositionss);

        return "company/available_positions";
    }

    @RequestMapping("/company/show_position_form")
    String showPositionForm(Model model) {

        TraineeshipPosition position = companyService.showPositionForm();

        model.addAttribute("position", position);

        return "company/position";

    }

    @RequestMapping("/company/save_position")
    public String savePosition(@ModelAttribute("position") TraineeshipPosition position, Model model) {
        companyService.savePosition(position);

        return "redirect:/company/dashboard";
    }

    @RequestMapping("/company/list_assigned_positions")
    public String listAssignedPositions(Model model){
        List<TraineeshipPosition> assignedPositions =  companyService.retrieveAssignedPositions();

        model.addAttribute("positions", assignedPositions);

        return "company/assigned_positions";
    }

    @RequestMapping("/company/delete_position")
    public String deletePosition(@RequestParam("positionId") Integer positionId){
        companyService.deletePosition(positionId);

        return "redirect:/company/list_available_positions";
    }

    @RequestMapping("/company/view_evaluation_form")
    public String viewEvaluationForm(@RequestParam("positionId") Integer positionId,
                                     Model model) {
        model.addAttribute("positionId", positionId);
        model.addAttribute("evaluation", new Evaluation());

        return "company/evaluation_form";
    }

    @RequestMapping("/company/save_evaluation")
    public String saveEvaluation(@RequestParam("positionId") Integer positionId,
                                 @ModelAttribute("evaluation") Evaluation evaluation,
                                 Model model) {

        companyService.saveEvaluation(evaluation, positionId);

        List<TraineeshipPosition> assignedPositions = companyService.retrieveAssignedPositions();
        model.addAttribute("positions", assignedPositions);
        model.addAttribute("successMessage", "Evaluation submitted successfully!");

        return "company/assigned_positions";
    }
}