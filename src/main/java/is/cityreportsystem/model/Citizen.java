package is.cityreportsystem.model;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;


@Entity
@Inheritance(strategy=InheritanceType.JOINED)
@Data 							// ne moramo pisati getere,setere i konstruktor
public class Citizen {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	private String idCard;
	private String firstName;
	private String lastName;
	private String phone;
	private String username;
	private String passwordHash;
	private byte active;
	@OneToMany(mappedBy = "creator")
	private List<Report> myReports; 	// = new ArrayList<Report>();
}
