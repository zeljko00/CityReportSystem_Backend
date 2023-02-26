package is.cityreportsystem.services.implementations;

import is.cityreportsystem.DAO.CityOfficialDAO;
import is.cityreportsystem.DAO.EventDAO;
import is.cityreportsystem.model.CityOfficial;
import is.cityreportsystem.model.DTO.EventDTO;
import is.cityreportsystem.model.DTO.EventRequest;
import is.cityreportsystem.model.DTO.PageDTO;
import is.cityreportsystem.model.Event;
import is.cityreportsystem.services.EventService;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class EventServiceImpl implements EventService {

    private EventDAO eventDAO;
    private final CityOfficialDAO cityOfficialDAO;
    private ModelMapper modelMapper;
    private DateFormat dateFormat=new SimpleDateFormat("dd.MM.yyyy hh:mm:ss");

    public EventServiceImpl(EventDAO eventDAO, CityOfficialDAO cityOfficialDAO, ModelMapper modelMapper) {
        super();
        this.eventDAO = eventDAO;
        this.cityOfficialDAO = cityOfficialDAO;
        this.modelMapper = modelMapper;
    }

    public List<EventDTO> getActiveEvents() {
        List<Event> events = eventDAO.findEventsByActive(true);
        List<EventDTO> result = new ArrayList<EventDTO>();
        for (Event e : events)
            result.add(modelMapper.map(e, EventDTO.class));
        return result;
    }

    public PageDTO<EventDTO> getAllEvents(Pageable pageable,String search) {
        Page<Event> page = null;
        if("-".equals(search))
            page=eventDAO.findAll(pageable);
        else
            page=eventDAO.findEventsByTitleContains(pageable,search);
        PageDTO<EventDTO> result = new PageDTO<>();
        result.setPages(page.getTotalElements());
        result.setData(page.getContent().stream().map(this::map).collect(Collectors.toList()));
        return result;
    }

    private EventDTO map(Event e) {
        return modelMapper.map(e, EventDTO.class);
    }

    public int getActiveEventsByCreatorId(long id) {
        int temp=eventDAO.getActiveEventsByCreatorId(id);
        System.out.println(temp);
        return temp;
    }

    public int getEventsByCreatorId(long id) {
        return eventDAO.getEventsByCreatorId(id);
    }
//    public Event getEventById(int id) {
//        return eventRepository.getById(id);
//    }
//
    public EventDTO createEvent(EventRequest e) {

        try{
            CityOfficial cityOfficial=cityOfficialDAO.findById(e.getCreator()).get();
            Event event=modelMapper.map(e, Event.class);
            int random=e.getRandom();
            event.setCreator(cityOfficial);
            event.setActive(true);
            event.setDate(dateFormat.format(new Date()));
            EventDTO result=modelMapper.map(eventDAO.saveAndFlush(event),EventDTO.class);
            if(result.getImages()==null)
                result.setImages(new ArrayList<>());
            return result;
        }catch(Exception ex){
            ex.printStackTrace();
            return null;
        }
    }

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
    public boolean deactivateEvent(long executorId, long id) {
        try {
            Event event = eventDAO.findById(id).get();
            CityOfficial cityOfficial = cityOfficialDAO.findById(executorId).get();
            if (event.getCreator().getId() == cityOfficial.getId()) {
                event.setActive(false);
                eventDAO.saveAndFlush(event);
                return true;
            } else
                return false;
        } catch (Exception e) {
            return false;
        }
    }
    public boolean activateEvent(long executorId, long id) {
        try {
            Event event = eventDAO.findById(id).get();
            CityOfficial cityOfficial = cityOfficialDAO.findById(executorId).get();
            if (event.getCreator().getId() == cityOfficial.getId()) {
                event.setActive(true);
                eventDAO.saveAndFlush(event);
                return true;
            } else
                return false;
        } catch (Exception e) {
            return false;
        }
    }

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
