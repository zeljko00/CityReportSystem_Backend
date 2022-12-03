package is.cityreportsystem.model;

import jakarta.persistence.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
@Entity
@PrimaryKeyJoinColumn(name="id")
@Data
public class CityOfficial extends  Citizen{
	private String position;
	private String education;
	private String serviceUsername;
	private String servicePassword;
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="department", referencedColumnName = "id")
	private CityService department;
	@OneToMany(mappedBy = "creator")
	private List<Event> myEvents=new ArrayList<Event>();
}
