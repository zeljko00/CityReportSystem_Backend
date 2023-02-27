package is.cityreportsystem.controllers;

import is.cityreportsystem.services.CityServiceService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/cityServices")
public class CityServiceController {
    private final CityServiceService cityServiceService;

    public CityServiceController(CityServiceService cityServiceService) {
        this.cityServiceService = cityServiceService;
    }
    @GetMapping
    public ResponseEntity<?> getCityServiceNames(){
        return new ResponseEntity<>(cityServiceService.getCityServiceNames(), HttpStatus.OK);
    }
}
