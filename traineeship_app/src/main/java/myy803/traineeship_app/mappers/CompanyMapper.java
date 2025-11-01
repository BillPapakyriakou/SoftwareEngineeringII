package myy803.traineeship_app.mappers;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import myy803.traineeship_app.domain.Company;

@Repository
public interface CompanyMapper extends JpaRepository<Company, String> {
	Company findByUsername(String username);
	List<Company> findByCompanyLocation(String location);
}
