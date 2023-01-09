package is.cityreportsystem.controllers;

import is.cityreportsystem.model.DTO.ReportDTO;
import is.cityreportsystem.model.DTO.ReportRequest;
import is.cityreportsystem.services.ReportService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/reports")
public class ReportController {
    private final ReportService reportService;

    public ReportController(ReportService reportService) {
        this.reportService = reportService;
    }

    @PostMapping("/images/upload")
    public void uploadImage(Model model, @RequestParam("image") MultipartFile file,@RequestParam("identificator") int id) throws IOException {
        System.out.println("Image upload hit! + id="+id);
        reportService.saveImage(file.getBytes(),id);
    }
    @PostMapping
    public ResponseEntity<?> postReport(@RequestBody ReportRequest report){
            try{
                ReportDTO result=reportService.createReport(report);
                return new ResponseEntity<>(result,HttpStatus.OK);
            }catch(Exception e){
                e.printStackTrace();
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
    }
    @GetMapping("/author/{id}")
    public ResponseEntity<?> getReportsByAuthor(@PathVariable("id") long id){
        return new ResponseEntity<List<ReportDTO>>(reportService.getReportsByAuthor(id),HttpStatus.OK);
    }

}
