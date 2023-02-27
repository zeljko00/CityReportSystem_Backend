package is.cityreportsystem.controllers;

import is.cityreportsystem.model.DTO.EventDTO;
import is.cityreportsystem.model.DTO.EventRequest;
import is.cityreportsystem.model.DTO.PageDTO;
import is.cityreportsystem.services.EventImageService;
import is.cityreportsystem.services.EventService;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;

@RestController
@RequestMapping("/events")
public class EventController {

    private final EventImageService eventImageService;
    private final EventService eventService;
    private DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public EventController(EventService eventService, EventImageService eventImageService) {
        super();
        this.eventService = eventService;
        this.eventImageService = eventImageService;
    }

    @GetMapping("/{typeFilter}/{stateFilter}/{departmentFilter}/{page}/{size}/{search}/{sort}/{dir}")
    public ResponseEntity<?> getAllEvents(@PathVariable("typeFilter") String typeFilter, @PathVariable("stateFilter") String stateFilter, @PathVariable("departmentFilter") String departmentFilter, @PathVariable("page") int page, @PathVariable("size") int size, @PathVariable("search") String search, @PathVariable("dir") String direction, @PathVariable("sort") String sort) {
        Pageable pageable = PageRequest.of(page, size);
        if ("desc".equals(direction))
            pageable = PageRequest.of(page, size, Sort.by(sort).descending());
        else
            pageable = PageRequest.of(page, size, Sort.by(sort).ascending());
        System.out.println("Page - " + page + " filter: " + typeFilter + " - " + stateFilter + " - " + departmentFilter+" sort - "+sort+" "+direction);
        PageDTO result = eventService.getAllEvents(pageable, search, typeFilter, stateFilter, departmentFilter);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping("/active")
    public ResponseEntity<?> getActiveEvents() {
        System.out.println("EventControler - active events hit!");
        List<EventDTO> result = eventService.getActiveEvents();
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping(value = "/active/images/{id}", produces = MediaType.IMAGE_JPEG_VALUE)

    public ResponseEntity<byte[]> getImage(@PathVariable("id") long id) {
        System.out.println("EventControler - event images hit!");
        byte[] image = eventImageService.getImageById(id);
        if (image != null) return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(image);
        else return ResponseEntity.notFound().build();
    }

    //	@GetMapping("/{id}")
//	public ResponseEntity<?> getEvent(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorization,
//			@PathVariable("id") int id) {
//		int flag1 = checkCityOfficialCredentials(authorization);
//		if (flag1 == -1) {
//			return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
//		} else {
//			Event event = eventService.getEventById(id);
//			if (event == null)
//				return new ResponseEntity<>(HttpStatus.NO_CONTENT);
//			else
//				return new ResponseEntity<>(event, HttpStatus.OK);
//		}
//	}
//
    @PostMapping()
    public ResponseEntity<?> addEvent(@RequestBody EventRequest event) {
        EventDTO eventDTO = eventService.createEvent(event);
        if (eventDTO == null) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        } else {
            return new ResponseEntity<>(eventDTO, HttpStatus.OK);
        }
    }

    @PostMapping("/images/upload")
    public void uploadImage(Model model, @RequestParam("image") MultipartFile file, @RequestParam("identificator") String id) throws IOException {
        eventService.saveImage(file.getBytes(), id);
    }

    @PostMapping("/images/upload/{id}")
    public void updateImages(Model model, @RequestParam("image") MultipartFile file, @PathVariable("id") String id) throws IOException {
        eventService.updateImage(file.getBytes(), id);
    }

    @DeleteMapping("/images/{id}")
    public ResponseEntity<?> deleteReportImageById(@PathVariable("id") String id) {
        eventService.deleteImage(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/images/update/{id}")
    public ResponseEntity<?> deleteUpdatedImageById(@PathVariable("id") String id) {
        eventService.deleteUpdatedImage(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping("{id}")
    public ResponseEntity<?> updateEvent(@PathVariable("id") long id,
                                         @RequestBody EventDTO event) {
        EventDTO result = eventService.updateEvent(id, event);
        System.out.println("controler:" + result.getImages().size());
        if (result != null)
            return new ResponseEntity<>(result, HttpStatus.OK);
        else
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);


    }

    @DeleteMapping("/deactivate/{creator}/{id}")
    public ResponseEntity<?> deactivateEvent(@PathVariable("creator") long creator, @PathVariable("id") long id) {
        boolean flag2 = eventService.deactivateEvent(creator, id);
        if (flag2) return new ResponseEntity<>(HttpStatus.OK);
        else return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }

    @PutMapping("/activate/{creator}/{id}")
    public ResponseEntity<?> activateEvent(@PathVariable("creator") long creator, @PathVariable("id") long id) {
        boolean flag2 = eventService.activateEvent(creator, id);
        if (flag2) return new ResponseEntity<>(HttpStatus.OK);
        else return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }

//
//	private int checkCityOfficialCredentials(String authorization) {
//		Decoder decoder = Base64.getDecoder();
//		try {
//			String[] tokens = authorization.split(" ");
//			byte[] data = tokens[1].getBytes();
//			byte[] decodedData = decoder.decode(data);
//			String credentials = new String(decodedData);
//			tokens = credentials.split(":");
//			String username = tokens[0];
//			String password = tokens[1];
//			int flag2 = cityOfficialService.checkCredentials(username, password);
//			if (flag2 > 0)
//				return flag2;
//			else
//				return -1;
//		} catch (Exception e) {
//			return -1;
//		}
//	}
//
//	private int checkCitizenCredentials(String authorization) {
//		Decoder decoder = Base64.getDecoder();
//		try {
//			String[] tokens = authorization.split(" ");
//			byte[] data = tokens[1].getBytes();
//			byte[] decodedData = decoder.decode(data);
//			String credentials = new String(decodedData);
//			tokens = credentials.split(":");
//			String username = tokens[0];
//			String password = tokens[1];
//			int flag2 = citizenService.checkCredentials(username, password);
//			if (flag2 > 0)
//				return flag2;
//			else
//				return -1;
//		} catch (Exception e) {
//			return -1;
//		}
//	}

}