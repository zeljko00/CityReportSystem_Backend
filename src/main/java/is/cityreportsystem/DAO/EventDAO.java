package is.cityreportsystem.DAO;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import is.cityreportsystem.model.Event;

import java.util.List;

@Repository
public interface EventDAO extends  JpaRepository<Event, Long>{
    public List<Event> findEventsByActive(short active);
}
