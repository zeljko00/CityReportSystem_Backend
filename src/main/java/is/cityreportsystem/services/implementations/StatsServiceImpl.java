package is.cityreportsystem.services.implementations;

import is.cityreportsystem.DAO.ReportDAO;
import is.cityreportsystem.model.DTO.ReportInfo;
import is.cityreportsystem.model.DTO.ReportTuple;
import is.cityreportsystem.model.DTO.Stats;
import is.cityreportsystem.model.Report;
import is.cityreportsystem.model.enums.ReportType;
import is.cityreportsystem.services.StatsService;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class StatsServiceImpl implements StatsService {
    private final ReportDAO reportDAO;
    private final DateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
    private final ModelMapper modelMapper;

    public StatsServiceImpl(ReportDAO reportDAO, ModelMapper modelMapper) {
        this.reportDAO = reportDAO;
        this.modelMapper = modelMapper;
    }

    public Stats getStats(String startDate, String endDate, String type) {
        Stats result = new Stats();
        try {
            List<Report> reports = new ArrayList<>();
            List<Report> solved = new ArrayList<>();
            if ("all".equals(type.toLowerCase())) {
                reports = reportDAO.findReportsByDateGreaterThanEqualAndDateIsLessThanEqual(startDate + " 00:00:00", endDate + " 23:59:59");
                solved = reportDAO.findReportsBySolvedDateGreaterThanEqualAndSolvedDateIsLessThanEqual(startDate + " 00:00:00", endDate + " 23:59:59");
            } else {
                reports = reportDAO.findReportsByDateGreaterThanEqualAndDateIsLessThanEqualAndTypeIgnoreCase(startDate + " 00:00:00", endDate + " 23:59:59", type);
                solved = reportDAO.findReportsBySolvedDateGreaterThanEqualAndSolvedDateIsLessThanEqualAndTypeIgnoreCase(startDate + " 00:00:00", endDate + " 23:59:59", type);
            }
            result.setReports(reports.size());
            result.setSolvedReports(solved.size());
            List<Report> totalSolved = reportDAO.findReportsBySolvedDateNotNull();
            double totalAvg = totalSolved.stream().map((Report r) -> {
                try {
                    return (Double.valueOf(df.parse(r.getSolvedDate()).getTime() - df.parse(r.getDate()).getTime()) / 60000.0);
                } catch (Exception e) {
                    e.printStackTrace();
                    return 0.0;
                }
            }).reduce(0.0, (n1, n2) -> n1 + n2) / totalSolved.size();
            double avg = solved.stream().map((Report r) -> {
                try {
                    return (Double.valueOf(df.parse(r.getSolvedDate()).getTime() - df.parse(r.getDate()).getTime()) / 60000.0);
                } catch (Exception e) {
                    e.printStackTrace();
                    return 0.0;
                }
            }).reduce(0.0, (n1, n2) -> n1 + n2) / solved.size();
            result.setAvgTimeInMin(avg);
            ReportTuple maxTime = new ReportTuple();
            if (solved.size() > 1)
                solved.stream().map((Report r) -> {
                    try {
                        ReportTuple tuple = new ReportTuple();
                        tuple.setNumber((df.parse(r.getSolvedDate()).getTime() - df.parse(r.getDate()).getTime()) / 60000.0);
                        tuple.setType(r.getType());
                        return tuple;
                    } catch (Exception e) {
                        return new ReportTuple();
                    }
                }).max((rt1, rt2) -> {
                    return rt2.getNumber() > rt1.getNumber() ? -1 : 1;
                }).get();
            else if (solved.size() == 1)
                maxTime.setNumber((df.parse(solved.get(0).getSolvedDate()).getTime() - df.parse(solved.get(0).getDate()).getTime()) / 60000.0);
            else
                maxTime.setNumber(0);
            result.setMaxTime(maxTime);
            result.setReportsData(reports.stream().map(r -> {
                return modelMapper.map(r, ReportInfo.class);
            }).collect(Collectors.toList()));
            List<ReportTuple> reportsPerType = new ArrayList<>();
            List<ReportTuple> timePerType = new ArrayList<>();
            for (ReportType t : ReportType.values()) {
                List<Report> perType = reportDAO.findReportsByDateGreaterThanEqualAndDateIsLessThanEqualAndTypeIgnoreCase(startDate + " 00:00:00", endDate + " 23:59:59", t.name());
                List<Report> solvedPerType = reportDAO.findReportsBySolvedDateGreaterThanEqualAndSolvedDateIsLessThanEqualAndTypeIgnoreCase(startDate + " 00:00:00", endDate + " 23:59:59", t.name());
                ReportTuple tuple1 = new ReportTuple();
                tuple1.setType(t.name());
                tuple1.setNumber(perType.size());
                reportsPerType.add(tuple1);

                ReportTuple tuple2 = new ReportTuple();
                tuple2.setType(t.name());
                tuple2.setNumber(solvedPerType.stream().map((Report r) -> {
                    try {
                        return (Double.valueOf(df.parse(r.getSolvedDate()).getTime() - df.parse(r.getDate()).getTime()) / 60000.0);
                    } catch (Exception e) {
                        e.printStackTrace();
                        return 0.0;
                    }
                }).reduce(0.0, (n1, n2) -> n1 + n2) / solvedPerType.size());
                timePerType.add(tuple2);

            }
            result.setReportsPerType(reportsPerType);
            result.setAvgTimePerType(timePerType);
            result.setDifferencePercentage((int) (maxTime.getNumber() / avg * 100.0));
            System.out.println(solved.size()+"/"+reports.size()+"*100="+((int) (Double.valueOf(solved.size()) / reports.size()*100.0)));
            result.setAvgPercentage((int) (avg / totalAvg * 100.0));
            result.setSolvedPercentage((int) (Double.valueOf(solved.size()) / reports.size()*100.0));
            return result;
        } catch (Exception ee) {
            ee.printStackTrace();
            return null;
        }
    }
}
