package is.cityreportsystem.controllers;

import java.util.Base64;
import java.util.Base64.Decoder;

import is.cityreportsystem.model.DTO.CitizenDTO;
import is.cityreportsystem.services.CitizenService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/citizens")
public class CitizenController {
	private CitizenService citizenService;

	private Decoder decoder;

	public CitizenController(CitizenService citizenService) {
		this.citizenService = citizenService;

		decoder = Base64.getDecoder();
	}

	@CrossOrigin
	@GetMapping("/login")
	public ResponseEntity<CitizenDTO> login(@RequestHeader(HttpHeaders.AUTHORIZATION) String auth){

		String[] tokens=auth.split(" ");
		System.out.println("username:password"+tokens[1]);
		byte[] data = tokens[1].getBytes();
		byte[] decodedData = decoder.decode(data);
		String credentials = new String(decodedData);
		tokens = credentials.split(":");
		String username = tokens[0];
		String password = tokens[1];
		System.out.println(password);
		CitizenDTO citizen=citizenService.login(username,password);
		if(citizen!=null)
			return new ResponseEntity<>(citizen,HttpStatus.OK);
		else
			return new ResponseEntity<>(null,HttpStatus.UNAUTHORIZED);
	}

	@CrossOrigin
	@PostMapping()
	public ResponseEntity<CitizenDTO> createCitizen(@RequestBody CitizenDTO c) {
		System.out.println(c);
		if (citizenService.checkIfIdCardIsUsed(c.getIdCard()))
			return new ResponseEntity<>(HttpStatus.FORBIDDEN);
		CitizenDTO result= citizenService.createCitizen(c);
			return new ResponseEntity<CitizenDTO>(result,HttpStatus.CREATED);
	}
}
