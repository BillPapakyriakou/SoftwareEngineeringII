package myy803.traineeship_app;

import myy803.traineeship_app.domain.Professor;
import myy803.traineeship_app.mappers.ProfessorMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.TestPropertySource;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@TestPropertySource(locations = "classpath:application.properties")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class ProfessorMapperTest {

    @Autowired
    private ProfessorMapper professorMapper;

    @Test
    void findByUsername_returnsProfessor_whenExists() {

        String username = "prof_test_" + System.currentTimeMillis();

        Professor professor = new Professor();
        professor.setUsername(username);
        professor.setProfessorName("Dr Test");
        professor.setInterests("AI");

        professorMapper.save(professor);
        professorMapper.flush();

        Professor found = professorMapper.findByUsername(username);

        assertThat(found).isNotNull();
        assertThat(found.getUsername()).isEqualTo(username);
        assertThat(found.getProfessorName()).isEqualTo("Dr Test");
    }

    @Test
    void findByUsername_returnsNull_whenNotExists() {
        Professor found = professorMapper.findByUsername("___NO_SUCH_PROFESSOR___");

        assertThat(found).isNull();
    }
}

