package myy803.traineeship_app;

import myy803.traineeship_app.domain.Student;
import myy803.traineeship_app.mappers.StudentMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.TestPropertySource;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@TestPropertySource(locations = "classpath:application.properties")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class StudentMapperTest {

    @Autowired
    private StudentMapper studentMapper;

    @Test
    void findByUsername_returnsStudent_whenExists() {
        String username = "student_test_" + System.currentTimeMillis();

        Student student = new Student();
        student.setUsername(username);
        student.setStudentName("Test Student");
        student.setLookingForTraineeship(true);

        studentMapper.save(student);
        studentMapper.flush();

        Student found = studentMapper.findByUsername(username);

        assertThat(found).isNotNull();
        assertThat(found.getUsername()).isEqualTo(username);
        assertThat(found.getStudentName()).isEqualTo("Test Student");
    }

    @Test
    void findByUsername_returnsNull_whenNotExists() {
        Student found = studentMapper.findByUsername("___NO_SUCH_STUDENT___");

        assertThat(found).isNull();
    }

    @Test
    void findByLookingForTraineeshipTrue_returnsOnlyMatchingStudents() {
        String user1 = "s1_" + System.currentTimeMillis();
        String user2 = "s2_" + System.currentTimeMillis();
        String user3 = "s3_" + System.currentTimeMillis();

        Student s1 = new Student();
        s1.setUsername(user1);
        s1.setStudentName("Student One");
        s1.setLookingForTraineeship(true);

        Student s2 = new Student();
        s2.setUsername(user2);
        s2.setStudentName("Student Two");
        s2.setLookingForTraineeship(true);

        Student s3 = new Student();
        s3.setUsername(user3);
        s3.setStudentName("Student Three");
        s3.setLookingForTraineeship(false);

        studentMapper.saveAll(List.of(s1, s2, s3));
        studentMapper.flush();

        List<Student> result = studentMapper.findByLookingForTraineeshipTrue();

        assertThat(result)
                .extracting(Student::getUsername)
                .contains(user1, user2)
                .doesNotContain(user3);
    }
}

