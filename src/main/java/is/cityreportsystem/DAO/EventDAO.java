package is.cityreportsystem.DAO;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import is.cityreportsystem.model.Event;

import java.util.List;

@Repository
public interface EventDAO extends  JpaRepository<Event, Long>{
    List<Event> findEventsByActive(boolean active);
    Page<Event> findAll(Pageable pageable);
    @Query("SELECT COUNT(e.id) FROM Event e WHERE e.creator.id=:id")
    int getEventsByCreatorId(long id);
    @Query("SELECT COUNT(e.id) FROM Event e WHERE e.creator.id=:id AND e.active=true")
    int getActiveEventsByCreatorId(long id);
}
