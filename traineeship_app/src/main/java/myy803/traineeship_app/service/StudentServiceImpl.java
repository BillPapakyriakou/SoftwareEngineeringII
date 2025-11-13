package myy803.traineeship_app.service;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import myy803.traineeship_app.domain.Student;
import myy803.traineeship_app.mappers.StudentMapper;

@Service
public class StudentServiceImpl implements StudentService {

    private final StudentMapper studentMapper;

    @Autowired
    public StudentServiceImpl(StudentMapper studentMapper) {
        this.studentMapper = studentMapper;
    }

    @Override
    public void saveProfile(Student student) {
        student.setLookingForTraineeship(true);
        studentMapper.save(student);
    }

    public Student retrieveProfile() {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String studentUsername = authentication.getName();
        System.err.println("Logged user: " + studentUsername);

        Student student = studentMapper.findByUsername(studentUsername);
        if (student == null) {
            student = new Student(studentUsername);
        }

        return student;
    }
}