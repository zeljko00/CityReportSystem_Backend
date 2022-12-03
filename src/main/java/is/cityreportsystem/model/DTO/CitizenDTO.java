package is.cityreportsystem.model.DTO;

import is.cityreportsystem.model.Report;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.Data;

import java.util.List;

@Data                            // ne moramo pisati getere,setere i konstruktor
public class CitizenDTO {

	private long id;
	private String idCard;
	private String firstName;
	private String lastName;
	private String phone;
	private String username;
	private String passwordHash;
	private byte active;
	private List<ReportDTO> myReports; 	// = new ArrayList<Report>();

	public String toString(){
		return idCard+" "+firstName+" "+lastName+" "+phone+" "+passwordHash;
	}
}