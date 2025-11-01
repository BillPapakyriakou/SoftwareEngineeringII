package myy803.traineeship_app.service;

import myy803.traineeship_app.domain.User;

public interface UserService {
	public void saveUser(User user);
    public boolean isUserPresent(User user);
	public User findById(String username);
}
