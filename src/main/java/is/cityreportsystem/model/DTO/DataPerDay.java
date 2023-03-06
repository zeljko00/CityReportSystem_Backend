package is.cityreportsystem.model.DTO;

import lombok.Data;

@Data
public class DataPerDay {
    private String date;
    private long solved;
    private long received;
}
