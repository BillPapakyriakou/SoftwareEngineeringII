package myy803.traineeship_app.domain;

public enum EvaluationType {
	PROFESSOR_EVALUATION("Professor Evaluation"),
	COMPANY_EVALUATION("Company");	

    private final String value;

    private EvaluationType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
