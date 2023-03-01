package is.cityreportsystem.DAO;

import is.cityreportsystem.model.Coordinate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CoordinateDAO  extends JpaRepository<Coordinate,Long> {
    void deleteCoordinatesByEventId(long id);
}
