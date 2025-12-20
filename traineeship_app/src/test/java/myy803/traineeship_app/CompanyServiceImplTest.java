package myy803.traineeship_app;

import myy803.traineeship_app.domain.Company;
import myy803.traineeship_app.domain.Evaluation;
import myy803.traineeship_app.domain.EvaluationType;
import myy803.traineeship_app.domain.Student;
import myy803.traineeship_app.domain.TraineeshipPosition;
import myy803.traineeship_app.mappers.CompanyMapper;
import myy803.traineeship_app.mappers.StudentMapper;
import myy803.traineeship_app.mappers.TraineeshipPositionsMapper;
import myy803.traineeship_app.service.CompanyServiceImpl;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CompanyServiceImplTest {

    @InjectMocks
    private CompanyServiceImpl companyService;

    @Mock
    private CompanyMapper companyMapper;

    @Mock
    private StudentMapper studentMapper;

    @Mock
    private TraineeshipPositionsMapper positionsMapper;

    @Mock
    private SecurityContext securityContext;

    @Mock
    private Authentication authentication;

    @BeforeEach
    void setupSecurityContext() {
        SecurityContextHolder.setContext(securityContext);
        lenient().when(securityContext.getAuthentication()).thenReturn(authentication);
        lenient().when(authentication.getName()).thenReturn("companyUser");
    }

    @AfterEach
    void clearSecurityContext() {
        SecurityContextHolder.clearContext();
    }

    @Test
    void saveProfile_savesCompany() {
        Company company = new Company();
        company.setUsername("companyUser");

        companyService.saveProfile(company);

        verify(companyMapper).save(company);
        verifyNoMoreInteractions(companyMapper, studentMapper, positionsMapper);
    }

    @Test
    void retrieveProfile_returnsExistingCompany_whenFound() {
        Company existing = new Company("companyUser");
        when(companyMapper.findByUsername("companyUser")).thenReturn(existing);

        Company result = companyService.retrieveProfile();

        assertSame(existing, result);
        verify(companyMapper).findByUsername("companyUser");
        verifyNoMoreInteractions(companyMapper, studentMapper, positionsMapper);
    }

    @Test
    void retrieveProfile_returnsNewCompany_whenNotFound() {
        when(companyMapper.findByUsername("companyUser")).thenReturn(null);

        Company result = companyService.retrieveProfile();

        assertNotNull(result);
        assertEquals("companyUser", result.getUsername());
        verify(companyMapper).findByUsername("companyUser");
        verifyNoMoreInteractions(companyMapper, studentMapper, positionsMapper);
    }

    @Test
    void listAvailablePositions_returnsCompanyAvailablePositions() {
        Company company = new Company("companyUser");
        company.setPositions(new ArrayList<>());

        TraineeshipPosition available = new TraineeshipPosition();
        available.setAssigned(false);
        available.setCompleted(false);

        TraineeshipPosition assigned = new TraineeshipPosition();
        assigned.setAssigned(true);
        assigned.setCompleted(false);

        TraineeshipPosition completed = new TraineeshipPosition();
        completed.setAssigned(false);
        completed.setCompleted(true);

        company.getPositions().add(available);
        company.getPositions().add(assigned);
        company.getPositions().add(completed);

        when(companyMapper.findByUsername("companyUser")).thenReturn(company);

        List<TraineeshipPosition> result = companyService.listAvailablePositions();

        assertEquals(1, result.size());
        assertSame(available, result.get(0));

        verify(companyMapper).findByUsername("companyUser");
        verifyNoMoreInteractions(companyMapper, studentMapper, positionsMapper);
    }

    @Test
    void showPositionForm_returnsNewPosition() {
        TraineeshipPosition p1 = companyService.showPositionForm();
        TraineeshipPosition p2 = companyService.showPositionForm();

        assertNotNull(p1);
        assertNotNull(p2);
        assertNotSame(p1, p2);

        verifyNoInteractions(companyMapper, studentMapper, positionsMapper);
    }

    @Test
    void savePosition_setsCompany_addsPosition_andSavesCompany_whenCompanyExists() {
        Company company = new Company("companyUser");
        company.setPositions(new ArrayList<>());
        when(companyMapper.findByUsername("companyUser")).thenReturn(company);

        TraineeshipPosition position = new TraineeshipPosition();

        companyService.savePosition(position);

        assertSame(company, position.getCompany());
        assertTrue(company.getPositions().contains(position));

        verify(companyMapper).findByUsername("companyUser");
        verify(companyMapper).save(company);
        verifyNoMoreInteractions(companyMapper, studentMapper, positionsMapper);
    }

    @Test
    void retrieveAssignedPositions_returnsAssignedFromCompany() {
        Company company = new Company("companyUser");
        company.setPositions(new ArrayList<>());

        TraineeshipPosition p1 = new TraineeshipPosition();
        p1.setAssigned(true);

        TraineeshipPosition p2 = new TraineeshipPosition();
        p2.setAssigned(false);

        company.getPositions().add(p1);
        company.getPositions().add(p2);

        when(companyMapper.findByUsername("companyUser")).thenReturn(company);

        List<TraineeshipPosition> result = companyService.retrieveAssignedPositions();

        assertEquals(1, result.size());
        assertSame(p1, result.get(0));

        verify(companyMapper).findByUsername("companyUser");
        verifyNoMoreInteractions(companyMapper, studentMapper, positionsMapper);
    }

    @Test
    void deletePosition_whenStudentPresent_clearsStudentAndDeletesPosition() {
        Integer positionId = 10;

        Student student = new Student();
        TraineeshipPosition position = new TraineeshipPosition();
        position.setId(positionId);
        position.setStudent(student);

        when(positionsMapper.findById(positionId)).thenReturn(Optional.of(position));
        when(positionsMapper.existsById(positionId)).thenReturn(true);

        companyService.deletePosition(positionId);

        assertNull(student.getAssignedTraineeship());

        verify(studentMapper).save(student);
        verify(positionsMapper).findById(positionId);
        verify(positionsMapper).existsById(positionId);
        verify(positionsMapper).deleteById(positionId);
        verifyNoMoreInteractions(companyMapper, studentMapper, positionsMapper);
    }

    @Test
    void deletePosition_whenStudentNull_deletesPositionOnly() {
        Integer positionId = 11;

        TraineeshipPosition position = new TraineeshipPosition();
        position.setId(positionId);
        position.setStudent(null);

        when(positionsMapper.findById(positionId)).thenReturn(Optional.of(position));
        when(positionsMapper.existsById(positionId)).thenReturn(true);

        companyService.deletePosition(positionId);

        verify(positionsMapper).findById(positionId);
        verify(positionsMapper).existsById(positionId);
        verify(positionsMapper).deleteById(positionId);
        verifyNoInteractions(studentMapper);
    }

    @Test
    void deletePosition_whenNotExists_throwsIllegalArgumentException() {
        Integer positionId = 12;

        TraineeshipPosition position = new TraineeshipPosition();
        position.setId(positionId);

        when(positionsMapper.findById(positionId)).thenReturn(Optional.of(position));
        when(positionsMapper.existsById(positionId)).thenReturn(false);

        assertThrows(IllegalArgumentException.class, () -> companyService.deletePosition(positionId));

        verify(positionsMapper).findById(positionId);
        verify(positionsMapper).existsById(positionId);
        verify(positionsMapper, never()).deleteById(anyInt());
        verifyNoInteractions(studentMapper, companyMapper);
    }

    @Test
    void saveEvaluation_whenPositionMissing_throwsIllegalArgumentException() {
        Integer positionId = 99;
        when(positionsMapper.findById(positionId)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class,
                () -> companyService.saveEvaluation(new Evaluation(), positionId));

        verify(positionsMapper).findById(positionId);
        verifyNoMoreInteractions(positionsMapper);
        verifyNoInteractions(companyMapper, studentMapper);
    }

    @Test
    void saveEvaluation_whenPositionNotAssigned_throwsIllegalStateException() {
        Integer positionId = 20;

        TraineeshipPosition position = new TraineeshipPosition();
        position.setId(positionId);
        position.setAssigned(false);
        position.setEvaluations(new ArrayList<>());

        when(positionsMapper.findById(positionId)).thenReturn(Optional.of(position));

        assertThrows(IllegalStateException.class,
                () -> companyService.saveEvaluation(new Evaluation(), positionId));

        verify(positionsMapper).findById(positionId);
        verify(positionsMapper, never()).save(any());
        verifyNoInteractions(companyMapper, studentMapper);
    }

    @Test
    void saveEvaluation_setsCompanyType_addsEvaluation_andSavesPosition() {
        Integer positionId = 21;

        TraineeshipPosition position = new TraineeshipPosition();
        position.setId(positionId);
        position.setAssigned(true);
        position.setEvaluations(new ArrayList<>());

        when(positionsMapper.findById(positionId)).thenReturn(Optional.of(position));

        Evaluation evaluation = new Evaluation();

        companyService.saveEvaluation(evaluation, positionId);

        assertEquals(EvaluationType.COMPANY_EVALUATION, evaluation.getEvaluationType());
        assertEquals(1, position.getEvaluations().size());
        assertSame(evaluation, position.getEvaluations().get(0));

        verify(positionsMapper).findById(positionId);
        verify(positionsMapper).save(position);
        verifyNoMoreInteractions(positionsMapper);
        verifyNoInteractions(companyMapper, studentMapper);
    }
}
