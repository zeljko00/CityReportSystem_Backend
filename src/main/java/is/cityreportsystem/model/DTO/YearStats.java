package is.cityreportsystem.model.DTO;

import lombok.Data;
import java.util.List;
@Data
public class YearStats {
    private long received;
    private double receivedPerDay;
    private long solved;
    private double solvedPerDay;
    private List<PerMonthData> perMonth;
    private List<ReportTuple> perType;
    private List<PerDepartmentData> perDepartmentData;

}
