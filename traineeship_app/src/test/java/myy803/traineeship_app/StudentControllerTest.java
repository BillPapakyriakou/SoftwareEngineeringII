package myy803.traineeship_app;

import myy803.traineeship_app.controllers.StudentController;
import myy803.traineeship_app.domain.Company;
import myy803.traineeship_app.domain.Student;
import myy803.traineeship_app.domain.TraineeshipPosition;
import myy803.traineeship_app.service.StudentService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(StudentController.class)
@WithMockUser(username = "studentUser", authorities = "STUDENT")
class StudentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private StudentService studentService;


    private TraineeshipPosition safePosition(int id) {
        Company company = new Company();
        company.setCompanyName("ACME");

        TraineeshipPosition p = new TraineeshipPosition();
        p.setId(id);
        p.setTitle("Intern " + id);
        p.setCompany(company);
        p.setStudentLogbook("Existing logbook text");
        return p;
    }

    @Test
    void dashboard_returnsDashboardView() throws Exception {
        mockMvc.perform(get("/student/dashboard"))
                .andExpect(status().isOk())
                .andExpect(view().name("student/dashboard"));
    }

    @Test
    void retrieveStudentProfile_addsStudentToModel_andReturnsProfileView() throws Exception {
        Student student = new Student();
        when(studentService.retrieveProfile()).thenReturn(student);

        mockMvc.perform(get("/student/profile"))
                .andExpect(status().isOk())
                .andExpect(view().name("student/profile"))
                .andExpect(model().attribute("student", student));

        verify(studentService).retrieveProfile();
    }

    @Test
    void saveProfile_callsService_andReturnsDashboardView() throws Exception {
        mockMvc.perform(get("/student/save_profile")
                        .param("username", "studentUser")
                        .param("studentName", "John Student")
                        .param("AM", "12345"))
                .andExpect(status().isOk())
                .andExpect(view().name("student/dashboard"));

        verify(studentService).saveProfile(any(Student.class));
    }

    @Test
    void viewLogbook_whenNotAssigned_setsError_andReturnsDashboard() throws Exception {
        Student student = new Student();
        student.setAssignedTraineeship(null);

        when(studentService.retrieveProfile()).thenReturn(student);

        mockMvc.perform(get("/student/logbook"))
                .andExpect(status().isOk())
                .andExpect(view().name("student/dashboard"))
                .andExpect(model().attribute("error", "Not assigned position"))
                .andExpect(model().attribute("student", student));

        verify(studentService).retrieveProfile();
        verifyNoMoreInteractions(studentService);
    }

    @Test
    void viewLogbook_whenAssigned_addsPosition_andReturnsLogbookForm() throws Exception {
        Student student = new Student();
        TraineeshipPosition position = safePosition(7);
        student.setAssignedTraineeship(position);

        when(studentService.retrieveProfile()).thenReturn(student);

        mockMvc.perform(get("/student/logbook"))
                .andExpect(status().isOk())
                .andExpect(view().name("student/logbook_form"))
                .andExpect(model().attribute("traineeshipPosition", position));

        verify(studentService).retrieveProfile();
    }

    @Test
    void fillLogbook_success_setsSuccessMessage_andReturnsDashboard() throws Exception {
        Student student = new Student();
        when(studentService.retrieveProfile()).thenReturn(student);

        mockMvc.perform(post("/student/logbook")
                        .with(csrf())
                        .param("positionId", "7")
                        .param("newLogbook", "Did work today"))
                .andExpect(status().isOk())
                .andExpect(view().name("student/dashboard"))
                .andExpect(model().attribute("successMessage", "Logbook saved"))
                .andExpect(model().attribute("student", student));

        verify(studentService).retrieveProfile();
        verify(studentService).saveLogbook(7, "Did work today");
    }

    @Test
    void fillLogbook_failure_setsErrorMessage_andReturnsDashboard() throws Exception {
        Student student = new Student();
        when(studentService.retrieveProfile()).thenReturn(student);

        doThrow(new RuntimeException("DB down"))
                .when(studentService).saveLogbook(eq(7), anyString());

        mockMvc.perform(post("/student/logbook")
                        .with(csrf())
                        .param("positionId", "7")
                        .param("newLogbook", "Did work today"))
                .andExpect(status().isOk())
                .andExpect(view().name("student/dashboard"))
                .andExpect(model().attributeExists("errorMessage"))
                .andExpect(model().attribute("student", student));

        verify(studentService).retrieveProfile();
        verify(studentService).saveLogbook(7, "Did work today");
    }


}

