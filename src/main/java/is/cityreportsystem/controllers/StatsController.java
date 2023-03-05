package is.cityreportsystem.controllers;

import is.cityreportsystem.model.DTO.Stats;
import is.cityreportsystem.services.StatsService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/statistics")
public class StatsController {
    private final StatsService statsService;

    public StatsController(StatsService statsService) {
        this.statsService = statsService;
    }

    @GetMapping("/{startDate}/{endDate}/{type}")
    public ResponseEntity<Stats> getStats(@PathVariable("startDate") String startDate, @PathVariable("endDate") String endDate, @PathVariable("type") String type) {
        Stats result=statsService.getStats(startDate,endDate,type);
        if(result!=null)
        return new ResponseEntity<>(result, HttpStatus.OK);
        else
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }
}
