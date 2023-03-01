package is.cityreportsystem.services.implementations;

import is.cityreportsystem.DAO.CityOfficialDAO;
import is.cityreportsystem.DAO.CoordinateDAO;
import is.cityreportsystem.DAO.EventDAO;
import is.cityreportsystem.model.*;
import is.cityreportsystem.model.DTO.*;
import is.cityreportsystem.model.enums.EventType;
import is.cityreportsystem.services.EventImageService;
import is.cityreportsystem.services.EventService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
public class EventServiceImpl implements EventService {
    @Value("${events.images.repository}")
    private String imagesRepo;
    private EventDAO eventDAO;
    private final CityOfficialDAO cityOfficialDAO;
    private final EventImageService eventImageService;
    private final CoordinateDAO coordinateDAO;
    private ModelMapper modelMapper;
    private DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private DateFormat dateFormatLocale = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
    private HashMap<String, List<Tuple>> uploadedImages = new HashMap<String, List<Tuple>>();
    private HashMap<String, List<Tuple>> updatedImages = new HashMap<String, List<Tuple>>();

    public EventServiceImpl(EventDAO eventDAO, CityOfficialDAO cityOfficialDAO, EventImageService eventImageService, CoordinateDAO coordinateDAO, ModelMapper modelMapper) {
        super();
        this.eventDAO = eventDAO;
        this.cityOfficialDAO = cityOfficialDAO;
        this.eventImageService = eventImageService;
        this.coordinateDAO = coordinateDAO;
        this.modelMapper = modelMapper;
    }

    public List<EventDTO> getActiveEvents() {
        List<Event> events = eventDAO.findEventsByActive(true);
        List<EventDTO> result = new ArrayList<EventDTO>();
        for (Event e : events)
            result.add(map(e));
        return result;
    }
//    public int eventDateComparator(Event event){
//        String date="2000-01-01 00:00:00";
//        try{
//        return (int)(dateFormat.parse(event.getDate()).getTime()- dateFormat.parse(date).getTime());}
//        catch (Exception e){
//            e.printStackTrace();
//            return 0;
//        }
//    }

