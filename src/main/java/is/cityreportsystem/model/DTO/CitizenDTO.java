package is.cityreportsystem.model.DTO;

import is.cityreportsystem.model.Citizen;
import is.cityreportsystem.model.Report;
import is.cityreportsystem.model.enums.UserRole;
import is.cityreportsystem.model.enums.UserStatus;
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
	private String password;
	private UserStatus status;
	private UserRole role;

	public String toString(){
		return idCard+" "+firstName+" "+lastName+" "+phone+" "+password;
	}
}