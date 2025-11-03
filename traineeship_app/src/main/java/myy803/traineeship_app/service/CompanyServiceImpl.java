package myy803.traineeship_app.service;

import myy803.traineeship_app.domain.Company;
import myy803.traineeship_app.domain.TraineeshipPosition;
import myy803.traineeship_app.mappers.CompanyMapper;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CompanyServiceImpl implements CompanyService {

    private final CompanyMapper companyMapper;

    public CompanyServiceImpl(final CompanyMapper companyMapper) {
        this.companyMapper = companyMapper;
    }

    @Override
    public void saveProfile(Company company) {
        companyMapper.save(company);
    }

    @Override
    public Company retrieveProfile() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        System.err.println("Logged use: " + username);

        Company company = companyMapper.findByUsername(username);
        if (company == null) {
            company = new Company(username);
        }

        return company;
    }

    @Override
    public List<TraineeshipPosition> listAvailablePositions() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        System.err.println("Logged use: " + username);

        Company company = companyMapper.findByUsername(username);
        List<TraineeshipPosition> positions = company.getAvailablePositions();

        return company.getAvailablePositions();
    }

    @Override
    public TraineeshipPosition showPositionForm() {
        return new TraineeshipPosition();
    }

    @Override
    public void savePosition(TraineeshipPosition position) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        Company company = companyMapper.findByUsername(username);
        if (company == null) {
            company = new Company(username);
        }

        position.setCompany(company);
        company.addPosition(position);
        companyMapper.save(company);
    }
}