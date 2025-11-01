package myy803.traineeship_app.mappers;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import myy803.traineeship_app.domain.User;

@Repository
public interface UserMapper extends JpaRepository<User, String> {
	
	User findByUsername(String username);

}
