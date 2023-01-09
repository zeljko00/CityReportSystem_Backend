package is.cityreportsystem.services.implementations;

import is.cityreportsystem.DAO.ReportImageDAO;
import is.cityreportsystem.model.ReportImage;
import is.cityreportsystem.services.ReportImageService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

@Transactional
@Service
public class ReportImageServiceImpl implements ReportImageService {
    ReportImageDAO reportImageDAO;

    public ReportImageServiceImpl(ReportImageDAO reportImageDAO){
        this.reportImageDAO=reportImageDAO;
    }
    public void addImage(ReportImage reportImage){
        reportImageDAO.save(reportImage);
    }
}
