package is.cityreportsystem.DAO;

import is.cityreportsystem.model.ReportImage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface  ReportImageDAO extends JpaRepository<ReportImage,Long> {
}
