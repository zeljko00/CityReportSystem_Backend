package is.cityreportsystem.services.implementations;

import is.cityreportsystem.DAO.CityServiceDAO;
import is.cityreportsystem.DAO.ReportTypeDAO;
import is.cityreportsystem.model.ReportType;
import is.cityreportsystem.services.ReportTypeService;
import is.cityreportsystem.util.LoggerBean;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ReportTypeServiceImpl implements ReportTypeService {
    private final ReportTypeDAO reportTypeDAO;
    private final CityServiceDAO cityServiceDAO;
    private final LoggerBean loggerBean;
    public ReportTypeServiceImpl(ReportTypeDAO reportTypeDAO, CityServiceDAO cityServiceDAO, LoggerBean loggerBean) {
        this.reportTypeDAO = reportTypeDAO;
        this.cityServiceDAO = cityServiceDAO;
        this.loggerBean = loggerBean;
    }

    public List<String> getAllTypes(){
        List<ReportType> list=reportTypeDAO.findAll();
        return list.stream().map((ReportType e)->{
            return e.getName();
        }).toList();
    }
    public List<String> getAllTypesByDepartment(long id){
        try {
            return cityServiceDAO.findById(id).get().getReportTypes().stream().map((ReportType e) -> {
                return e.getName();
            }).toList();
        }catch(Exception e){
            e.printStackTrace();
            loggerBean.logError(e);
            return new ArrayList<>();
        }
    }
    public ReportType getByName(String name){
        return reportTypeDAO.getReportTypeByName(name);
    }
}
