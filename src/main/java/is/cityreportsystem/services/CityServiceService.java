package is.cityreportsystem.services;

import is.cityreportsystem.model.DTO.CityServiceDTO;
import java.util.List;

public interface CityServiceService {
    CityServiceDTO getCityServiceById(long id);
    List<String> getCityServiceNames();

}
