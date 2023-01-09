package is.cityreportsystem.services.implementations;

import is.cityreportsystem.DAO.CityServiceDAO;
import is.cityreportsystem.model.CityService;
import is.cityreportsystem.model.DTO.CityServiceDTO;
import is.cityreportsystem.services.CityServiceService;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class CityServiceServiceImpl implements CityServiceService {
    private CityServiceDAO cityServiceDAO;
    private ModelMapper modelMapper;

    public CityServiceServiceImpl(CityServiceDAO cityServiceDAO, ModelMapper modelMapper){
        this.cityServiceDAO=cityServiceDAO;
        this.modelMapper=modelMapper;
    }

    public CityServiceDTO getCityServiceById(long id)
    {
        CityService cityService=cityServiceDAO.findById(id).get();
        if(cityService==null)
            return null;
        else{
            CityServiceDTO cityServiceDTO=modelMapper.map(cityService,CityServiceDTO.class);
            return cityServiceDTO;
        }
    }
}
