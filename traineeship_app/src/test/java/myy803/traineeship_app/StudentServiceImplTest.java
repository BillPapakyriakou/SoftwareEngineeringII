package myy803.traineeship_app;

import myy803.traineeship_app.domain.Student;
import myy803.traineeship_app.domain.TraineeshipPosition;
import myy803.traineeship_app.mappers.StudentMapper;
import myy803.traineeship_app.mappers.TraineeshipPositionsMapper;
import myy803.traineeship_app.service.StudentServiceImpl;
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
import org.springframework.security.authentication.TestingAuthenticationToken;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class StudentServiceImplTest {

    @InjectMocks
    private StudentServiceImpl studentService;

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
        lenient().when(authentication.getName()).thenReturn("studentUser");
    }

    @AfterEach
    void clearSecurityContext() {
        SecurityContextHolder.clearContext();
    }

    @Test
    void retrieveProfile_returnsExistingStudent_whenFound() {
        Student existing = new Student("studentUser");
        when(studentMapper.findByUsername("studentUser")).thenReturn(existing);

        Student result = studentService.retrieveProfile();

        assertSame(existing, result);
        verify(studentMapper).findByUsername("studentUser");
        verifyNoMoreInteractions(studentMapper, positionsMapper);
    }

    @Test
    void retrieveProfile_returnsNewStudent_whenNotFound() {
        SecurityContextHolder.getContext()
                .setAuthentication(new TestingAuthenticationToken("studentUser", "pw"));

        when(studentMapper.findByUsername("studentUser")).thenReturn(null);

        Student result = studentService.retrieveProfile();

        assertNotNull(result);
        verify(studentMapper).findByUsername("studentUser");
    }

    @Test
    void saveLogbook_setsLogbookAndSavesPosition() {
        Integer positionId = 10;
        String logbook = "Day 1: implemented feature X";

        TraineeshipPosition position = new TraineeshipPosition();
        position.setId(positionId);

        when(positionsMapper.findById(positionId)).thenReturn(Optional.of(position));

        studentService.saveLogbook(positionId, logbook);

        assertEquals(logbook, position.getStudentLogbook());
        verify(positionsMapper).findById(positionId);
        verify(positionsMapper).save(position);
        verifyNoMoreInteractions(positionsMapper);
        verifyNoInteractions(studentMapper);
    }

    @Test
    void saveLogbook_whenPositionMissing_throwsRuntimeException() {
        Integer positionId = 99;
        when(positionsMapper.findById(positionId)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> studentService.saveLogbook(positionId, "x"));

        verify(positionsMapper).findById(positionId);
        verifyNoMoreInteractions(positionsMapper);
        verifyNoInteractions(studentMapper);
    }

    @Test
    void saveProfile_updatesFieldsAndLookingForTraineeship_whenNoAssignedTraineeship() {
        Student existing = new Student("student1");
        existing.setAssignedTraineeship(null);
        existing.setLookingForTraineeship(false);

        Student incoming = new Student();
        incoming.setUsername("student1");
        incoming.setStudentName("New Name");
        incoming.setAM("1234");
        incoming.setAvgGrade(8.5);
        incoming.setPreferredLocation("Athens");
        incoming.setInterests("AI");
        incoming.setSkills("Java");
        incoming.setLookingForTraineeship(true);

        when(studentMapper.findByUsername("student1")).thenReturn(existing);

        studentService.saveProfile(incoming);

        assertEquals("New Name", existing.getStudentName());
        assertEquals("1234", existing.getAM());
        assertEquals(8.5, existing.getAvgGrade());
        assertEquals("Athens", existing.getPreferredLocation());
        assertEquals("AI", existing.getInterests());
        assertEquals("Java", existing.getSkills());
        assertTrue(existing.isLookingForTraineeship());

        verify(studentMapper).findByUsername("student1");
        verify(studentMapper).save(existing);
        verifyNoMoreInteractions(studentMapper);
        verifyNoInteractions(positionsMapper);
    }

    @Test
    void saveProfile_doesNotChangeLookingForTraineeship_whenAssignedTraineeshipExists() {
        Student existing = new Student("student1");
        existing.setLookingForTraineeship(false);

        TraineeshipPosition assigned = new TraineeshipPosition();
        existing.setAssignedTraineeship(assigned);

        Student incoming = new Student();
        incoming.setUsername("student1");
        incoming.setStudentName("New Name");
        incoming.setAM("1234");
        incoming.setAvgGrade(8.5);
        incoming.setPreferredLocation("Athens");
        incoming.setInterests("AI");
        incoming.setSkills("Java");
        incoming.setLookingForTraineeship(true);

        when(studentMapper.findByUsername("student1")).thenReturn(existing);

        studentService.saveProfile(incoming);

        assertEquals("New Name", existing.getStudentName());
        assertEquals("1234", existing.getAM());
        assertEquals(8.5, existing.getAvgGrade());
        assertEquals("Athens", existing.getPreferredLocation());
        assertEquals("AI", existing.getInterests());
        assertEquals("Java", existing.getSkills());
        assertFalse(existing.isLookingForTraineeship());

        verify(studentMapper).findByUsername("student1");
        verify(studentMapper).save(existing);
        verifyNoMoreInteractions(studentMapper);
        verifyNoInteractions(positionsMapper);
    }
}

