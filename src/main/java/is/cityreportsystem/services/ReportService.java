package is.cityreportsystem.services;

import is.cityreportsystem.model.DTO.EventDTO;
import is.cityreportsystem.model.DTO.PageDTO;
import is.cityreportsystem.model.DTO.ReportDTO;
import is.cityreportsystem.model.DTO.ReportRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ReportService {
    public boolean saveImage(byte[] data, String id);
    public ReportDTO createReport(ReportRequest report);
    List<ReportDTO> getReportsByAuthor(long id);
    void deleteImage(String id);
    boolean requireInfo(long id, String required);
    boolean provideInfo(long id, String required);
    boolean addFeedback(long id,String feedback);
    boolean changeState(long user,long id,String state);
    PageDTO<ReportDTO> getReports(long userId,long departmentId,Pageable pageable, String search, String typeFilter, String stateFilter);
}
