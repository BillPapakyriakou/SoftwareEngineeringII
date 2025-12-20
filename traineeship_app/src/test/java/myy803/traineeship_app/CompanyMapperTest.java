package myy803.traineeship_app;

import myy803.traineeship_app.domain.Company;
import myy803.traineeship_app.mappers.CompanyMapper;
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
class CompanyMapperTest {

    @Autowired
    private CompanyMapper companyMapper;

    @Test
    void findByUsername_returnsCompany_whenExists() {
        Company company = new Company();
        company.setUsername("testcompany");
        company.setCompanyName("Test Company");
        company.setCompanyLocation("Athens");

        companyMapper.save(company);

        Company found = companyMapper.findByUsername("testcompany");

        assertThat(found).isNotNull();
        assertThat(found.getUsername()).isEqualTo("testcompany");
        assertThat(found.getCompanyName()).isEqualTo("Test Company");
        assertThat(found.getCompanyLocation()).isEqualTo("Athens");
    }

    @Test
    void findByUsername_returnsNull_whenNotExists() {
        Company found = companyMapper.findByUsername("doesNotExist");

        assertThat(found).isNull();
    }

    @Test
    void findByCompanyLocation_returnsCompaniesInLocation() {
        Company c1 = new Company();
        c1.setUsername("c1");
        c1.setCompanyName("Company One");
        c1.setCompanyLocation("Athens");

        Company c2 = new Company();
        c2.setUsername("c2");
        c2.setCompanyName("Company Two");
        c2.setCompanyLocation("Athens");

        Company c3 = new Company();
        c3.setUsername("c3");
        c3.setCompanyName("Company Three");
        c3.setCompanyLocation("Patras");

        companyMapper.saveAll(List.of(c1, c2, c3));
        companyMapper.flush(); // ✅ important

        List<Company> athensCompanies = companyMapper.findByCompanyLocation("Athens");

        assertThat(athensCompanies)
                .extracting(Company::getUsername)
                .contains("c1", "c2");
    }


    @Test
    void findByCompanyLocation_returnsEmptyList_whenNoneMatch() {
        List<Company> result =
                companyMapper.findByCompanyLocation("Thessaloniki");

        assertThat(result).isEmpty();
    }
}
