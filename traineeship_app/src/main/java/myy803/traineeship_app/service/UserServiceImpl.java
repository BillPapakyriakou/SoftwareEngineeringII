package myy803.traineeship_app.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import myy803.traineeship_app.domain.Company;
import myy803.traineeship_app.domain.Professor;
import myy803.traineeship_app.domain.Student;
import myy803.traineeship_app.domain.User;

import myy803.traineeship_app.mappers.CompanyMapper;
import myy803.traineeship_app.mappers.ProfessorMapper;
import myy803.traineeship_app.mappers.StudentMapper;
import myy803.traineeship_app.mappers.UserMapper;


@Service
public class UserServiceImpl implements UserService, UserDetailsService {

	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;
	
	@Autowired
	private UserMapper userMapper;
	
	@Autowired
	private StudentMapper studentMapper;
	
	@Autowired
	private ProfessorMapper professorMapper;
	
	@Autowired
	CompanyMapper companyMapper;	
	
	@Override
	public void saveUser(User user) {
		String encodedPassword = bCryptPasswordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);
        userMapper.save(user);	
        
        switch(user.getRole()) {
        case STUDENT:
        	studentMapper.save(new Student(user.getUsername(), "", "", 0, "", "", "", true, null));
        	break;
        case COMPANY:
        	companyMapper.save(new Company(user.getUsername(), "", null, null)); 
        	break;
        case PROFESSOR:
        	professorMapper.save(new Professor(user.getUsername(), "", "", null)); 
        	break;
		default:
			break;
      }
    }

	@Override
	public boolean isUserPresent(User user) {
		User storedUser = userMapper.findByUsername(user.getUsername());
		return storedUser != null;
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		 return userMapper.findByUsername(username);
	}
	
	@Override
	public User findById(String username) {
		return userMapper.findByUsername(username);
	}
}