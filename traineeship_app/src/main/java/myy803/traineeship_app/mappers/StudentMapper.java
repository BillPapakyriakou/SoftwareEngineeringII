package myy803.traineeship_app.mappers;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import myy803.traineeship_app.domain.Student;

@Repository
public interface StudentMapper extends JpaRepository<Student, String> {
	Student findByUsername(String username);
	List<Student> findByLookingForTraineeshipTrue();
}
