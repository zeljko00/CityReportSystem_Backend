package is.cityreportsystem.services;
import is.cityreportsystem.model.ReportType;

import java.util.List;
public interface ReportTypeService {
    List<String> getAllTypes();
    ReportType getByName(String name);
    List<String> getAllTypesByDepartment(long id);
}
