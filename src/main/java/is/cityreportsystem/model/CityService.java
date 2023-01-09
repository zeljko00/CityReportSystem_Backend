package is.cityreportsystem.model;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity
@Data
public class CityService {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String name;
    private String phone;
    private String mail;
    @OneToMany(mappedBy = "department", fetch = FetchType.LAZY)
    private List<CityOfficial> employees;

}
