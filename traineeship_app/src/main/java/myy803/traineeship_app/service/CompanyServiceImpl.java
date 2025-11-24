package myy803.traineeship_app.service;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import myy803.traineeship_app.domain.Company;
import myy803.traineeship_app.domain.TraineeshipPosition;
import myy803.traineeship_app.mappers.CompanyMapper;
import myy803.traineeship_app.mappers.TraineeshipPositionsMapper;

import java.util.List;

@Service
public class CompanyServiceImpl implements CompanyService {

    private final CompanyMapper companyMapper;
    private final TraineeshipPositionsMapper positionsMapper;

    @Autowired
    public CompanyServiceImpl(CompanyMapper companyMapper, TraineeshipPositionsMapper positionsMapper) {
        this.companyMapper = companyMapper;
        this.positionsMapper = positionsMapper;
    }

    @Override
    public void saveProfile(Company company) {
        companyMapper.save(company);
    }

    @Override
    public Company retrieveProfile() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        System.err.println("Logged user: " + username);

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

    @Override
    public List<TraineeshipPosition> retrieveAssignedPositions(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        Company company = companyMapper.findByUsername(username);

        List<TraineeshipPosition> assignedPositions = company.getAssignedPositions();

        return assignedPositions;
    }

    @Override
    public void deletePosition(Integer positionId){
        if (positionsMapper.existsById(positionId)) {
            positionsMapper.deleteById(positionId);
        } else {
            throw new IllegalArgumentException("Position with ID " + positionId + " not found.");
        }
    }
}