package myy803.traineeship_app.mappers;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import myy803.traineeship_app.domain.Professor;

@Repository
public interface ProfessorMapper extends JpaRepository<Professor, String> {
	
	Professor findByUsername(String username);

}
