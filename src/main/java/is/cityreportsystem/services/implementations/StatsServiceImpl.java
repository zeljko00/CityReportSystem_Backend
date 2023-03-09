package is.cityreportsystem.services.implementations;

import is.cityreportsystem.DAO.CityServiceDAO;
import is.cityreportsystem.DAO.ReportDAO;
import is.cityreportsystem.model.CityService;
import is.cityreportsystem.model.DTO.*;
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
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
public class StatsServiceImpl implements StatsService {
    private final ReportDAO reportDAO;
    private final CityServiceDAO cityServiceDAO;
    private final DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private final DateFormat input_df = new SimpleDateFormat("yyyy-MM-dd");
    private final ModelMapper modelMapper;

    public StatsServiceImpl(ReportDAO reportDAO, CityServiceDAO cityServiceDAO, ModelMapper modelMapper) {
        this.reportDAO = reportDAO;
        this.cityServiceDAO = cityServiceDAO;
        this.modelMapper = modelMapper;
    }

    public Stats getStats(String startDate, String endDate, String type) {
        Stats result = new Stats();
        try {
            System.out.println(startDate + " 00:00:00");
            Date start = input_df.parse(startDate);
            System.out.println(endDate + " 00:00:00");
            Date end = input_df.parse(endDate);
            LocalDate startLocal = start.toInstant()
                    .atZone(ZoneId.systemDefault())
                    .toLocalDate();
            System.out.println("start-local: " + startLocal);
            LocalDate endLocal = end.toInstant()
                    .atZone(ZoneId.systemDefault())
                    .toLocalDate();
            System.out.println("end-local: " + endLocal);
            List<LocalDate> beetween = new ArrayList<>();
            try {
                beetween = startLocal.datesUntil(endLocal)
                        .collect(Collectors.toList());
            } catch (Exception e) {
                e.printStackTrace();
            }
            List<DataPerDay> dataPerDay = new ArrayList<>();
            ;
            long days = beetween.size();
            List<Report> reports = new ArrayList<>();
            List<Report> solved = new ArrayList<>();
            if ("all".equals(type.toLowerCase())) {
                reports = reportDAO.findReportsByDateGreaterThanEqualAndDateIsLessThanEqual(startDate + " 00:00:00", endDate + " 23:59:59");
                solved = reportDAO.findReportsBySolvedDateGreaterThanEqualAndSolvedDateIsLessThanEqual(startDate + " 00:00:00", endDate + " 23:59:59");
            } else {
                reports = reportDAO.findReportsByDateGreaterThanEqualAndDateIsLessThanEqualAndTypeIgnoreCase(startDate + " 00:00:00", endDate + " 23:59:59", type);
                solved = reportDAO.findReportsBySolvedDateGreaterThanEqualAndSolvedDateIsLessThanEqualAndTypeIgnoreCase(startDate + " 00:00:00", endDate + " 23:59:59", type);
            }
            for (LocalDate d : beetween) {
                DataPerDay data = new DataPerDay();
                data.setSolved(solved.stream().filter((Report r) -> {
                    return r.getSolvedDate().contains(d.toString());
                }).count());
                data.setDate(d.toString());
                data.setReceived(reports.stream().filter((Report r) -> {
                    return r.getDate().contains(d.toString());
                }).count());
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
                    System.out.println(r.getSolvedDate());
                    System.out.println(r.getDate());
                    e.printStackTrace();

                    return 0.0;
                }
            }).reduce(0.0, (n1, n2) -> n1 + n2) / totalSolved.size();
            double avg = solved.stream().map((Report r) -> {
                try {
                    System.out.println(r.getDate() + " - " + r.getSolvedDate());
                    return (Double.valueOf(df.parse(r.getSolvedDate()).getTime() - df.parse(r.getDate()).getTime()) / 60000.0);
                } catch (Exception e) {
                    e.printStackTrace();
                    return 0.0;
                }
            }).reduce(0.0, (n1, n2) -> n1 + n2) / solved.size();
            result.setAvgTimeInMin(avg);
            ReportTuple maxTime = new ReportTuple();
            maxTime.setNumber(0);
            if (solved.size() > 1) {
                ReportTuple max = solved.stream().map((Report r) -> {
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
                maxTime.setType(max.getType());
            } else if (solved.size() == 1) {
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
                if (solvedPerType.size() > 1)
                    tuple2.setNumber(solvedPerType.stream().map((Report r) -> {
                        try {
                            return (Double.valueOf(df.parse(r.getSolvedDate()).getTime() - df.parse(r.getDate()).getTime()) / 3600000.0);
                        } catch (Exception e) {
                            e.printStackTrace();
                            return 0.0;
                        }
                    }).reduce(0.0, (n1, n2) -> n1 + n2) / solvedPerType.size());
                else if (solvedPerType.size() == 1) {
                    try {
                        tuple2.setNumber(Double.valueOf(df.parse(solvedPerType.get(0).getSolvedDate()).getTime() - df.parse(solvedPerType.get(0).getDate()).getTime()) / 3600000.0);

                    } catch (Exception e) {
                        e.printStackTrace();
                        tuple2.setNumber(0.0);
                    }
                } else
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
            result.setReportsPerDay(reports.size() / days);
            result.setSolvedPerDay(solved.size() / days);
            result.setReportsPerType(reportsPerType);
            result.setAvgTimePerType(timePerType);
            result.setTotalTimePerType(totalTimePerType);
            result.setDifferencePercentage((int) (maxTime.getNumber() / avg * 100.0));
            result.setAvgPercentage((int) (avg / totalAvg * 100.0));
            result.setSolvedPercentage((int) (Double.valueOf(solved.size()) / reports.size() * 100.0));
            return result;
        } catch (Exception ee) {
            ee.printStackTrace();
            return null;
        }
    }

    @Override
    public YearStats getStatsByYear(int year) {
        YearStats result = new YearStats();
        List<Report> reportsReceived = reportDAO.findReportsByDateContains(Integer.toString(year));
        List<Report> reportsSolved = reportDAO.findReportsBySolvedDateContains(Integer.toString(year));
        result.setReceived(reportsReceived.size());
        result.setSolved(reportsSolved.size());

        HashMap<String, PerMonthData> reportsPerMonth = new HashMap();
        HashMap<String, List<Report>> reportsPerType = new HashMap<>();
        reportsReceived.stream().forEach(report -> {
            String[] tokens = report.getDate().split("-");
            if (reportsPerType.containsKey(report.getType()) == false)
                reportsPerType.put(report.getType(), new ArrayList<>());
            reportsPerType.get(report.getType()).add(report);
            if (reportsPerMonth.containsKey(tokens[1]) == false) {
                PerMonthData temp = new PerMonthData();
                temp.setMonth(tokens[1]);
                reportsPerMonth.put(tokens[1], temp);
            }
            reportsPerMonth.get(tokens[1]).setReceived(reportsPerMonth.get(tokens[1]).getReceived() + 1);
        });
        reportsSolved.stream().forEach(report -> {
            String[] tokens = report.getSolvedDate().split("-");
            if (reportsPerMonth.containsKey(tokens[1]) == false) {
                PerMonthData temp = new PerMonthData();
                temp.setMonth(tokens[1]);
                reportsPerMonth.put(tokens[1], temp);
            }
            reportsPerMonth.get(tokens[1]).setSolved(reportsPerMonth.get(tokens[1]).getSolved() + 1);
        });
        List<PerMonthData> perMonth = new ArrayList<>();
        for (Map.Entry<String, PerMonthData> entry : reportsPerMonth.entrySet())
            perMonth.add(entry.getValue());
        result.setPerMonth(perMonth);
        List<ReportTuple> perTypeList = new ArrayList<>();
        for (Map.Entry<String, List<Report>> entry : reportsPerType.entrySet()) {
            ReportTuple temp = new ReportTuple();
            temp.setType(entry.getKey());
            temp.setNumber(entry.getValue().size());
            perTypeList.add(temp);
        }
        result.setPerType(perTypeList);
        double days = 1;
        if (LocalDate.now().getYear() == year)
            days = LocalDate.of(year, 1, 1).datesUntil(LocalDate.now()).count();
        else
            days = 365;
        result.setSolvedPerDay(reportsSolved.size() / days);
        result.setReceivedPerDay(reportsReceived.size() / days);
        List<CityService> departments = cityServiceDAO.findAll();
        List<PerDepartmentData> perDepartmentData = departments.stream().map(department -> {
            PerDepartmentData temp = new PerDepartmentData();
            temp.setDepartment(department.getName());
            temp.setReceived(department.getArrivedReports().stream().filter(r -> r.getDate().contains(Integer.toString(year))).count());
            temp.setSolved(department.getArrivedReports().stream().filter(r -> r.getSolvedDate() != null && r.getSolvedDate().contains(Integer.toString(year))).count());
            double avgInHrs = department.getArrivedReports().stream().filter(r -> r.getSolvedDate() != null && r.getSolvedDate().contains(Integer.toString(year))).map(r -> {
                try {
                    double d = Double.valueOf(df.parse(r.getSolvedDate()).getTime() - df.parse(r.getDate()).getTime()) / 3600000.0;
                    System.out.println(r.getSolvedDate()+" - "+r.getDate()+" "+d);
                    return d;
                } catch (Exception e) {
                    e.printStackTrace();
                    return 0.0;
                }
            }).reduce(0.0, (n1, n2) -> n1 + n2 / temp.getSolved());
            temp.setAvgTime(avgInHrs);
            return temp;
        }).collect(Collectors.toList());
        result.setPerDepartmentData(perDepartmentData);
        return result;
    }

}
