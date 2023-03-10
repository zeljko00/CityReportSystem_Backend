package is.cityreportsystem.services.implementations;

import is.cityreportsystem.DAO.ReportImageDAO;
import is.cityreportsystem.model.ReportImage;
import is.cityreportsystem.services.ReportImageService;
import is.cityreportsystem.util.LoggerBean;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;

@Transactional
@Service
public class ReportImageServiceImpl implements ReportImageService {

    @Value("${reports.images.repository}")
    private String imagesRepo;
    ReportImageDAO reportImageDAO;
    private final LoggerBean loggerBean;

    public ReportImageServiceImpl(ReportImageDAO reportImageDAO, LoggerBean loggerBean){
        this.reportImageDAO=reportImageDAO;
        this.loggerBean = loggerBean;
    }
    public void addImage(ReportImage reportImage){
        reportImageDAO.save(reportImage);
    }
    public byte[] getImageById(long id){
        ReportImage image=reportImageDAO.findById(id).get();
        String path=imagesRepo+ File.separator+image.getName();
        try{
//            File file=new File(path);
//            System.out.println(file.getAbsolutePath());
//            System.out.println(file.exists());
            byte[] result=Files.readAllBytes(Paths.get(path));
            return result;
        }catch(Exception e){
            e.printStackTrace();
            loggerBean.logError(e);
            return null;
        }
    }
}
