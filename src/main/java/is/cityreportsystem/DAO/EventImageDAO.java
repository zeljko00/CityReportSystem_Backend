package is.cityreportsystem.DAO;

import is.cityreportsystem.model.EventImage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EventImageDAO extends JpaRepository<EventImage, Long> {
}
