package myy803.traineeship_app;

import myy803.traineeship_app.controllers.CompanyController;
import myy803.traineeship_app.domain.Company;
import myy803.traineeship_app.domain.Evaluation;
import myy803.traineeship_app.domain.Student;
import myy803.traineeship_app.domain.TraineeshipPosition;
import myy803.traineeship_app.service.CompanyService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CompanyController.class)
@WithMockUser(username = "companyUser", authorities = "COMPANY")
class CompanyControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CompanyService companyService;

    @Test
    void dashboard_returnsDashboardView() throws Exception {
        mockMvc.perform(get("/company/dashboard"))
                .andExpect(status().isOk())
                .andExpect(view().name("company/dashboard"));
    }

    @Test
    void retrieveCompanyProfile_addsCompanyToModel_andReturnsProfileView() throws Exception {
        Company company = new Company();
        when(companyService.retrieveProfile()).thenReturn(company);

        mockMvc.perform(get("/company/profile"))
                .andExpect(status().isOk())
                .andExpect(view().name("company/profile"))
                .andExpect(model().attribute("company", company));

        verify(companyService).retrieveProfile();
    }

    @Test
    void saveProfile_callsService_andReturnsDashboardView() throws Exception {
        mockMvc.perform(get("/company/save_profile")
                        .param("companyName", "ACME")
                        .param("username", "companyUser"))
                .andExpect(status().isOk())
                .andExpect(view().name("company/dashboard"));


        verify(companyService).saveProfile(any(Company.class));
    }

    @Test
    void listAvailablePositions_addsPositionsToModel_andReturnsView() throws Exception {
        List<TraineeshipPosition> positions = List.of(new TraineeshipPosition(), new TraineeshipPosition());
        when(companyService.listAvailablePositions()).thenReturn(positions);

        mockMvc.perform(get("/company/list_available_positions"))
                .andExpect(status().isOk())
                .andExpect(view().name("company/available_positions"))
                .andExpect(model().attribute("positions", positions));

        verify(companyService).listAvailablePositions();
    }

    @Test
    void showPositionForm_addsPositionToModel_andReturnsPositionView() throws Exception {
        TraineeshipPosition position = new TraineeshipPosition();
        when(companyService.showPositionForm()).thenReturn(position);

        mockMvc.perform(get("/company/show_position_form"))
                .andExpect(status().isOk())
                .andExpect(view().name("company/position"))
                .andExpect(model().attribute("position", position));

        verify(companyService).showPositionForm();
    }

    @Test
    void savePosition_callsService_andRedirectsToDashboard() throws Exception {
        mockMvc.perform(get("/company/save_position")
                        .param("title", "Java Intern")
                        .param("topics", "java,spring"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/company/dashboard"));

        verify(companyService).savePosition(any(TraineeshipPosition.class));
    }

    @Test
    void listAssignedPositions_addsPositionsToModel_andReturnsView() throws Exception {
        Student s = new Student();
        s.setUsername("student1");

        TraineeshipPosition p = new TraineeshipPosition();
        p.setId(1);
        p.setStudent(s);
        p.setEvaluations(new java.util.ArrayList<>());

        List<TraineeshipPosition> assigned = List.of(p);
        when(companyService.retrieveAssignedPositions()).thenReturn(assigned);

        mockMvc.perform(get("/company/list_assigned_positions"))
                .andExpect(status().isOk())
                .andExpect(view().name("company/assigned_positions"))
                .andExpect(model().attribute("positions", assigned));

        verify(companyService).retrieveAssignedPositions();
    }

    @Test
    void deletePosition_callsService_andRedirectsToAvailablePositions() throws Exception {
        mockMvc.perform(get("/company/delete_position")
                        .param("positionId", "12"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/company/list_available_positions"));

        verify(companyService).deletePosition(12);
    }

    @Test
    void viewEvaluationForm_setsPositionIdAndEmptyEvaluation_andReturnsFormView() throws Exception {
        mockMvc.perform(get("/company/view_evaluation_form")
                        .param("positionId", "7"))
                .andExpect(status().isOk())
                .andExpect(view().name("company/evaluation_form"))
                .andExpect(model().attribute("positionId", 7))
                .andExpect(model().attributeExists("evaluation"));

        verifyNoInteractions(companyService);
    }

    @Test
    void saveEvaluation_callsService_addsPositionsAndSuccessMessage_andReturnsAssignedPositionsView() throws Exception {

        Student s1 = new Student();
        s1.setUsername("student1");

        TraineeshipPosition p1 = new TraineeshipPosition();
        p1.setId(1);
        p1.setStudent(s1);
        p1.setEvaluations(new java.util.ArrayList<>());

        Student s2 = new Student();
        s2.setUsername("student2");

        TraineeshipPosition p2 = new TraineeshipPosition();
        p2.setId(2);
        p2.setStudent(s2);
        p2.setEvaluations(new java.util.ArrayList<>());

        List<TraineeshipPosition> assigned = List.of(p1, p2);
        when(companyService.retrieveAssignedPositions()).thenReturn(assigned);

        Evaluation evaluation = new Evaluation();

        mockMvc.perform(get("/company/save_evaluation")
                        .param("positionId", "9")
                        .flashAttr("evaluation", evaluation))
                .andExpect(status().isOk())
                .andExpect(view().name("company/assigned_positions"))
                .andExpect(model().attribute("positions", assigned))
                .andExpect(model().attribute("successMessage", "Evaluation submitted successfully!"));

        verify(companyService).saveEvaluation(any(Evaluation.class), eq(9));
        verify(companyService).retrieveAssignedPositions();
    }


}

