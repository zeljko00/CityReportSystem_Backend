package is.cityreportsystem.DAO;

import is.cityreportsystem.model.Citizen;
import is.cityreportsystem.model.Report;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ReportDAO extends JpaRepository<Report,Long> {
    List<Report> findReportsByCreator(Citizen citizen);
}
