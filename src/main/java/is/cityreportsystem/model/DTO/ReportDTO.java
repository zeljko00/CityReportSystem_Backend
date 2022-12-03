package is.cityreportsystem.model.DTO;

import lombok.Data;
@Data
public class ReportDTO {
	private long id;
	private String title;
	private String note;
	private String date;
	private String content;
	private String type;
	private String state;
	private double x;
	private double y;
	private CitizenDTO creator;
	private CityServiceDTO recipient;


}
