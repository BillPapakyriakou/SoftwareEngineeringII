package myy803.traineeship_app.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name="evaluations")
public class Evaluation {
	@Id
	@Column(name="id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@Enumerated(EnumType.STRING)
    @Column(name="evaluation_type")
    private EvaluationType evaluationType;
	
	@Column(name="motivation")
	int motivation;
	
	@Column(name="efficiency")
	int efficiency;
	
	@Column(name="effectiveness")
	int effectiveness;

	@Column(name = "facilities")
	private Integer facilities; // Only for PROFESSOR_EVALUATION

	@Column(name = "guidance")
	private Integer guidance; // Only for PROFESSOR_EVALUATION

	public Evaluation() {}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public EvaluationType getEvaluationType() {
		return evaluationType;
	}

	public void setEvaluationType(EvaluationType evaluationType) {
		this.evaluationType = evaluationType;
	}

	public int getMotivation() {
		return motivation;
	}

	public void setMotivation(int motivation) {
		this.motivation = motivation;
	}

	public int getEfficiency() {
		return efficiency;
	}

	public void setEfficiency(int efficiency) {
		this.efficiency = efficiency;
	}

	public int getEffectiveness() {
		return effectiveness;
	}

	public void setEffectiveness(int effectiveness) {
		this.effectiveness = effectiveness;
	}

	public Integer getFacilities() {
		return facilities;
	}

	public void setFacilities(Integer facilities) {
		this.facilities = facilities;
	}

	public Integer getGuidance() {
		return guidance;
	}

	public void setGuidance(Integer guidance) {
		this.guidance = guidance;
	}
}
