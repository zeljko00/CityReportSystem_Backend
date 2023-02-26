package is.cityreportsystem.controllers;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import is.cityreportsystem.model.DTO.EventDTO;
import is.cityreportsystem.model.DTO.EventRequest;
import is.cityreportsystem.model.DTO.PageDTO;
import is.cityreportsystem.services.EventImageService;
import is.cityreportsystem.services.EventService;
import org.apache.tomcat.util.http.fileupload.FileUtils;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/events")
public class EventController {

    private EventService eventService;
    private final EventImageService eventImageService;

    public EventController(EventService eventService, EventImageService eventImageService) {
        super();
        this.eventService = eventService;
        this.eventImageService = eventImageService;
    }

    @GetMapping("/{page}/{size}/{search}")
    public ResponseEntity<?> getAllEvents(@PathVariable("page") int page, @PathVariable("size") int size, @PathVariable("search") String search) {
        Pageable pageable = PageRequest.of(page, size);
        System.out.println("Page - " + page);
        PageDTO result = eventService.getAllEvents(pageable, search);
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
        if (eventDTO==null) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        } else {
            return new ResponseEntity<>(eventDTO, HttpStatus.OK);
        }
    }

    //
//	@CrossOrigin
//	@PutMapping("/{id}")
//	public ResponseEntity<?> updateEvent(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorization,
//			@RequestBody Event event, @PathVariable("id") int id) {
//		int flag = checkCityOfficialCredentials(authorization);
//		if (flag == -1) {
//			return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
//		} else {
//			boolean flag2 = eventService.updateEvent(id, event,flag);
//			if (flag2)
//				return new ResponseEntity<>(HttpStatus.OK);
//			else
//				return new ResponseEntity<>(HttpStatus.NOT_FOUND);
//		}
//
//	}
//
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