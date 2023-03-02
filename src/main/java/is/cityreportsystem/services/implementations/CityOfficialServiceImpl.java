package is.cityreportsystem.services.implementations;

import is.cityreportsystem.DAO.CityOfficialDAO;
import is.cityreportsystem.model.CityOfficial;
import is.cityreportsystem.model.CityService;
import is.cityreportsystem.model.DTO.CityOfficialDTO;
import is.cityreportsystem.model.ReportType;
import is.cityreportsystem.services.CityOfficialService;
import is.cityreportsystem.services.EventService;
import is.cityreportsystem.services.ReportService;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

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
        CityOfficial temp=cityOfficialDAO.findById(id).get();

        if(temp!=null){
            List<ReportType> types=temp.getDepartment().getReportTypes();
            temp.getDepartment().setReportTypes(null);
            CityOfficialDTO result=modelMapper.map(temp, CityOfficialDTO.class);
//            result.getDepartment().setReports(types.stream().map(t->t.getName()).collect(Collectors.toList()));
            result.setActiveEventsNum(eventService.getActiveEventsByCreatorId(id));
            result.setCreatedEventsNum(eventService.getEventsByCreatorId(id));
            return result;
        }
        return null;

    }
}
