package is.cityreportsystem;

import is.cityreportsystem.DAO.CitizenDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class CityReportSystemApplication {

    public static void main(String[] args) {
        SpringApplication.run(CityReportSystemApplication.class, args);
    }

}
