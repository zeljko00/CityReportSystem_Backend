package is.cityreportsystem.util;

import is.cityreportsystem.model.Citizen;
import is.cityreportsystem.services.CitizenService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;

@Configuration
public class AuditConfig {
    private final CitizenService citizenService;

    public AuditConfig(CitizenService citizenService) {
        this.citizenService = citizenService;
    }

    @Bean
    AuditorAware<Citizen> auditorProvider() {return new AuditorAwareImpl(citizenService);
    }
}
