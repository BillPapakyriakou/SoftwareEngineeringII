package myy803.traineeship_app.service;

import myy803.traineeship_app.domain.Professor;

public interface ProfessorService {

    void saveProfile(Professor professor);

    Professor retrieveProfile();

}