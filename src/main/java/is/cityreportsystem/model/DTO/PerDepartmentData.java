package is.cityreportsystem.model.DTO;

import lombok.Data;

@Data
public class PerDepartmentData {
    private String department;
    private long received;
    private long solved;
    private double avgTime;
}
