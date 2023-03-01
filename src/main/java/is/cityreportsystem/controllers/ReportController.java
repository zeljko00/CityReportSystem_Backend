package is.cityreportsystem.controllers;

import is.cityreportsystem.model.DTO.PageDTO;
import is.cityreportsystem.model.DTO.ReportDTO;
import is.cityreportsystem.model.DTO.ReportRequest;
import is.cityreportsystem.services.ReportImageService;
import is.cityreportsystem.services.ReportService;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/reports")
public class ReportController {
    private final ReportService reportService;
    private final ReportImageService reportImageService;

    public ReportController(ReportService reportService, ReportImageService reportImageService) {
        this.reportService = reportService;
        this.reportImageService = reportImageService;
    }

    @PostMapping("/images/upload")
    public void uploadImage(Model model, @RequestParam("image") MultipartFile file,@RequestParam("identificator") String id) throws IOException {
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
    @PutMapping("/{id}")
    public ResponseEntity<?> requireAddtionalInfo(@PathVariable("id") long id, @RequestBody String required){
        boolean result=reportService.requireInfo(id,required.replace("\"",""));
        if(result)
            return new ResponseEntity<>(HttpStatus.OK);
        else
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
    @PutMapping("/additionalInfo/{id}")
    public ResponseEntity<?> provideAddtionalInfo(@PathVariable("id") long id, @RequestBody String required){
        boolean result=reportService.provideInfo(id,required.replace("\"",""));
        if(result)
            return new ResponseEntity<>(HttpStatus.OK);
        else
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
    @GetMapping("/author/{id}")
    public ResponseEntity<?> getReportsByAuthor(@PathVariable("id") long id){
        return new ResponseEntity<List<ReportDTO>>(reportService.getReportsByAuthor(id),HttpStatus.OK);
    }
    @GetMapping(path="/images/{id}", produces = MediaType.IMAGE_JPEG_VALUE)
    public ResponseEntity<?> getReportImageById(@PathVariable("id") long id){
        byte[] result=reportImageService.getImageById(id);
        if(result!=null)
            return new ResponseEntity<>(result,HttpStatus.OK);
        else
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
    @DeleteMapping("/images/{id}")
    public ResponseEntity<?> deleteReportImageById(@PathVariable("id") String id){
        reportService.deleteImage(id);
            return new ResponseEntity<>(HttpStatus.OK);
    }
    @GetMapping("/{userId}/{departmentId}/{typeFilter}/{stateFilter}/{page}/{size}/{search}/{sort}/{dir}")
    public ResponseEntity<?> getReports(@PathVariable("userId") long userId, @PathVariable("departmentId") long departmentId,@PathVariable("typeFilter") String typeFilter, @PathVariable("stateFilter") String stateFilter, @PathVariable("page") int page, @PathVariable("size") int size, @PathVariable("search") String search, @PathVariable("dir") String direction, @PathVariable("sort") String sort){
        Pageable pageable = PageRequest.of(page, size);
        if ("desc".equals(direction))
            pageable = PageRequest.of(page, size, Sort.by(sort).descending());
        else
            pageable = PageRequest.of(page, size, Sort.by(sort).ascending());
        PageDTO result = reportService.getReports(userId,departmentId,pageable, search, typeFilter, stateFilter);
        if(result!=null)
            return new ResponseEntity<>(result, HttpStatus.OK);
        else
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }

}
