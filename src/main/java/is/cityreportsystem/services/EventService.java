package is.cityreportsystem.services;

import is.cityreportsystem.model.DTO.EventDTO;

import java.util.List;

public interface EventService {
    List<EventDTO> getActiveEvents();
    List<EventDTO> getAllEvents();
//    Event getEventById(int id);
//    int addEvent(EventRequest event);
//    boolean updateEvent(int id,Event e,int executorId);
//    boolean deactivateEvent(int id,int executorId);
}
