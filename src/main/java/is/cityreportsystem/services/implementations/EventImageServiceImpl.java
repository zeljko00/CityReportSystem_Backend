package is.cityreportsystem.services.implementations;

import is.cityreportsystem.DAO.EventImageDAO;
import is.cityreportsystem.model.EventImage;
import is.cityreportsystem.services.EventImageService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.io.InputStream;

@Transactional
@Service
public class EventImageServiceImpl implements EventImageService {
    private EventImageDAO eventImageDAO;

    public EventImageServiceImpl(EventImageDAO eventImageDAO){
        this.eventImageDAO=eventImageDAO;
    }
    public byte[] getImageById(long id){
        EventImage image=eventImageDAO.findById(id).get();
        InputStream is=getClass().getClassLoader().getResourceAsStream(image.getName());
        try {
            return is.readAllBytes();
        }
        catch(Exception e){
            e.printStackTrace();
        }
        return null;
    }
}
