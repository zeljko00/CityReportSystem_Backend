package is.cityreportsystem.services.implementations;

import is.cityreportsystem.DAO.ReportDAO;
import is.cityreportsystem.model.DTO.DataPerDay;
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
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
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
            Date start=df.parse(startDate+" 00:00:00");
            Date end=df.parse(endDate + " 00:00:00");
            LocalDate startLocal=start.toInstant()
                    .atZone(ZoneId.systemDefault())
                    .toLocalDate();
            LocalDate endLocal=end.toInstant()
                    .atZone(ZoneId.systemDefault())
                    .toLocalDate();
            List<LocalDate> beetween=startLocal.datesUntil(endLocal)
                    .collect(Collectors.toList());
            List<DataPerDay> dataPerDay=new ArrayList<>();
            long difference = end.getTime() - start.getTime();
            System.out.println(difference);
            double days = (difference / (1000*60*60*24));
            System.out.println(days);
            List<Report> reports = new ArrayList<>();
            List<Report> solved = new ArrayList<>();
            if ("all".equals(type.toLowerCase())) {
                reports = reportDAO.findReportsByDateGreaterThanEqualAndDateIsLessThanEqual(startDate + " 00:00:00", endDate + " 23:59:59");
                solved = reportDAO.findReportsBySolvedDateGreaterThanEqualAndSolvedDateIsLessThanEqual(startDate + " 00:00:00", endDate + " 23:59:59");
            } else {
                reports = reportDAO.findReportsByDateGreaterThanEqualAndDateIsLessThanEqualAndTypeIgnoreCase(startDate + " 00:00:00", endDate + " 23:59:59", type);
                solved = reportDAO.findReportsBySolvedDateGreaterThanEqualAndSolvedDateIsLessThanEqualAndTypeIgnoreCase(startDate + " 00:00:00", endDate + " 23:59:59", type);
            }
            for(LocalDate d: beetween){
                DataPerDay data=new DataPerDay();
                data.setSolved(solved.stream().filter((Report r) -> {return r.getSolvedDate().contains(d.toString());}).count());
                data.setDate(d.toString());
                data.setReceived(reports.stream().filter((Report r) -> {return r.getDate().contains(d.toString());}).count());
          dataPerDay.add(data);
            }
            result.setDataPerDay(dataPerDay);
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
            maxTime.setNumber(0);
            if (solved.size() > 1){
                System.out.println("More than 1 solved report");
                ReportTuple max=solved.stream().map((Report r) -> {
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
                maxTime.setNumber(max.getNumber());
            maxTime.setType(max.getType());}
            else if (solved.size() == 1) {
                maxTime.setNumber((df.parse(solved.get(0).getSolvedDate()).getTime() - df.parse(solved.get(0).getDate()).getTime()) / 60000.0);
                maxTime.setType(solved.get(0).getType());
            }
            result.setMaxTime(maxTime);
            result.setReportsData(reports.stream().map(r -> {
                return modelMapper.map(r, ReportInfo.class);
            }).collect(Collectors.toList()));
            List<ReportTuple> reportsPerType = new ArrayList<>();
            List<ReportTuple> timePerType = new ArrayList<>();
            List<ReportTuple> totalTimePerType = new ArrayList<>();
            for (ReportType t : ReportType.values()) {
                List<Report> perType = reportDAO.findReportsByDateGreaterThanEqualAndDateIsLessThanEqualAndTypeIgnoreCase(startDate + " 00:00:00", endDate + " 23:59:59", t.name());
                List<Report> solvedPerType = reportDAO.findReportsBySolvedDateGreaterThanEqualAndSolvedDateIsLessThanEqualAndTypeIgnoreCase(startDate + " 00:00:00", endDate + " 23:59:59", t.name());
                ReportTuple tuple1 = new ReportTuple();
                tuple1.setType(t.name());
                tuple1.setNumber(perType.size());
                reportsPerType.add(tuple1);

                ReportTuple tuple2 = new ReportTuple();
                tuple2.setType(t.name());
                if(solvedPerType.size()>1)
                tuple2.setNumber(solvedPerType.stream().map((Report r) -> {
                    try {
                        return (Double.valueOf(df.parse(r.getSolvedDate()).getTime() - df.parse(r.getDate()).getTime()) / 3600000.0);
                    } catch (Exception e) {
                        e.printStackTrace();
                        return 0.0;
                    }
                }).reduce(0.0, (n1, n2) -> n1 + n2) / solvedPerType.size());
                else if(solvedPerType.size()==1) {
                    System.out.println(solvedPerType.get(0).getDate()+"    "+solvedPerType.get(0).getSolvedDate());
                    try {
                    tuple2.setNumber(Double.valueOf(df.parse(solvedPerType.get(0).getSolvedDate()).getTime() - df.parse(solvedPerType.get(0).getDate()).getTime()) / 3600000.0);

                    } catch (Exception e) {
                        e.printStackTrace();
                        tuple2.setNumber(0.0);
                    }} else
                        tuple2.setNumber(0);
                timePerType.add(tuple2);

                ReportTuple tuple3 = new ReportTuple();
                tuple3.setType(t.name());
                tuple3.setNumber(solvedPerType.stream().map((Report r) -> {
                    try {
                        return (Double.valueOf(df.parse(r.getSolvedDate()).getTime() - df.parse(r.getDate()).getTime()) / 3600000.0);
                    } catch (Exception e) {
                        e.printStackTrace();
                        return 0.0;
                    }
                }).reduce(0.0, (n1, n2) -> n1 + n2));
                totalTimePerType.add(tuple3);
            }
            System.out.println(reports.size()+" / "+days+"="+reports.size()/days);
            result.setReportsPerDay(reports.size()/days);
            result.setSolvedPerDay(solved.size()/days);
            result.setReportsPerType(reportsPerType);
            result.setAvgTimePerType(timePerType);
            result.setTotalTimePerType(totalTimePerType);
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