    public PageDTO<EventDTO> getAllEvents(Pageable pageable, String search, String typeFilter, String stateFilter, String departmentFilter) {
        Page<Event> page = null;
        if ("-".equals(search)) {
            if ("all".equals(typeFilter)) {
                if ("all".equals(stateFilter)) {
                    if ("all".equals(departmentFilter))
                        page = eventDAO.findAll(pageable);
                    else
                        page = eventDAO.findEventsByCreatorDepartmentName(pageable, departmentFilter);
                } else {
                    if ("true".equals(stateFilter)) {
                        if ("all".equals(departmentFilter))
                            page = eventDAO.findEventsByActive(pageable, true);
                        else
                            page = eventDAO.findEventsByCreatorDepartmentNameAndActive(pageable, departmentFilter, true);
                    } else {
                        if ("all".equals(departmentFilter))
                            page = eventDAO.findEventsByActive(pageable, false);
                        else
                            page = eventDAO.findEventsByCreatorDepartmentNameAndActive(pageable, departmentFilter, false);

                    }
                }
            } else {
                if ("all".equals(stateFilter)) {
                    if ("all".equals(departmentFilter)) {
                        try {
                            page = eventDAO.findEventsByType(pageable, EventType.valueOf(typeFilter));
                        } catch (Exception e) {
                            page = eventDAO.findAll(pageable);
                        }
                    } else {
                        try {
                            page = eventDAO.findEventsByCreatorDepartmentNameAndType(pageable, departmentFilter, EventType.valueOf(typeFilter));
                        } catch (Exception e) {
                            page = eventDAO.findEventsByCreatorDepartmentName(pageable, departmentFilter);
                        }
                    }
                } else {
                    if ("true".equals(stateFilter)) {
                        if ("all".equals(departmentFilter)) {
                            try {
                                page = eventDAO.findEventsByActiveAndType(pageable, true, EventType.valueOf(typeFilter));
                            } catch (Exception e) {
                                page = eventDAO.findEventsByActive(pageable, true);
                            }
                        } else {
                            try {
                                page = eventDAO.findEventsByCreatorDepartmentNameAndActiveAndType(pageable, departmentFilter, true, EventType.valueOf(typeFilter));
                            } catch (Exception e) {
                                page = eventDAO.findEventsByCreatorDepartmentNameAndActive(pageable, departmentFilter, true);
                            }
                        }
                    } else {
                        if ("all".equals(departmentFilter)) {
                            try {
                                page = eventDAO.findEventsByActiveAndType(pageable, false, EventType.valueOf(typeFilter));
                            } catch (Exception e) {
                                page = eventDAO.findEventsByActive(pageable, false);
                            }
                        } else {
                            try {
                                page = eventDAO.findEventsByCreatorDepartmentNameAndActiveAndType(pageable, departmentFilter, false, EventType.valueOf(typeFilter));
                            } catch (Exception e) {
                                page = eventDAO.findEventsByCreatorDepartmentNameAndActive(pageable, departmentFilter, false);
                            }
                        }
                    }
                }
            }
        } else {
            if ("all".equals(typeFilter)) {
                if ("all".equals(stateFilter)) {
                    if ("all".equals(departmentFilter))
                        page = eventDAO.findEventsByTitleContainsIgnoreCase(pageable, search);
                    else
                        page = eventDAO.findEventsByCreatorDepartmentNameAndTitleContainsIgnoreCase(pageable, departmentFilter, search);
                } else {
                    if ("true".equals(stateFilter)) {
                        if ("all".equals(departmentFilter))
                            page = eventDAO.findEventsByActiveAndTitleContainsIgnoreCase(pageable, true, search);
                        else
                            page = eventDAO.findEventsByCreatorDepartmentNameAndActiveAndTitleContainsIgnoreCase(pageable, departmentFilter, true, search);
                    } else {
                        if ("all".equals(departmentFilter))
                            page = eventDAO.findEventsByActiveAndTitleContainsIgnoreCase(pageable, false, search);
                        else
                            page = eventDAO.findEventsByCreatorDepartmentNameAndActiveAndTitleContainsIgnoreCase(pageable, departmentFilter, false, search);

                    }
                }
            } else {
                if ("all".equals(stateFilter)) {
                    if ("all".equals(departmentFilter)) {
                        try {
                            page = eventDAO.findEventsByTypeAndTitleContainsIgnoreCase(pageable, EventType.valueOf(typeFilter), search);
                        } catch (Exception e) {
                            page = eventDAO.findEventsByTitleContainsIgnoreCase(pageable, search);
                        }
                    } else {
                        try {
                            page = eventDAO.findEventsByCreatorDepartmentNameAndTypeAndTitleContainsIgnoreCase(pageable, departmentFilter, EventType.valueOf(typeFilter), search);
                        } catch (Exception e) {
                            page = eventDAO.findEventsByCreatorDepartmentNameAndTitleContainsIgnoreCase(pageable, departmentFilter, search);
                        }
                    }
                } else {
                    if ("true".equals(stateFilter)) {
                        if ("all".equals(departmentFilter)) {
                            try {
                                page = eventDAO.findEventsByTypeAndActiveAndTitleContainsIgnoreCase(pageable, EventType.valueOf(typeFilter), true, search);
                            } catch (Exception e) {
                                page = eventDAO.findEventsByActiveAndTitleContainsIgnoreCase(pageable, true, search);
                            }
                        } else {
                            try {
                                page = eventDAO.findEventsByCreatorDepartmentNameAndActiveAndTypeAndTitleContainsIgnoreCase(pageable, departmentFilter, true, EventType.valueOf(typeFilter), search);
                            } catch (Exception e) {
                                page = eventDAO.findEventsByCreatorDepartmentNameAndActiveAndTitleContainsIgnoreCase(pageable, departmentFilter, true, search);
                            }
                        }
                    } else {
                        if ("all".equals(departmentFilter)) {
                            try {
                                page = eventDAO.findEventsByTypeAndActiveAndTitleContainsIgnoreCase(pageable, EventType.valueOf(typeFilter), false, search);
                            } catch (Exception e) {
                                page = eventDAO.findEventsByActiveAndTitleContainsIgnoreCase(pageable, false, search);
                            }
                        } else {
                            try {
                                page = eventDAO.findEventsByCreatorDepartmentNameAndActiveAndTypeAndTitleContainsIgnoreCase(pageable, departmentFilter, false, EventType.valueOf(typeFilter), search);
                            } catch (Exception e) {
                                page = eventDAO.findEventsByCreatorDepartmentNameAndActiveAndTitleContainsIgnoreCase(pageable, departmentFilter, false, search);
                            }
                        }
                    }
                }
            }
        }

        PageDTO<EventDTO> result = new PageDTO<>();
        result.setPages(page.getTotalElements());
        result.setData(page.getContent().

                stream().

                map(this::map).

                collect(Collectors.toList()));
        return result;
    }

