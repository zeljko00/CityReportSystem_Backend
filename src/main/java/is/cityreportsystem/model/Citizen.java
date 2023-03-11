package is.cityreportsystem.model;

import is.cityreportsystem.model.enums.UserRole;
import is.cityreportsystem.model.enums.UserStatus;
import jakarta.persistence.*;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.Date;
import java.util.List;


@Entity
@EntityListeners(AuditingEntityListener.class)
@Inheritance(strategy=InheritanceType.JOINED)   //uses vertical strategy for inheritance mapping
@Data 											// automatically generates getters and setters
public class Citizen {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	private String idCard;
	private String firstName;
	private String lastName;
	private String phone;
	private String username;
	private String password;
	@Enumerated(EnumType.STRING)
	private UserStatus status;
	@OneToMany(mappedBy = "creator", fetch = FetchType.LAZY)
	private List<Report> myReports;
	@Enumerated(EnumType.STRING)
	private UserRole role;

	@Column(name = "created_at", updatable = false)
	@CreatedDate
	private Date createdAt;
}
