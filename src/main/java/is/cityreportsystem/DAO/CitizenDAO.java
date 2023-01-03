package is.cityreportsystem.DAO;

import is.cityreportsystem.model.Citizen;
import is.cityreportsystem.model.enums.UserStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CitizenDAO extends JpaRepository<Citizen,Long> {
    public Citizen findCitizenByUsername(String username);
    public Citizen findCitizenByUsernameAndStatus(String username, UserStatus status);
    public Citizen findByIdCard(String idCard);
    public Citizen findCitizenById(long id);
}