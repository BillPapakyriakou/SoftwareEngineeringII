package myy803.traineeship_app;

import myy803.traineeship_app.controllers.searchstrategies.PositionsSearchFactory;
import myy803.traineeship_app.controllers.searchstrategies.PositionsSearchStrategy;
import myy803.traineeship_app.controllers.supervisorsearchstrategies.SupervisorAssigmentFactory;
import myy803.traineeship_app.controllers.supervisorsearchstrategies.SupervisorAssignmentStrategy;
import myy803.traineeship_app.domain.Evaluation;
import myy803.traineeship_app.domain.EvaluationType;
import myy803.traineeship_app.domain.Student;
import myy803.traineeship_app.domain.TraineeshipPosition;
import myy803.traineeship_app.mappers.StudentMapper;
import myy803.traineeship_app.mappers.TraineeshipPositionsMapper;
import myy803.traineeship_app.service.CommitteeServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CommitteeServiceImplTest {

    @InjectMocks
    private CommitteeServiceImpl committeeService;

    @Mock
    private PositionsSearchFactory positionsSearchFactory;

    @Mock
    private SupervisorAssigmentFactory supervisorAssigmentFactory;

    @Mock
    private StudentMapper studentMapper;

    @Mock
    private TraineeshipPositionsMapper positionsMapper;

    @BeforeEach
    void setup() {
        // nothing required
    }

    @Test
    void listTraineeshipApplications_returnsStudentsFromMapper() {
        List<Student> expected = List.of(new Student(), new Student());
        when(studentMapper.findByLookingForTraineeshipTrue()).thenReturn(expected);

        List<Student> result = committeeService.listTraineeshipApplications();

        assertSame(expected, result);
        verify(studentMapper).findByLookingForTraineeshipTrue();
        verifyNoMoreInteractions(studentMapper, positionsMapper, positionsSearchFactory, supervisorAssigmentFactory);
    }

    @Test
    void findPositions_usesFactoryStrategyAndReturnsResults() {
        String username = "student1";
        String strategyName = "topic";

        PositionsSearchStrategy strategy = mock(PositionsSearchStrategy.class);
        List<TraineeshipPosition> expected = List.of(new TraineeshipPosition(), new TraineeshipPosition());

        when(positionsSearchFactory.create(strategyName)).thenReturn(strategy);
        when(strategy.search(username)).thenReturn(expected);

        List<TraineeshipPosition> result = committeeService.findPositions(username, strategyName);

        assertSame(expected, result);
        verify(positionsSearchFactory).create(strategyName);
        verify(strategy).search(username);
        verifyNoMoreInteractions(positionsSearchFactory);
        verifyNoInteractions(studentMapper, positionsMapper, supervisorAssigmentFactory);
    }

    @Test
    void assignPosition_setsStudentAndFlags_andSavesPosition() {
        Integer positionId = 42;
        String studentUsername = "student1";

        Student student = new Student();
        student.setUsername(studentUsername);
        student.setLookingForTraineeship(true);

        TraineeshipPosition position = new TraineeshipPosition();
        position.setId(positionId);

        when(studentMapper.findByUsername(studentUsername)).thenReturn(student);
        when(positionsMapper.findById(positionId)).thenReturn(Optional.of(position));

        committeeService.assignPosition(positionId, studentUsername);

        assertSame(student, position.getStudent());
        assertSame(position, student.getAssignedTraineeship());
        assertFalse(student.isLookingForTraineeship(), "Student should no longer be looking for traineeship");

        verify(studentMapper).findByUsername(studentUsername);
        verify(positionsMapper).findById(positionId);
        verify(positionsMapper).save(position);


        verifyNoMoreInteractions(studentMapper, positionsMapper, positionsSearchFactory, supervisorAssigmentFactory);
    }

    @Test
    void assignSupervisor_callsStrategy_marksAssigned_andSavesPosition() {
        Integer positionId = 10;
        String strategyName = "load";

        TraineeshipPosition position = new TraineeshipPosition();
        position.setId(positionId);
        position.setAssigned(false);

        SupervisorAssignmentStrategy assignmentStrategy = mock(SupervisorAssignmentStrategy.class);

        when(positionsMapper.findById(positionId)).thenReturn(Optional.of(position));
        when(supervisorAssigmentFactory.create(strategyName)).thenReturn(assignmentStrategy);

        committeeService.assignSupervisor(positionId, strategyName);

        verify(positionsMapper).findById(positionId);
        verify(supervisorAssigmentFactory).create(strategyName);
        verify(assignmentStrategy).assign(positionId);

        assertTrue(position.isAssigned(), "Position should be marked assigned after supervisor assignment");
        verify(positionsMapper).save(position);

        verifyNoMoreInteractions(studentMapper, positionsMapper, positionsSearchFactory, supervisorAssigmentFactory);
    }

    @Test
    void showAssignedPositions_returnsAssignedFromMapper() {
        List<TraineeshipPosition> expected = List.of(new TraineeshipPosition());
        when(positionsMapper.findByIsAssignedTrue()).thenReturn(expected);

        List<TraineeshipPosition> result = committeeService.showAssignedPositions();

        assertSame(expected, result);
        verify(positionsMapper).findByIsAssignedTrue();
        verifyNoMoreInteractions(positionsMapper);
        verifyNoInteractions(studentMapper, positionsSearchFactory, supervisorAssigmentFactory);
    }

    @Test
    void findById_returnsPositionWhenPresent() {
        Integer id = 1;
        TraineeshipPosition position = new TraineeshipPosition();
        when(positionsMapper.findById(id)).thenReturn(Optional.of(position));

        TraineeshipPosition result = committeeService.findById(id);

        assertSame(position, result);
        verify(positionsMapper).findById(id);
    }

    @Test
    void findById_returnsNullWhenMissing() {
        Integer id = 1;
        when(positionsMapper.findById(id)).thenReturn(Optional.empty());

        TraineeshipPosition result = committeeService.findById(id);

        assertNull(result);
        verify(positionsMapper).findById(id);
    }

    @Test
    void getCompanyEvaluations_filtersOnlyCompanyType() {
        TraineeshipPosition position = new TraineeshipPosition();

        Evaluation company1 = new Evaluation();
        company1.setEvaluationType(EvaluationType.COMPANY_EVALUATION);

        Evaluation prof1 = new Evaluation();
        prof1.setEvaluationType(EvaluationType.PROFESSOR_EVALUATION);

        Evaluation company2 = new Evaluation();
        company2.setEvaluationType(EvaluationType.COMPANY_EVALUATION);

        position.setEvaluations(List.of(company1, prof1, company2));

        List<Evaluation> result = committeeService.getCompanyEvaluations(position);

        assertEquals(2, result.size());
        assertTrue(result.contains(company1));
        assertTrue(result.contains(company2));
        assertFalse(result.contains(prof1));
    }

    @Test
    void getProfessorEvaluations_filtersOnlyProfessorType() {
        TraineeshipPosition position = new TraineeshipPosition();

        Evaluation company1 = new Evaluation();
        company1.setEvaluationType(EvaluationType.COMPANY_EVALUATION);

        Evaluation prof1 = new Evaluation();
        prof1.setEvaluationType(EvaluationType.PROFESSOR_EVALUATION);

        position.setEvaluations(List.of(company1, prof1));

        List<Evaluation> result = committeeService.getProfessorEvaluations(position);

        assertEquals(1, result.size());
        assertTrue(result.contains(prof1));
        assertFalse(result.contains(company1));
    }

    @Test
    void completeTraineeship_whenPositionMissing_doesNothing() {
        Integer positionId = 999;
        when(positionsMapper.findById(positionId)).thenReturn(Optional.empty());

        committeeService.completeTraineeship(positionId, true);

        verify(positionsMapper).findById(positionId);
        verifyNoMoreInteractions(positionsMapper);
        verifyNoInteractions(studentMapper);
    }

    @Test
    void completeTraineeship_whenStudentPresent_updatesBothAndSaves() {
        Integer positionId = 5;

        Student student = new Student();
        TraineeshipPosition position = new TraineeshipPosition();
        position.setId(positionId);
        position.setAssigned(true);
        position.setCompleted(false);
        position.setStudent(student);

        when(positionsMapper.findById(positionId)).thenReturn(Optional.of(position));

        committeeService.completeTraineeship(positionId, true);

        assertFalse(position.isAssigned());
        assertTrue(position.isCompleted());
        assertTrue(position.isPassFailGrade());
        assertNull(position.getSupervisor(), "Supervisor should be cleared");

        assertNull(student.getAssignedTraineeship(), "Student's assigned traineeship should be cleared");

        verify(studentMapper).save(student);
        verify(positionsMapper).save(position);
    }

    @Test
    void completeTraineeship_whenStudentNull_onlySavesPosition() {
        Integer positionId = 6;

        TraineeshipPosition position = new TraineeshipPosition();
        position.setId(positionId);
        position.setStudent(null);

        when(positionsMapper.findById(positionId)).thenReturn(Optional.of(position));

        committeeService.completeTraineeship(positionId, false);

        assertFalse(position.isAssigned());
        assertTrue(position.isCompleted());
        assertFalse(position.isPassFailGrade());

        verify(positionsMapper).save(position);
        verifyNoInteractions(studentMapper);
    }
}

