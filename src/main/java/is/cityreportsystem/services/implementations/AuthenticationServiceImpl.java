package is.cityreportsystem.services.implementations;

import is.cityreportsystem.model.DTO.*;
import is.cityreportsystem.model.enums.UserRole;
import is.cityreportsystem.security.JwtUtil;
import is.cityreportsystem.services.AuthenticationService;
import is.cityreportsystem.services.CitizenService;
import is.cityreportsystem.services.CityOfficialService;
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

    public AuthenticationServiceImpl(AuthenticationManager authenticationManager, CitizenService citizenService, CityOfficialService cityOfficialService, ModelMapper modelMapper, JwtUtil jwtUtil) {
        this.authenticationManager = authenticationManager;
        this.citizenService = citizenService;
        this.cityOfficialService = cityOfficialService;
        this.modelMapper = modelMapper;
        this.jwtUtil = jwtUtil;
    }

    @Override
    public LoginResponse login(String username, String password) {
        System.out.println("Auth service hit!");
        LoginResponse loginResponse = null;
        try {
            //authentication manager authenticates user based on received credentials -
            //manager compares user data from jwt and data gathered by jwtUserDetailsService
            //if authentication fails exception is thrown
            Authentication authenticate = authenticationManager
                    .authenticate(
                            new UsernamePasswordAuthenticationToken(
                                    username, password
                            )
                    );
            //authenticated user
            JwtUser user = (JwtUser) authenticate.getPrincipal();
            //based on role response will contain info about Citizen or CityOfficial
            if(user.getRole().equals(UserRole.CITIZEN)){
                LoginCitizenResponse loginCitizenResponse=modelMapper.map(citizenService.findCitizenById(user.getId()), LoginCitizenResponse.class);
                // generating jwt
                String token=jwtUtil.generateToken(user);
                System.out.println(token);
                loginCitizenResponse.setToken(token);
                loginResponse=loginCitizenResponse;
            }
            else
            {
                LoginCitizenResponse loginCitizenResponse=modelMapper.map(cityOfficialService.getCityOfficialById(user.getId()), LoginCitizenResponse.class);
                loginCitizenResponse.setToken(jwtUtil.generateToken(user));
                loginResponse=loginCitizenResponse;
            }
        } catch (Exception ex) {
            throw ex;               //logovati
        }
        return loginResponse;
    }
}
