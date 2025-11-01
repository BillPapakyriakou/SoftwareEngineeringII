package myy803.traineeship_app.domain;

public enum Role {
	STUDENT("Student"),
	PROFESSOR("Professor"),
	COMPANY("Company"),
	COMMITTEE("Committee");
	

    private final String value;

    private Role(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
