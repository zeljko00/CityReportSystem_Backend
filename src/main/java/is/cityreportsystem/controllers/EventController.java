package is.cityreportsystem.controllers;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import is.cityreportsystem.model.DTO.EventDTO;
import is.cityreportsystem.services.EventImageService;
import is.cityreportsystem.services.EventService;
import org.apache.tomcat.util.http.fileupload.FileUtils;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

	@GetMapping()
	public ResponseEntity<?> getAllEvents() {
		List<EventDTO> result = eventService.getAllEvents();
		return new ResponseEntity<>(result, HttpStatus.OK);
	}

	@GetMapping("/active")
	public ResponseEntity<?> getActiveEvents() {
		System.out.println("EventControler /active hit!");
		List<EventDTO> result = eventService.getActiveEvents();
		System.out.println(result.size());
		return new ResponseEntity<>(result, HttpStatus.OK);
	}
	@GetMapping(value= "/active/images/{id}", produces = MediaType.IMAGE_JPEG_VALUE)

	public ResponseEntity<byte[]> getImage(@PathVariable("id")long id) {
		System.out.println("Images hit!");
		byte[] image = eventImageService.getImageById(id);
		if(image!=null)
			return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(image);
		else
			return  ResponseEntity.notFound().build();
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
//	@PostMapping()
//	public ResponseEntity<?> addEvent(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorization,
//			@RequestBody EventRequest event) {
//		int flag = checkCityOfficialCredentials(authorization);
//		if (flag == -1) {
//			return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
//		} else {
//			int id = eventService.addEvent(event);
//			return new ResponseEntity<>(id, HttpStatus.OK);
//		}
//
//	}
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
//	@CrossOrigin
//	@PutMapping("/deactivate/{id}")
//	public ResponseEntity<?> deactivateEvent(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorization, @PathVariable("id") int id) {
//		int flag = checkCityOfficialCredentials(authorization);
//		if (flag == -1) {
//			return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
//		} else {
//			System.out.println("????????????????????????????????????");
//			boolean flag2 = eventService.deactivateEvent(id,flag);
//			if (flag2)
//				return new ResponseEntity<>(HttpStatus.OK);
//			else
//				return new ResponseEntity<>(HttpStatus.NOT_FOUND);
//		}
//	}
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
