package is.cityreportsystem.model;

import jakarta.persistence.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
@Entity
@PrimaryKeyJoinColumn(name="id")				//referencing base clase object (required for vertical inheritance mapping)
@Data
public class CityOfficial extends  Citizen{
	private String position;
	private String education;
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="department", referencedColumnName = "id")
	private CityService department;
	@OneToMany(mappedBy = "creator", fetch = FetchType.LAZY)
	private List<Event> myEvents;

}
