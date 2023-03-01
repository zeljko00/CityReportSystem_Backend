package is.cityreportsystem.services;

import is.cityreportsystem.model.DTO.EventDTO;
import is.cityreportsystem.model.DTO.EventRequest;
import is.cityreportsystem.model.DTO.EventUpdateRequest;
import is.cityreportsystem.model.DTO.PageDTO;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface EventService {
    List<EventDTO> getActiveEvents();
    PageDTO<EventDTO> getAllEvents(Pageable pageable,String search,String typeFilter,String stateFilter,String departmentFilter);
     int getActiveEventsByCreatorId(long id);
     int getEventsByCreatorId(long id);
//    Event getEventById(int id);
    EventDTO createEvent(EventRequest event);
    EventDTO updateEvent(long executorId, EventDTO event);
    boolean deactivateEvent(long executorId,long id);
    boolean activateEvent(long executorId,long id);
    void saveImage(byte[] image, String id);
    void deleteImage(String id);

    void updateImage(byte[] image, String id);
    void deleteUpdatedImage(String id);
}
