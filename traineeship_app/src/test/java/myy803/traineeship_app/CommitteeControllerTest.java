package myy803.traineeship_app;

import myy803.traineeship_app.controllers.CommitteeController;
import myy803.traineeship_app.domain.Evaluation;
import myy803.traineeship_app.domain.Student;
import myy803.traineeship_app.domain.TraineeshipPosition;
import myy803.traineeship_app.service.CommitteeService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CommitteeController.class)
@WithMockUser(username = "committeeUser", authorities = "COMMITTEE")
class CommitteeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CommitteeService committeeService;

    @Test
    void dashboard_returnsDashboardView() throws Exception {
        mockMvc.perform(get("/committee/dashboard"))
                .andExpect(status().isOk())
                .andExpect(view().name("committee/dashboard"));
    }

    @Test
    void listTraineeshipApplications_addsApplicationsToModel() throws Exception {
        Student s1 = new Student();
        Student s2 = new Student();
        when(committeeService.listTraineeshipApplications()).thenReturn(List.of(s1, s2));

        mockMvc.perform(get("/committee/list_traineeship_applications"))
                .andExpect(status().isOk())
                .andExpect(view().name("committee/traineeship_applications"))
                .andExpect(model().attribute("traineeship_applications", List.of(s1, s2)));

        verify(committeeService).listTraineeshipApplications();
    }

    @Test
    void findPositions_addsPositionsAndStudentUsername() throws Exception {
        String studentUsername = "student1";
        String strategy = "topic";
        TraineeshipPosition p1 = new TraineeshipPosition();
        TraineeshipPosition p2 = new TraineeshipPosition();

        when(committeeService.findPositions(studentUsername, strategy)).thenReturn(List.of(p1, p2));

        mockMvc.perform(get("/committee/find_positions")
                        .param("selected_student_id", studentUsername)
                        .param("strategy", strategy))
                .andExpect(status().isOk())
                .andExpect(view().name("committee/available_positions"))
                .andExpect(model().attribute("positions", List.of(p1, p2)))
                .andExpect(model().attribute("student_username", studentUsername));

        verify(committeeService).findPositions(studentUsername, strategy);
    }

    @Test
    void assignPosition_callsService_setsPositionId_andReturnsSupervisorAssignmentView() throws Exception {
        Integer positionId = 42;
        String applicantUsername = "student1";

        mockMvc.perform(get("/committee/assign_position")
                        .param("selected_position_id", String.valueOf(positionId))
                        .param("applicant_username", applicantUsername))
                .andExpect(status().isOk())
                .andExpect(view().name("committee/supervisor_assignment"))
                .andExpect(model().attribute("position_id", positionId));

        verify(committeeService).assignPosition(positionId, applicantUsername);
    }

    @Test
    void assignSupervisor_success_setsFlagsAndReturnsSupervisorAssignmentView() throws Exception {
        Integer positionId = 10;
        String strategy = "load";

        mockMvc.perform(get("/committee/assign_supervisor")
                        .param("selected_position_id", String.valueOf(positionId))
                        .param("strategy", strategy))
                .andExpect(status().isOk())
                .andExpect(view().name("committee/supervisor_assignment"))
                .andExpect(model().attribute("supervisorAssigned", true))
                .andExpect(model().attribute("supervisorError", (Object) null))
                .andExpect(model().attribute("position_id", positionId));

        verify(committeeService).assignSupervisor(positionId, strategy);
    }

    @Test
    void assignSupervisor_failure_setsErrorMessageAndReturnsSupervisorAssignmentView() throws Exception {
        Integer positionId = 10;
        String strategy = "load";

        doThrow(new RuntimeException("no match"))
                .when(committeeService).assignSupervisor(positionId, strategy);

        mockMvc.perform(get("/committee/assign_supervisor")
                        .param("selected_position_id", String.valueOf(positionId))
                        .param("strategy", strategy))
                .andExpect(status().isOk())
                .andExpect(view().name("committee/supervisor_assignment"))
                .andExpect(model().attribute("supervisorAssigned", false))
                .andExpect(model().attribute("supervisorError", "No professor matches this strategy."))
                .andExpect(model().attribute("position_id", positionId));

        verify(committeeService).assignSupervisor(positionId, strategy);
    }

    @Test
    void listAssignedPositions_addsPositionsToModel() throws Exception {
        TraineeshipPosition p1 = new TraineeshipPosition();
        TraineeshipPosition p2 = new TraineeshipPosition();

        when(committeeService.showAssignedPositions()).thenReturn(List.of(p1, p2));

        mockMvc.perform(get("/committee/list_assigned_traineeships"))
                .andExpect(status().isOk())
                .andExpect(view().name("committee/assigned_positions"))
                .andExpect(model().attribute("positions", List.of(p1, p2)));

        verify(committeeService).showAssignedPositions();
    }

    @Test
    void monitorPosition_addsPositionAndEvaluationsToModel() throws Exception {
        Integer positionId = 7;

        TraineeshipPosition position = new TraineeshipPosition();
        List<Evaluation> companyEvals = List.of(new Evaluation(), new Evaluation());
        List<Evaluation> professorEvals = List.of(new Evaluation());

        when(committeeService.findById(positionId)).thenReturn(position);
        when(committeeService.getCompanyEvaluations(position)).thenReturn(companyEvals);
        when(committeeService.getProfessorEvaluations(position)).thenReturn(professorEvals);

        mockMvc.perform(get("/committee/monitor_position")
                        .param("positionId", String.valueOf(positionId)))
                .andExpect(status().isOk())
                .andExpect(view().name("committee/monitor_view"))
                .andExpect(model().attribute("position", position))
                .andExpect(model().attribute("companyEvaluations", companyEvals))
                .andExpect(model().attribute("professorEvaluations", professorEvals));

        verify(committeeService).findById(positionId);
        verify(committeeService).getCompanyEvaluations(position);
        verify(committeeService).getProfessorEvaluations(position);
    }

    @Test
    void pass_callsCompleteTraineeshipTrue_andRedirects() throws Exception {
        Integer positionId = 5;

        mockMvc.perform(post("/committee/pass/{positionId}", positionId).with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/committee/list_assigned_traineeships"));

        verify(committeeService).completeTraineeship(positionId, true);
    }

    @Test
    void fail_callsCompleteTraineeshipFalse_andRedirects() throws Exception {
        Integer positionId = 5;

        mockMvc.perform(post("/committee/fail/{positionId}", positionId).with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/committee/list_assigned_traineeships"));

        verify(committeeService).completeTraineeship(positionId, false);
    }
}