    private EventDTO map(Event e) {
        List<Coordinate> list= e.getCoords();
        e.setCoords(null);
        EventDTO temp = modelMapper.map(e, EventDTO.class);

        Map<Integer,List<CoordinateDTO>> collection=new HashMap<>();
        list.stream().forEach(c -> {
            if(collection.containsKey(c.getArea())==false)
                collection.put(c.getArea(),new ArrayList<>());
            collection.get(c.getArea()).add(modelMapper.map(c,CoordinateDTO.class));
        });
        temp.setCoords(new ArrayList<>(collection.values()));
        System.out.println("areas: "+temp.getCoords().size());
        try {
            temp.setDate(dateFormatLocale.format(dateFormat.parse(temp.getDate())));
        } catch (Exception ex) {
        }
        return temp;
    }

    public int getActiveEventsByCreatorId(long id) {
        int temp = eventDAO.getActiveEventsByCreatorId(id);
        System.out.println(temp);
        return temp;
    }

    public int getEventsByCreatorId(long id) {
        return eventDAO.getEventsByCreatorId(id);
    }

    public void saveImage(byte[] data, String id) {
        System.out.println(id);
        try {
            String[] tokens = id.split("--");
            String random = tokens[0];
            synchronized (uploadedImages) {
                if (!uploadedImages.containsKey(random))
                    uploadedImages.put(random, new ArrayList<Tuple>());
                else
                    System.out.println("already contains");
                Tuple temp = new Tuple();
                System.out.println(tokens[1]);
                temp.setId(tokens[1]);
                temp.setData(data);
                uploadedImages.get(random).add(temp);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void updateImage(byte[] data, String id) {
        System.out.println("updating image: " + id);
        try {
            String[] tokens = id.split("--");
            String random = tokens[0];
            synchronized (updatedImages) {
                if (!updatedImages.containsKey(random))
                    updatedImages.put(random, new ArrayList<Tuple>());
                else
                    System.out.println("already contains");
                Tuple temp = new Tuple();
                System.out.println(tokens[1]);
                temp.setId(tokens[1]);
                temp.setData(data);
                updatedImages.get(random).add(temp);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void deleteImage(String id) {

        try {
            String[] tokens = id.split("--");
            String random = tokens[0];
            synchronized (uploadedImages) {
                List<Tuple> imgs = uploadedImages.get(random);
                Tuple toDelete = null;
                for (Tuple t : imgs)
                    if (t.getId().equals(tokens[1]))
                        toDelete = t;
                System.out.println("deleted " + toDelete.getId());
                imgs.remove(toDelete);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void deleteUpdatedImage(String id) {
        System.out.println("deleting image: " + id);
        try {
            String[] tokens = id.split("--");
            String random = tokens[0];
            synchronized (updatedImages) {
                List<Tuple> imgs = updatedImages.get(random);
                Tuple toDelete = null;
                for (Tuple t : imgs)
                    if (t.getId().equals(tokens[1]))
                        toDelete = t;
                System.out.println("deleted " + toDelete.getId());
                imgs.remove(toDelete);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public EventDTO createEvent(EventRequest e) {

        try {
            System.out.println("event request coords: "+e.getCoords().size());
            CityOfficial cityOfficial = cityOfficialDAO.findById(e.getCreator()).get();
            Event event = modelMapper.map(e, Event.class);
            List<Coordinate> list=event.getCoords();
            int random = e.getRandom();
            event.setCreator(cityOfficial);
            event.setActive(true);
            event.setDate(dateFormat.format(new Date()));
            Event eventEntity = eventDAO.saveAndFlush(event);
            list.stream().forEach(c->{c.setEvent(eventEntity);
            coordinateDAO.saveAndFlush(c);}
            );
            EventDTO result = map(eventEntity);
            result.setImages(new ArrayList<>());
            synchronized (uploadedImages) {
                if (uploadedImages.get(Long.toString(e.getRandom())) != null) {
                    System.out.println("Images exist!");
                    List<Tuple> images = uploadedImages.get(Long.toString(e.getRandom()));
                    uploadedImages.remove(Long.toString(e.getRandom()));
                    int count = 1;
                    for (Tuple t : images) {
                        EventImage eventImage = new EventImage();
                        eventImage.setEvent(eventEntity);
                        eventImage.setName(eventEntity.getId() + "_" + count++ + ".jpg");
                        long id = eventImageService.addImage(eventImage).getId();
                        ImageDTO image = new ImageDTO();
                        image.setId(id);
                        result.getImages().add(image);
                        String path = imagesRepo + File.separator + eventImage.getName();
                        File file = new File(path);
                        try {
                            file.createNewFile();
                            Files.write(Paths.get(path), t.getData());
                            System.out.println("saved");
                        } catch (Exception ee) {
                            ee.printStackTrace();
                        }
                    }
                } else
                    System.out.println("no images");
            }

            return result;
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public EventDTO updateEvent(long executorId, EventDTO event) {
        try {
            Event eventEntity = eventDAO.findById(event.getId()).get();
            if(event.getCoords()!=null && event.getCoords().size()>0){
                System.out.println("changing location");
                coordinateDAO.deleteCoordinatesByEventId(eventEntity.getId());
                event.getCoords().stream().forEach(area->{
                    area.forEach(c -> {Coordinate cc=modelMapper.map(c,Coordinate.class);
                        cc.setEvent(eventEntity);
                        coordinateDAO.saveAndFlush(cc);});

                });
            }
            else
                System.out.println("not changing location");

            if (event.getCreator().getId() != executorId)
                return null;
            eventEntity.setInfo(event.getInfo());
            eventEntity.setDescription(event.getDescription());
            eventEntity.setX(event.getX());
            eventEntity.setY(event.getY());
            Event resultEntity=eventDAO.saveAndFlush(eventEntity);
            if (event.getImages() == null)
                event.setImages(new ArrayList<>());
            synchronized (updatedImages) {
                if (updatedImages.get(Long.toString(event.getId())) != null) {
                    System.out.println("Images exist!");
                    List<Tuple> images = updatedImages.get(Long.toString(event.getId()));
                    updatedImages.remove(Long.toString(event.getId()));
                    int count = eventEntity.getImages().size() + 1;
                    for (Tuple t : images) {
                        EventImage eventImage = new EventImage();
                        eventImage.setEvent(eventEntity);
                        eventImage.setName(eventEntity.getId() + "_" + count++ + ".jpg");
                        long id = eventImageService.addImage(eventImage).getId();
                        ImageDTO image = new ImageDTO();
                        image.setId(id);
                        System.out.println("size " + event.getImages().size());
                        event.getImages().add(image);
                        System.out.println("size " + event.getImages().size());
                        String path = imagesRepo + File.separator + eventImage.getName();
                        File file = new File(path);
                        try {
                            file.createNewFile();
                            Files.write(Paths.get(path), t.getData());
                            System.out.println("saved");
                        } catch (Exception ee) {
                            ee.printStackTrace();
                        }
                    }
                } else
                    System.out.println("no images");
            }
            for (ImageDTO i : event.getImages())
                System.out.println(i.getId());

            return event;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


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
