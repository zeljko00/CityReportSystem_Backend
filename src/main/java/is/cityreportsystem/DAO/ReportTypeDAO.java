package is.cityreportsystem.DAO;

import is.cityreportsystem.model.ReportType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReportTypeDAO extends JpaRepository<ReportType,Long> {
    ReportType getReportTypeByName(String name);
}
