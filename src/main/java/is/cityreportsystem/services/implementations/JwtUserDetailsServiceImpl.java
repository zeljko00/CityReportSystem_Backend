package is.cityreportsystem.services.implementations;

import is.cityreportsystem.DAO.CitizenDAO;
import is.cityreportsystem.model.Citizen;
import is.cityreportsystem.model.DTO.JwtUser;
import is.cityreportsystem.services.JwtUserDetailsService;
import is.cityreportsystem.util.LoggerBean;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
// service which provides information about user specified by username in received jwt
@Service
public class JwtUserDetailsServiceImpl implements JwtUserDetailsService {

    private final CitizenDAO citizenDAO;
    private final ModelMapper modelMapper;
    private final LoggerBean loggerBean;

    public JwtUserDetailsServiceImpl(CitizenDAO citizenDAO, ModelMapper modelMapper, LoggerBean loggerBean) {
        this.citizenDAO = citizenDAO;
        this.modelMapper = modelMapper;
        this.loggerBean = loggerBean;
    }

    // retrieving info about active user specified by username extracted from jwt
    @Override
    public JwtUser loadUserByUsername(String username) throws UsernameNotFoundException {
        System.out.println("Searching for username: "+username);
        Citizen citizen = citizenDAO.findCitizenByUsername(username);
        if (citizen != null)
            return modelMapper.map(citizen, JwtUser.class);
        else
            throw new UsernameNotFoundException(username);
    }
}
