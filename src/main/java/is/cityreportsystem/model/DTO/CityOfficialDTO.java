package is.cityreportsystem.model.DTO;

import is.cityreportsystem.model.Citizen;
import is.cityreportsystem.model.CityService;
import is.cityreportsystem.model.Event;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class CityOfficialDTO extends CitizenDTO {
	private String position;
	private String education;
	private CityServiceDTO department;
	private int createdEventsNum;
	private int activeEventsNum;
	private int solvedReportsNum;
}
