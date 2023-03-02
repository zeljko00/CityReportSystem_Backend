package is.cityreportsystem.model;

import is.cityreportsystem.model.enums.ReportState;
import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

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
	@Enumerated(EnumType.STRING)
	private ReportState state;
	private double x;
	private double y;
	private String requiredAdditionalInfo;
	private String providedAdditionalInfo;
	private String feedback;
	private boolean requiredInfo;
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "creator", referencedColumnName = "id", nullable = true)
	private Citizen creator;
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "recepient", referencedColumnName = "id", nullable = true)
	private CityService recepient;
	@OneToMany(mappedBy = "report", fetch = FetchType.LAZY)
	private List<ReportImage> images;
}
