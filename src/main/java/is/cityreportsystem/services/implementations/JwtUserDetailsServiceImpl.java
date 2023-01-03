package is.cityreportsystem.services.implementations;

import is.cityreportsystem.DAO.CitizenDAO;
import is.cityreportsystem.model.Citizen;
import is.cityreportsystem.model.DTO.JwtUser;
import is.cityreportsystem.model.enums.UserStatus;
import is.cityreportsystem.services.JwtUserDetailsService;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class JwtUserDetailsServiceImpl implements JwtUserDetailsService {

    private final CitizenDAO citizenDAO;
    private final ModelMapper modelMapper;

    public JwtUserDetailsServiceImpl(CitizenDAO citizenDAO, ModelMapper modelMapper) {
        this.citizenDAO = citizenDAO;
        this.modelMapper = modelMapper;
    }

    // retrieve active user info by specified username
    @Override
    public JwtUser loadUserByUsername(String username) throws UsernameNotFoundException {
        Citizen citizen=citizenDAO.findCitizenByUsernameAndStatus(username,UserStatus.ACTIVE);
        if(citizen!=null){
            System.out.println("Found");
            return modelMapper.map(citizen,JwtUser.class);
        }
        else{
            System.out.println("User not found!");
            throw new UsernameNotFoundException(username);
        }
    }
}
