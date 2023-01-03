package is.cityreportsystem.model.DTO;

import is.cityreportsystem.model.CityOfficial;
import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Data
public class EventDTO {
	private long id;
	private String title;
	private String description;
	private String info;
	private double x;
	private double y;
	private String type;
	private String date;
	private short active;
	private CityOfficialDTO creator;
	private List<ImageDTO> images;
	private List<CoordinateDTO> coords;
}