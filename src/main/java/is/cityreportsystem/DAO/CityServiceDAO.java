package is.cityreportsystem.DAO;

import is.cityreportsystem.model.CityService;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CityServiceDAO extends JpaRepository<CityService,Long> {
    CityService getCityServiceByName(String name);
}
