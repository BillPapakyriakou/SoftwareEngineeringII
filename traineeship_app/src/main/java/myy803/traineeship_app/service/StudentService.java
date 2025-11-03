package myy803.traineeship_app.service;

import myy803.traineeship_app.domain.Student;

public interface StudentService {

    void saveProfile(Student student);

    Student retrieveProfile();

}