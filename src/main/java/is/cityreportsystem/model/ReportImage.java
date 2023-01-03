package is.cityreportsystem.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
public class ReportImage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String name;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "report", referencedColumnName = "id", nullable = false)
    private Report report;
}