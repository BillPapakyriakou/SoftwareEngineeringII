package myy803.traineeship_app;

import myy803.traineeship_app.domain.Evaluation;
import myy803.traineeship_app.domain.EvaluationType;
import myy803.traineeship_app.domain.Professor;
import myy803.traineeship_app.domain.TraineeshipPosition;
import myy803.traineeship_app.mappers.ProfessorMapper;
import myy803.traineeship_app.mappers.TraineeshipPositionsMapper;
import myy803.traineeship_app.service.ProfessorServiceImpl;
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
class ProfessorServiceImplTest {

    @InjectMocks
    private ProfessorServiceImpl professorService;

    @Mock
    private ProfessorMapper professorMapper;

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
        lenient().when(authentication.getName()).thenReturn("profUser");
    }

    @AfterEach
    void clearSecurityContext() {
        SecurityContextHolder.clearContext();
    }

    @Test
    void saveProfile_savesProfessor() {
        Professor professor = new Professor();
        professor.setUsername("profUser");

        professorService.saveProfile(professor);

        verify(professorMapper).save(professor);
        verifyNoMoreInteractions(professorMapper, positionsMapper);
    }

    @Test
    void retrieveProfile_returnsExistingProfessor_whenFound() {
        Professor existing = new Professor("profUser");
        when(professorMapper.findByUsername("profUser")).thenReturn(existing);

        Professor result = professorService.retrieveProfile();

        assertSame(existing, result);
        verify(professorMapper).findByUsername("profUser");
        verifyNoMoreInteractions(professorMapper, positionsMapper);
    }

    @Test
    void retrieveProfile_returnsNewProfessor_whenNotFound() {
        when(professorMapper.findByUsername("profUser")).thenReturn(null);

        Professor result = professorService.retrieveProfile();

        assertNotNull(result);
        assertEquals("profUser", result.getUsername());
        verify(professorMapper).findByUsername("profUser");
        verifyNoMoreInteractions(professorMapper, positionsMapper);
    }

    @Test
    void retrieveSupervisedPositions_returnsPositionsFromMapper() {
        List<TraineeshipPosition> expected = List.of(new TraineeshipPosition(), new TraineeshipPosition());
        when(positionsMapper.findBySupervisorUsername("profUser")).thenReturn(expected);

        List<TraineeshipPosition> result = professorService.retrieveSupervisedPositions();

        assertSame(expected, result);
        verify(positionsMapper).findBySupervisorUsername("profUser");
        verifyNoMoreInteractions(positionsMapper);
        verifyNoInteractions(professorMapper);
    }

    @Test
    void saveEvaluation_whenPositionMissing_throwsIllegalArgumentException() {
        Integer positionId = 99;
        when(positionsMapper.findById(positionId)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class,
                () -> professorService.saveEvaluation(new Evaluation(), positionId));

        verify(positionsMapper).findById(positionId);
        verifyNoMoreInteractions(positionsMapper);
        verifyNoInteractions(professorMapper);
    }

    @Test
    void saveEvaluation_whenPositionNotAssigned_throwsIllegalStateException() {
        Integer positionId = 10;

        TraineeshipPosition position = new TraineeshipPosition();
        position.setId(positionId);
        position.setAssigned(false);
        position.setEvaluations(new ArrayList<>());

        when(positionsMapper.findById(positionId)).thenReturn(Optional.of(position));

        assertThrows(IllegalStateException.class,
                () -> professorService.saveEvaluation(new Evaluation(), positionId));

        verify(positionsMapper).findById(positionId);
        verify(positionsMapper, never()).save(any());
        verifyNoInteractions(professorMapper);
    }

    @Test
    void saveEvaluation_setsProfessorType_addsEvaluation_andSavesPosition() {
        Integer positionId = 11;

        TraineeshipPosition position = new TraineeshipPosition();
        position.setId(positionId);
        position.setAssigned(true);
        position.setEvaluations(new ArrayList<>());

        when(positionsMapper.findById(positionId)).thenReturn(Optional.of(position));

        Evaluation evaluation = new Evaluation();

        professorService.saveEvaluation(evaluation, positionId);

        assertEquals(EvaluationType.PROFESSOR_EVALUATION, evaluation.getEvaluationType());
        assertEquals(1, position.getEvaluations().size());
        assertSame(evaluation, position.getEvaluations().get(0));

        verify(positionsMapper).findById(positionId);
        verify(positionsMapper).save(position);
        verifyNoMoreInteractions(positionsMapper);
        verifyNoInteractions(professorMapper);
    }
}

