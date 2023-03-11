package is.cityreportsystem.util;

import is.cityreportsystem.model.Citizen;
import is.cityreportsystem.model.DTO.JwtUser;
import is.cityreportsystem.services.CitizenService;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

public class AuditorAwareImpl implements AuditorAware<Citizen> {
    private final CitizenService citizenService;

    public AuditorAwareImpl(CitizenService citizenService ) {
        this.citizenService=citizenService;
    }

    @Override
    public Optional<Citizen> getCurrentAuditor(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null) {
            try{
            JwtUser user=(JwtUser) authentication.getPrincipal();
            return Optional.of(citizenService.findById(user.getId()));}
            catch(Exception e){
                return Optional.empty();
            }
        }
        return Optional.empty();
    }
}
