package is.cityreportsystem.services.implementations;

import is.cityreportsystem.DAO.CityServiceDAO;
import is.cityreportsystem.model.CityService;
import is.cityreportsystem.model.DTO.CityServiceDTO;
import is.cityreportsystem.services.CityServiceService;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

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
            cityServiceDTO.setReportTypes(cityService.getReportTypes().stream().map(t -> t.getName()).collect(Collectors.toList()));
            return cityServiceDTO;
        }
    }
    public List<String> getCityServiceNames(){
        return cityServiceDAO.findAll().stream().map((CityService ss) -> {return ss.getName();}).collect(Collectors.toList());
    }
}
