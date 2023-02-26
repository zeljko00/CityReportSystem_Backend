package is.cityreportsystem.services;

import is.cityreportsystem.model.DTO.EventDTO;
import is.cityreportsystem.model.DTO.EventRequest;
import is.cityreportsystem.model.DTO.PageDTO;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface EventService {
    List<EventDTO> getActiveEvents();
    PageDTO<EventDTO> getAllEvents(Pageable pageable,String search);
     int getActiveEventsByCreatorId(long id);
     int getEventsByCreatorId(long id);
//    Event getEventById(int id);
    EventDTO createEvent(EventRequest event);
//    boolean updateEvent(int id,Event e,int executorId);
    boolean deactivateEvent(long executorId,long id);
    boolean activateEvent(long executorId,long id);
}
