package is.cityreportsystem.model.DTO;

import lombok.Data;

import java.util.List;
@Data
public class ReportRequest {
    private long id;
    private String title;
    private String note;
    private String content;
    private String type;
    private double x;
    private double y;
    private long creator;
}
