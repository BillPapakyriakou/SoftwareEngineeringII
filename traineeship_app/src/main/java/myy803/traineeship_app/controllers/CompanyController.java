package myy803.traineeship_app.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import myy803.traineeship_app.domain.Company;
import myy803.traineeship_app.domain.TraineeshipPosition;
import myy803.traineeship_app.mappers.CompanyMapper;


@Controller
@RequestMapping("/company")
public class CompanyController {

    @Autowired
    CompanyMapper companyMapper;

    // ---------- Company User Stories

    @RequestMapping("/company/dashboard")
    public String getCompanyDashboard(){

        return "company/dashboard";
    }

    @RequestMapping("/company/profile")
    public String retrieveCompanyProfile(Model model){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        System.err.println("Logged use: " + username);

        Company company = companyMapper.findByUsername(username);
        if (company == null)
            company = new Company(username);

        model.addAttribute("company", company);

        return "company/profile";
    }

    @RequestMapping("/company/save_profile")
    public String saveProfile(@ModelAttribute("profile") Company company, Model theModel) {

        companyMapper.save(company);

        return "company/dashboard";
    }

    @RequestMapping("/company/list_available_positions")
    public String listAvailablePositions(Model model){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        System.err.println("Logged use: " + username);

        Company company = companyMapper.findByUsername(username);
        List<TraineeshipPosition> positions = company.getAvailablePositions();

        model.addAttribute("positions", positions);

        return "company/available_positions";
    }

    @RequestMapping("/company/show_position_form")
    String showPositionForm(Model model) {

        TraineeshipPosition position = new TraineeshipPosition();

        model.addAttribute("position", position);

        return "company/position";

    }

    @RequestMapping("/company/save_position")
    public String savePosition(@ModelAttribute("position") TraineeshipPosition position, Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        Company company = companyMapper.findByUsername(username);
        position.setCompany(company);
        company.addPosition(position);
        companyMapper.save(company);

        return "redirect:/company/dashboard";
    }


}