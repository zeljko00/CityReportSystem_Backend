package is.cityreportsystem.model.DTO;

import lombok.Data;

import java.util.List;

@Data
public class ReportDTO {
	private long id;
	private String title;
	private String note;
	private String date;
	private String solvedDate;
	private String content;
	private String type;
	private String state;
	private double x;
	private double y;
	private String requiredAdditionalInfo;
	private String providedAdditionalInfo;
	private String feedback;
	private boolean requiredInfo;
	private CitizenDTO creator;
	private List<ImageDTO> images;
}
