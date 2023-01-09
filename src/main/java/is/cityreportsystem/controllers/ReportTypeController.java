package is.cityreportsystem.controllers;

import is.cityreportsystem.model.DTO.EventDTO;
import is.cityreportsystem.model.enums.ReportType;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/reports/types")
public class ReportTypeController {

    @GetMapping()
    public ResponseEntity<?> getAllEvents() {
        List<String> result = Arrays.stream(ReportType.values()).map((ReportType type)->{return type.name();}).toList();
        return new ResponseEntity<>(result, HttpStatus.OK);
    }
}
