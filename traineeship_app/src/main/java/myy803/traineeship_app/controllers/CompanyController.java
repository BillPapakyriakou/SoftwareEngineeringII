package myy803.traineeship_app.controllers;

import java.util.List;

import myy803.traineeship_app.service.CompanyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import myy803.traineeship_app.domain.Company;
import myy803.traineeship_app.domain.TraineeshipPosition;


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
        List<TraineeshipPosition> positions = companyService.listAvailablePositions();

        model.addAttribute("positions", positions);

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


}