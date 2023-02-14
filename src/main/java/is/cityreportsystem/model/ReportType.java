package is.cityreportsystem.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class ReportType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String name;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "responsibleService", referencedColumnName = "id")
    private CityService responsibleService;
}
