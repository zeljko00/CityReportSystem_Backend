package is.cityreportsystem.model.DTO;

import lombok.Data;

import java.util.List;
@Data
public class EventRequest {
    private int random;
    private String title;
    private String description;
    private double x;
    private double y;
    private String type;
    private long creator;
    private List<CoordinateDTO> coords;
}
