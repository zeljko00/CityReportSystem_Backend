package is.cityreportsystem.model;

import is.cityreportsystem.model.enums.EventType;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.Type;

import java.util.List;

@Entity
@Data
public class Event {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	private String title;
	private String description;
	private String info;
	private double x;		//nece trebati nakon implmentacije niza koordinata
	private double y;
	@Enumerated(EnumType.STRING)
	private EventType type;
//	@Column(columnDefinition ="DATETIME")
	private String date;
	private boolean active;
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "creator", referencedColumnName = "id", nullable = false)
	private CityOfficial creator;
	@OneToMany(mappedBy = "event", fetch = FetchType.LAZY)
	private List<EventImage> images;
	@OneToMany(mappedBy = "event", fetch = FetchType.LAZY)
	private List<Coordinate> coords;
}
