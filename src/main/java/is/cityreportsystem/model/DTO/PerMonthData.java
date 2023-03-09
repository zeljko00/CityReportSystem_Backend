package is.cityreportsystem.model.DTO;

import lombok.Data;

@Data
public class PerMonthData {
    private String month;
    private long received;
    private long solved;
}
