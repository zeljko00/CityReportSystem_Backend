package is.cityreportsystem.services;

import is.cityreportsystem.model.EventImage;
import is.cityreportsystem.model.ReportImage;

public interface EventImageService {
    EventImage addImage(EventImage eventImage);
    public byte[] getImageById(long id);
}
