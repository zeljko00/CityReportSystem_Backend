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


	public CitizenController(CitizenService citizenService) {
		this.citizenService = citizenService;

	}
}
