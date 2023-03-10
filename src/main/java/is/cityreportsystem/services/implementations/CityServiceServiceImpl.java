package is.cityreportsystem.services.implementations;

import is.cityreportsystem.DAO.CityServiceDAO;
import is.cityreportsystem.model.CityService;
import is.cityreportsystem.model.DTO.CityServiceDTO;
import is.cityreportsystem.model.ReportType;
import is.cityreportsystem.services.CityServiceService;
import is.cityreportsystem.util.LoggerBean;
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
    private final LoggerBean loggerBean;

    public CityServiceServiceImpl(CityServiceDAO cityServiceDAO, ModelMapper modelMapper, LoggerBean loggerBean){
        this.cityServiceDAO=cityServiceDAO;
        this.modelMapper=modelMapper;
        this.loggerBean = loggerBean;
    }

    public CityServiceDTO getCityServiceById(long id)
    {
        CityService cityService=cityServiceDAO.findById(id).get();
        if(cityService==null)
            return null;
        else{
            List<ReportType> list=cityService.getReportTypes();
            cityService.setReportTypes(null);
            CityServiceDTO cityServiceDTO=modelMapper.map(cityService,CityServiceDTO.class);
//            cityServiceDTO.setReports(list.stream().map(t -> t.getName()).collect(Collectors.toList()));
            return cityServiceDTO;
        }
    }
    public List<String> getCityServiceNames(){
        return cityServiceDAO.findAll().stream().map((CityService ss) -> {return ss.getName();}).collect(Collectors.toList());
    }
}
