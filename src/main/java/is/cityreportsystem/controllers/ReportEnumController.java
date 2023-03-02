package is.cityreportsystem.controllers;

import is.cityreportsystem.model.enums.ReportState;
import is.cityreportsystem.model.enums.ReportType;
import is.cityreportsystem.services.ReportTypeService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/reports")
public class ReportEnumController {
    private final ReportTypeService reportTypeService;

    public ReportEnumController(ReportTypeService reportTypeService) {
        this.reportTypeService = reportTypeService;
    }

    @GetMapping("/types")
    public ResponseEntity<?> getReportTypes() {
        return new ResponseEntity<>(reportTypeService.getAllTypes(), HttpStatus.OK);
    }
    @GetMapping("/types/{id}")
    public ResponseEntity<?> getReportTypesByDepartment(@PathVariable("id") long id) {
        return new ResponseEntity<>(reportTypeService.getAllTypesByDepartment(id), HttpStatus.OK);
    }
    @GetMapping("/states")
    public ResponseEntity<?> getReportStates() {
        List<String> result = Arrays.stream(ReportState.values()).map((ReportState type)->{return type.name();}).toList();
        return new ResponseEntity<>(result, HttpStatus.OK);
    }
}
