package is.cityreportsystem.services.implementations;

import is.cityreportsystem.DAO.CityOfficialDAO;
import is.cityreportsystem.model.DTO.CityOfficialDTO;
import is.cityreportsystem.services.CityOfficialService;
import is.cityreportsystem.services.EventService;
import is.cityreportsystem.services.ReportService;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class CityOfficialServiceImpl implements CityOfficialService {
    private final CityOfficialDAO cityOfficialDAO;

    private final EventService eventService;
    private final ReportService reportService;

    private final ModelMapper modelMapper;

    public CityOfficialServiceImpl(CityOfficialDAO cityOfficialDAO, EventService eventService, ReportService reportService, ModelMapper modelMapper) {
        this.cityOfficialDAO = cityOfficialDAO;
        this.eventService = eventService;
        this.reportService = reportService;
        this.modelMapper = modelMapper;
    }

    @Override
    public CityOfficialDTO getCityOfficialById(long id) {
        CityOfficialDTO result=modelMapper.map(cityOfficialDAO.findById(id).get(), CityOfficialDTO.class);
        if(result!=null){
            result.setActiveEventsNum(eventService.getActiveEventsByCreatorId(id));
            result.setCreatedEventsNum(eventService.getEventsByCreatorId(id));
        }
        return result;

    }
}
