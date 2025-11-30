package myy803.traineeship_app.service;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import myy803.traineeship_app.domain.Student;
import myy803.traineeship_app.domain.TraineeshipPosition;
import myy803.traineeship_app.mappers.StudentMapper;
import myy803.traineeship_app.mappers.TraineeshipPositionsMapper;

@Service
public class StudentServiceImpl implements StudentService {

    private final StudentMapper studentMapper;
    private final TraineeshipPositionsMapper positionsMapper;

    @Autowired
    public StudentServiceImpl(StudentMapper studentMapper, TraineeshipPositionsMapper positionsMapper) {
        this.studentMapper = studentMapper;
        this.positionsMapper = positionsMapper;
    }


    @Override
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

    @Override
    public void saveLogbook(Integer positionId, String newLogbook){
        TraineeshipPosition position = positionsMapper.findById(positionId)
                .orElseThrow(() -> new RuntimeException("Traineeship Position not found with ID: " + positionId));

        position.setStudentLogbook(newLogbook);
        positionsMapper.save(position);
    }

    public void saveProfile(Student student) {

        Student existingStudent = studentMapper.findByUsername(student.getUsername());

        existingStudent.setStudentName(student.getStudentName());
        existingStudent.setAM(student.getAM());
        existingStudent.setAvgGrade(student.getAvgGrade());
        existingStudent.setPreferredLocation(student.getPreferredLocation());
        existingStudent.setInterests(student.getInterests());
        existingStudent.setSkills(student.getSkills());


        if (existingStudent.getAssignedTraineeship() == null) {
            existingStudent.setLookingForTraineeship(student.isLookingForTraineeship());
        }
        studentMapper.save(existingStudent);
    }
}