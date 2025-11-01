package myy803.traineeship_app.controllers.supervisorsearchstrategies;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SupervisorAssigmentFactory {
	@Autowired
	AssignmentBasedOnLoad assigmentBasedOnLoad;
	
	@Autowired
	AssignmentBasedOnInterests assigmentBasedOnInterests;
	
	public SupervisorAssignmentStrategy create(String strategy) {
		// WARNING IF YOU MANUALLY CREATE THE STRATEGIES AUTOWIRE MAPPERS WITHIN THEM NOT WRORKING
		switch (strategy) {
		case "load":
			return assigmentBasedOnLoad;
		case "interests":
			return assigmentBasedOnInterests;
		default:
			return assigmentBasedOnLoad;
		}
	}
}
