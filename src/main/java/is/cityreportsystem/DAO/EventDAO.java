package is.cityreportsystem.DAO;

import is.cityreportsystem.model.enums.EventType;
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
    Page<Event> findEventsByTitleContainsIgnoreCase(Pageable pageable,String search);
    Page<Event> findEventsByActive(Pageable pageable,boolean active);
    Page<Event> findEventsByType(Pageable pageable, EventType type);
    Page<Event> findEventsByCreatorDepartmentName(Pageable pageable,String department);

    Page<Event> findEventsByCreatorDepartmentNameAndActive(Pageable pageable,String department,boolean active);
    Page<Event> findEventsByCreatorDepartmentNameAndType(Pageable pageable,String department,EventType type);
    Page<Event> findEventsByCreatorDepartmentNameAndTitleContainsIgnoreCase(Pageable pageable,String department,String search);

    Page<Event> findEventsByActiveAndTitleContainsIgnoreCase(Pageable pageable,boolean active,String search);
    Page<Event> findEventsByActiveAndType(Pageable pageable,boolean active,EventType type);
    Page<Event> findEventsByTypeAndTitleContainsIgnoreCase(Pageable pageable, EventType type,String search);

    Page<Event> findEventsByCreatorDepartmentNameAndActiveAndType(Pageable pageable,String department,boolean active,EventType type);
    Page<Event> findEventsByCreatorDepartmentNameAndActiveAndTitleContainsIgnoreCase(Pageable pageable,String department,boolean active,String search);
    Page<Event> findEventsByCreatorDepartmentNameAndTypeAndTitleContainsIgnoreCase(Pageable pageable,String department,EventType type,String search);
    Page<Event> findEventsByTypeAndActiveAndTitleContainsIgnoreCase(Pageable pageable,EventType type,boolean active,String search);

    Page<Event> findEventsByCreatorDepartmentNameAndActiveAndTypeAndTitleContainsIgnoreCase(Pageable pageable,String department,boolean active,EventType type,String search);
    @Query("SELECT COUNT(e.id) FROM Event e WHERE e.creator.id=:id")
    int getEventsByCreatorId(long id);
    @Query("SELECT COUNT(e.id) FROM Event e WHERE e.creator.id=:id AND e.active=true")
    int getActiveEventsByCreatorId(long id);
}
