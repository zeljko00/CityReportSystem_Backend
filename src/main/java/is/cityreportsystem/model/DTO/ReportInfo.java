package is.cityreportsystem.model.DTO;

import lombok.Data;

@Data
public class ReportInfo {
    private double x;
    private double y;
    private String date;
    private String solvedDate;
    private String type;
}
