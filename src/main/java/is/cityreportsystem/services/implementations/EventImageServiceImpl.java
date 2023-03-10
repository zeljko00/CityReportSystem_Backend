package is.cityreportsystem.services.implementations;

import is.cityreportsystem.DAO.EventImageDAO;
import is.cityreportsystem.model.EventImage;
import is.cityreportsystem.services.EventImageService;
import is.cityreportsystem.util.LoggerBean;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;

@Transactional
@Service
public class EventImageServiceImpl implements EventImageService {
    @Value("${events.images.repository}")
    private String imagesRepo;
    private EventImageDAO eventImageDAO;
    private final LoggerBean loggerBean;

    public EventImageServiceImpl(EventImageDAO eventImageDAO, LoggerBean loggerBean){
        this.eventImageDAO=eventImageDAO;
        this.loggerBean = loggerBean;
    }
    public byte[] getImageById(long id){
        EventImage image=eventImageDAO.findById(id).get();
        String path=imagesRepo+ File.separator+image.getName();
        try{
            byte[] result= Files.readAllBytes(Paths.get(path));
            return result;
        }catch(Exception e){
            e.printStackTrace();
            loggerBean.logError(e);
            return null;
        }
    }
    public EventImage addImage(EventImage eventImage){
        return eventImageDAO.save(eventImage);
    }
}
