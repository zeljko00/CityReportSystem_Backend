package is.cityreportsystem.model.DTO;

import lombok.Data;
import java.util.List;
@Data
public class Stats {
    private long reports;
    private long solvedReports;
    private int solvedPercentage;
    private List<ReportTuple> reportsPerType;
    private List<ReportTuple> avgTimePerType;
    private List<ReportTuple> totalTimePerType;
    private List<DataPerDay> dataPerDay;
    private double avgTimeInMin;
    private int avgPercentage;
    private ReportTuple maxTime;
    private int differencePercentage;
    private List<ReportInfo> reportsData;
    private double reportsPerDay;
    private double solvedPerDay;
}
