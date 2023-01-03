package is.cityreportsystem.model;

import jakarta.persistence.*;
import lombok.Data;

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
	private double x;
	private double y;
	private String type;
	private String date;
	private short active;
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "creator", referencedColumnName = "id", nullable = false)
	private CityOfficial creator;
	@OneToMany(mappedBy = "event", fetch = FetchType.LAZY)
	private List<EventImage> images;
	@OneToMany(mappedBy = "event", fetch = FetchType.LAZY)
	private List<Coordinate> coords;
}
