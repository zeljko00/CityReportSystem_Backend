package is.cityreportsystem.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
public class EventImage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String name;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "event", referencedColumnName = "id", nullable = false)
    private Event event;
}
