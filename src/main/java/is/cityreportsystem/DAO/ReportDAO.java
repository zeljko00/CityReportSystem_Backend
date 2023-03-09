package is.cityreportsystem.DAO;

import is.cityreportsystem.model.Citizen;
import is.cityreportsystem.model.DTO.PageDTO;
import is.cityreportsystem.model.Report;
import is.cityreportsystem.model.ReportType;
import is.cityreportsystem.model.enums.ReportState;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ReportDAO extends JpaRepository<Report,Long> {
    List<Report> findReportsByCreator(Citizen citizen);

    Page<Report> findReportsByTypeIn(Pageable pageable,List<String> types);
    Page<Report> findReportsByTypeInAndState(Pageable pageable, List<String> types, ReportState state);
    Page<Report> findReportsByTypeInAndTitleContainsIgnoreCase(Pageable pageable,List<String> types,String search);
    Page<Report> findReportsByTypeInAndStateAndTitleContainsIgnoreCase(Pageable pageable,List<String> types,ReportState state, String search);

    Page<Report> findReportsByType(Pageable pageable,String type);
    Page<Report> findReportsByTypeAndState(Pageable pageable,String type,ReportState state);
    Page<Report> findReportsByTypeAndTitleContainsIgnoreCase(Pageable pageable,String type,String search);
    Page<Report> findReportsByTypeAndStateAndTitleContainsIgnoreCase(Pageable pageable,String type,ReportState state,String search);

    List<Report> findReportsByDateGreaterThanEqualAndDateIsLessThanEqual(String startDate,String endDate);
    List<Report> findReportsByDateGreaterThanEqualAndDateIsLessThanEqualAndTypeIgnoreCase(String startDate,String endDate,String type);
    List<Report> findReportsBySolvedDateGreaterThanEqualAndSolvedDateIsLessThanEqual(String startDate,String endDate);
    List<Report> findReportsBySolvedDateGreaterThanEqualAndSolvedDateIsLessThanEqualAndTypeIgnoreCase(String startDate,String endDate,String type);
    List<Report> findReportsBySolvedDateNotNull();
    List<Report> findReportsBySolvedDate(String solvedDate);
    List<Report> findReportsByDate(String date);
    List<Report> findReportsByDateContains(String year);
    List<Report> findReportsBySolvedDateContains(String year);

}
