package is.cityreportsystem.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Report {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	private String title;
	private String note;
	private String date;
	private String content;
	private String type;
	private String state;
	private double x;
	private double y;
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "creator", referencedColumnName = "id", nullable = true)
	private Citizen creator;
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "recipient", referencedColumnName = "id", nullable = true)
	private CityService recipient;
}
