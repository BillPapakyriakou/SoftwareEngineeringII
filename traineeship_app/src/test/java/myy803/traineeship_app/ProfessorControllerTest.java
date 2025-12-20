package myy803.traineeship_app;

import myy803.traineeship_app.controllers.ProfessorController;
import myy803.traineeship_app.domain.*;
import myy803.traineeship_app.service.ProfessorService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ProfessorController.class)
@WithMockUser(username = "profUser", authorities = "PROFESSOR")
class ProfessorControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProfessorService professorService;


    private TraineeshipPosition pos(int id) {
        Company c = new Company();
        c.setCompanyName("ACME");

        TraineeshipPosition p = new TraineeshipPosition();
        p.setId(id);
        p.setTitle("Intern " + id);
        p.setCompany(c);
        p.setSkills("Java");
        p.setEvaluations(new ArrayList<>());
        return p;
    }

    @Test
    void dashboard_returnsDashboardView() throws Exception {
        mockMvc.perform(get("/professor/dashboard"))
                .andExpect(status().isOk())
                .andExpect(view().name("professor/dashboard"));
    }

    @Test
    void retrieveProfessorProfile_addsProfessorToModel_andReturnsProfileView() throws Exception {
        Professor professor = new Professor();
        when(professorService.retrieveProfile()).thenReturn(professor);

        mockMvc.perform(get("/professor/profile"))
                .andExpect(status().isOk())
                .andExpect(view().name("professor/profile"))
                .andExpect(model().attribute("professor", professor));

        verify(professorService).retrieveProfile();
    }

    @Test
    void saveProfile_callsService_andReturnsDashboardView() throws Exception {
        mockMvc.perform(get("/professor/save_profile")
                        .param("professorName", "Dr Smith")
                        .param("username", "profUser"))
                .andExpect(status().isOk())
                .andExpect(view().name("professor/dashboard"));

        verify(professorService).saveProfile(any(Professor.class));
    }

    @Test
    void listSupervisedPositions_addsPositionsToModel_andReturnsView() throws Exception {
        List<TraineeshipPosition> positions = List.of(pos(1), pos(2));
        when(professorService.retrieveSupervisedPositions()).thenReturn(positions);

        mockMvc.perform(get("/professor/list_supervised_positions"))
                .andExpect(status().isOk())
                .andExpect(view().name("professor/supervised_positions"))
                .andExpect(model().attribute("positions", positions));

        verify(professorService).retrieveSupervisedPositions();
    }

    @Test
    void viewEvaluationForm_setsPositionIdAndEmptyEvaluation_andReturnsFormView() throws Exception {
        mockMvc.perform(get("/professor/view_evaluation_form")
                        .param("positionId", "7"))
                .andExpect(status().isOk())
                .andExpect(view().name("professor/evaluation_form"))
                .andExpect(model().attribute("positionId", 7))
                .andExpect(model().attributeExists("evaluation"));

        verifyNoInteractions(professorService);
    }

    @Test
    void saveEvaluation_callsService_addsPositionsAndSuccessMessage_andReturnsSupervisedPositionsView() throws Exception {
        List<TraineeshipPosition> supervised = List.of(pos(1));
        when(professorService.retrieveSupervisedPositions()).thenReturn(supervised);

        Evaluation evaluation = new Evaluation();

        mockMvc.perform(get("/professor/save_evaluation")
                        .param("positionId", "9")
                        .flashAttr("evaluation", evaluation))
                .andExpect(status().isOk())
                .andExpect(view().name("professor/supervised_positions"))
                .andExpect(model().attribute("positions", supervised))
                .andExpect(model().attribute("successMessage", "Evaluation submitted successfully!"));

        verify(professorService).saveEvaluation(any(Evaluation.class), eq(9));
        verify(professorService).retrieveSupervisedPositions();
    }

}

