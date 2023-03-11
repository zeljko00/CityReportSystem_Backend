package is.cityreportsystem;

import is.cityreportsystem.DAO.CitizenDAO;
import is.cityreportsystem.model.Citizen;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing(auditorAwareRef = "auditorProvider")
public class CityReportSystemApplication {
    public static void main(String[] args) {
        SpringApplication.run(CityReportSystemApplication.class, args);
    }

}
