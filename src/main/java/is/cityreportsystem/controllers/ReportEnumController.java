package is.cityreportsystem.controllers;

import is.cityreportsystem.model.enums.ReportState;
import is.cityreportsystem.model.enums.ReportType;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/reports")
public class ReportEnumController {

    @GetMapping("/types")
    public ResponseEntity<?> getReportTypes() {
        List<String> result = Arrays.stream(ReportType.values()).map((ReportType type)->{return type.name();}).toList();
        return new ResponseEntity<>(result, HttpStatus.OK);
    }
    @GetMapping("/states")
    public ResponseEntity<?> getReportStates() {
        List<String> result = Arrays.stream(ReportState.values()).map((ReportState type)->{return type.name();}).toList();
        return new ResponseEntity<>(result, HttpStatus.OK);
    }
}
