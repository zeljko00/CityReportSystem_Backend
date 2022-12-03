package is.cityreportsystem.DAO;

import is.cityreportsystem.model.Citizen;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CitizenDAO extends JpaRepository<Citizen,Long> {
    public Citizen findCitizenByUsernameAndPasswordHash(String username, String passwordHash);
    public Citizen findByIdCard(String idCard);
}