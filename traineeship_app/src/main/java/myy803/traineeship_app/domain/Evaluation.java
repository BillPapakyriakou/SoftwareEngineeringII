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
}
