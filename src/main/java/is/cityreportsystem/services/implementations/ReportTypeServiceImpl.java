package is.cityreportsystem.services.implementations;

import is.cityreportsystem.DAO.ReportTypeDAO;
import is.cityreportsystem.model.ReportType;
import is.cityreportsystem.services.ReportTypeService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReportTypeServiceImpl implements ReportTypeService {
    private final ReportTypeDAO reportTypeDAO;

    public ReportTypeServiceImpl(ReportTypeDAO reportTypeDAO) {
        this.reportTypeDAO = reportTypeDAO;
    }

    public List<String> getAllTypes(){
        List<ReportType> list=reportTypeDAO.findAll();
        return list.stream().map((ReportType e)->{
            return e.getName();
        }).toList();
    }
    public ReportType getByName(String name){
        return reportTypeDAO.getReportTypeByName(name);
    }
}
