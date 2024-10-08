package is.cityreportsystem.services.implementations;

import is.cityreportsystem.model.DTO.*;
import is.cityreportsystem.model.enums.UserRole;
import is.cityreportsystem.security.JwtUtil;
import is.cityreportsystem.services.AuthenticationService;
import is.cityreportsystem.services.CitizenService;
import is.cityreportsystem.services.CityOfficialService;
import is.cityreportsystem.util.LoggerBean;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class AuthenticationServiceImpl implements AuthenticationService {

    private final AuthenticationManager authenticationManager;
    private final CitizenService citizenService;
    private final CityOfficialService cityOfficialService;
    private final ModelMapper modelMapper;
    private final JwtUtil jwtUtil;
    private final LoggerBean loggerBean;

    public AuthenticationServiceImpl(AuthenticationManager authenticationManager, CitizenService citizenService, CityOfficialService cityOfficialService, ModelMapper modelMapper, JwtUtil jwtUtil, LoggerBean loggerBean) {
        this.authenticationManager = authenticationManager;
        this.citizenService = citizenService;
        this.cityOfficialService = cityOfficialService;
        this.modelMapper = modelMapper;
        this.jwtUtil = jwtUtil;
        this.loggerBean = loggerBean;
    }

    @Override
    public LoginResponse login(String username, String password) {
        System.out.println("Authentication service hit!");
        LoginResponse loginResponse = new LoginResponse();
        //authentication manager authenticates user based on received credentials -
        //manager compares user data from jwt and data gathered by jwtUserDetailsService
        //if authentication fails exception is thrown
        try{
        Authentication authenticate = authenticationManager
                .authenticate(
                        new UsernamePasswordAuthenticationToken(
                                username, password
                        )
                );
        //authenticated user

        JwtUser user = (JwtUser) authenticate.getPrincipal();
        //based on role response will contain info about Citizen or CityOfficial
        if (user.getRole().equals(UserRole.CITIZEN))
            loginResponse.setUser(citizenService.findCitizenById(user.getId()));
        else
            loginResponse.setUser(cityOfficialService.getCityOfficialById(user.getId()));
        // generating new jwt
        String token = jwtUtil.generateToken(user);
        System.out.println("Generated token: " + token);
        //user data and new jwt will be returned inside of LoginResponse,
        loginResponse.setToken(token);
        return loginResponse;}catch(Exception e){
            e.printStackTrace();
            loggerBean.logError(e);
            throw e;
        }
    }
}
