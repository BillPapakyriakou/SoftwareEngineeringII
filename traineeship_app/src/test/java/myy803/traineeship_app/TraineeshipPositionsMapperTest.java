package myy803.traineeship_app;

import myy803.traineeship_app.domain.Company;
import myy803.traineeship_app.domain.Professor;
import myy803.traineeship_app.domain.TraineeshipPosition;
import myy803.traineeship_app.mappers.TraineeshipPositionsMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.TestPropertySource;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@TestPropertySource(locations = "classpath:application.properties")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class TraineeshipPositionsMapperTest {

    @Autowired
    private TraineeshipPositionsMapper positionsMapper;

    @Autowired
    private TestEntityManager em;

    private Company persistCompany(String location) {
        Company c = new Company();
        c.setUsername("company_" + UUID.randomUUID());
        c.setCompanyName("Test Company");
        c.setCompanyLocation(location);
        em.persist(c);
        return c;
    }

    private Professor persistProfessor(String username) {
        Professor p = new Professor();
        p.setUsername(username);
        p.setProfessorName("Dr Test");
        p.setInterests("AI");
        em.persist(p);
        return p;
    }

    private TraineeshipPosition position(Company company, String topics, boolean assigned, boolean completed) {
        TraineeshipPosition p = new TraineeshipPosition();
        p.setTitle("Intern " + UUID.randomUUID());
        p.setTopics(topics);
        p.setAssigned(assigned);
        p.setCompleted(completed);
        p.setSkills("Java");
        p.setCompany(company);
        p.setEvaluations(new ArrayList<>());
        return p;
    }

    @Test
    void findByTopicsContaining_returnsMatchingPositions() {
        String token = "topic_" + UUID.randomUUID();
        Company c = persistCompany("LOC_" + UUID.randomUUID());

        TraineeshipPosition p1 = position(c, "java," + token + ",spring", false, false);
        TraineeshipPosition p2 = position(c, "other", false, false);

        positionsMapper.saveAll(List.of(p1, p2));
        positionsMapper.flush();

        List<TraineeshipPosition> result = positionsMapper.findByTopicsContaining(token);

        assertThat(result).isNotEmpty();
        assertThat(result).allMatch(p -> p.getTopics() != null && p.getTopics().contains(token));
    }

    @Test
    void findByTopicsContainingAndIsAssignedFalse_returnsOnlyUnassigned() {
        String token = "topic_" + UUID.randomUUID();
        Company c = persistCompany("LOC_" + UUID.randomUUID());

        TraineeshipPosition unassigned = position(c, token, false, false);
        TraineeshipPosition assigned = position(c, token, true, false);

        positionsMapper.saveAll(List.of(unassigned, assigned));
        positionsMapper.flush();

        List<TraineeshipPosition> result = positionsMapper.findByTopicsContainingAndIsAssignedFalse(token);

        assertThat(result).isNotEmpty();
        assertThat(result).allMatch(p -> p.getTopics() != null && p.getTopics().contains(token) && !p.isAssigned());
    }

    @Test
    void findByTopicsContainingAndIsAssignedFalseAndIsCompletedFalse_returnsOnlyActivePositions() {
        String token = "topic_" + UUID.randomUUID();
        Company c = persistCompany("LOC_" + UUID.randomUUID());

        TraineeshipPosition active = position(c, token, false, false);
        TraineeshipPosition completed = position(c, token, false, true);
        TraineeshipPosition assigned = position(c, token, true, false);

        positionsMapper.saveAll(List.of(active, completed, assigned));
        positionsMapper.flush();

        List<TraineeshipPosition> result =
                positionsMapper.findByTopicsContainingAndIsAssignedFalseAndIsCompletedFalse(token);

        assertThat(result).isNotEmpty();
        assertThat(result).allMatch(p ->
                p.getTopics() != null && p.getTopics().contains(token) && !p.isAssigned() && !p.isCompleted()
        );
    }

    @Test
    void findBySupervisorUsername_returnsPositionsForSupervisor() {
        String profUsername = "prof_" + UUID.randomUUID();
        Company c = persistCompany("LOC_" + UUID.randomUUID());
        Professor prof = persistProfessor(profUsername);

        TraineeshipPosition p1 = position(c, "ai", true, false);
        p1.setSupervisor(prof);

        positionsMapper.save(p1);
        positionsMapper.flush();

        List<TraineeshipPosition> result = positionsMapper.findBySupervisorUsername(profUsername);

        assertThat(result).isNotEmpty();
        assertThat(result).allMatch(p ->
                p.getSupervisor() != null && profUsername.equals(p.getSupervisor().getUsername())
        );
    }

    @Test
    void findByIsAssignedTrue_returnsOnlyAssignedPositions() {
        Company c = persistCompany("LOC_" + UUID.randomUUID());

        TraineeshipPosition assigned = position(c, "cloud", true, false);
        TraineeshipPosition unassigned = position(c, "cloud", false, false);

        positionsMapper.saveAll(List.of(assigned, unassigned));
        positionsMapper.flush();

        List<TraineeshipPosition> result = positionsMapper.findByIsAssignedTrue();

        assertThat(result).isNotEmpty();
        assertThat(result).allMatch(TraineeshipPosition::isAssigned);
    }
}
