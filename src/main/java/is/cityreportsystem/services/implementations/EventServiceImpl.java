package is.cityreportsystem.services.implementations;

import is.cityreportsystem.DAO.EventDAO;
import is.cityreportsystem.model.DTO.EventDTO;
import is.cityreportsystem.model.Event;
import is.cityreportsystem.services.EventService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class EventServiceImpl implements EventService {

    private EventDAO eventDAO;
    private ModelMapper modelMapper;

    public EventServiceImpl(EventDAO eventDAO, ModelMapper modelMapper) {
        super();
        this.eventDAO = eventDAO;
        this.modelMapper = modelMapper;
    }

    public List<EventDTO> getActiveEvents() {
        List<Event> events = eventDAO.findEventsByActive((short)1);
        System.out.println("Service: "+events.size());
        List<EventDTO> result = new ArrayList<EventDTO>();

        for (Event e : events)
                result.add(modelMapper.map(e, EventDTO.class));
        return result;
    }

    public List<EventDTO> getAllEvents() {
        List<Event> events = eventDAO.findAll();
        List<EventDTO> result = new ArrayList<EventDTO>();

        for (Event e : events)
            result.add(modelMapper.map(e, EventDTO.class));
        return result;
    }

//    public Event getEventById(int id) {
//        return eventRepository.getById(id);
//    }
//
//    public int addEvent(EventRequest e) {
//        Event event=modelMapper.map(e, Event.class);
//        DateFormat form=new SimpleDateFormat("dd.MM.yyyy HH:mm");
//        event.setDate(form.format(new Date()));
//        event.setActive((short)1);
//        event.setEventCreator(modelMapper.map(cityOfficial.getCityOfficialById(e.getCreator()),CityOfficial.class));
//        int id = eventRepository.saveAndFlush(event).getId();
//        return id;
//    }

//	public boolean updateEvent(int id, Event e) {
//		Event event = eventRepository.getById(id);
//		if (event != null) {
//			event.setInfo(e.getInfo());
//			event.setDescription(e.getDescription());
//			eventRepository.saveAndFlush(event);
//			return true;
//		} else
//			return false;
//	}
//
//
//	public boolean deactivateEvent(int id) {
//		Event event = eventRepository.getById(id);
//		if (event != null) {
//			event.setActive((short)0);;
//			eventRepository.saveAndFlush(event);
//			return true;
//		} else
//			return false;
//	}

//    public boolean updateEvent(int id, Event e,int executorId) {
//        Event event = eventRepository.getById(id);
//        if (event != null) {
//            EventProxy proxy=new EventProxy(event);
//            boolean flag=proxy.tryUpdateEvent(e,executorId);
//            if(flag)
//                eventRepository.saveAndFlush(event);
//            return flag;
//        } else
//            return false;
//    }
//
//
//    public boolean deactivateEvent(int id,int executorId) {
//        Event event = eventRepository.getById(id);
//        if (event != null) {
//            EventProxy proxy=new EventProxy(event);
//            boolean flag=proxy.tryDeactivateEvent(executorId);
//            if(flag)
//                eventRepository.saveAndFlush(event);
//            return flag;
//        } else
//            return false;
//    }


}
