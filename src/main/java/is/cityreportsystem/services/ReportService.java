package is.cityreportsystem.services;

import is.cityreportsystem.model.DTO.ReportDTO;
import is.cityreportsystem.model.DTO.ReportRequest;

import java.util.List;

public interface ReportService {
    public boolean saveImage(byte[] data, String id);
    public ReportDTO createReport(ReportRequest report);
    List<ReportDTO> getReportsByAuthor(long id);
    void deleteImage(String id);
    boolean requireInfo(long id, String required);
    boolean provideInfo(long id, String required);
}
