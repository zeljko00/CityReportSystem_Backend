package is.cityreportsystem.model.DTO;

import is.cityreportsystem.model.CityOfficial;
import is.cityreportsystem.model.Report;
import jakarta.persistence.OneToMany;
import lombok.Data;

import java.util.List;
@Data
public class CityServiceDTO {
    private long id;
    private String name;
    private String phone;
    private String mail;
//    List<String> reports;
}
