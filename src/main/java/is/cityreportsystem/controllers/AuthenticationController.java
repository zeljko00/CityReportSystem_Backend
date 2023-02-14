package is.cityreportsystem.controllers;

import is.cityreportsystem.model.DTO.CitizenDTO;
import is.cityreportsystem.model.DTO.LoginResponse;
import is.cityreportsystem.services.AuthenticationService;
import is.cityreportsystem.services.CitizenService;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.*;

import java.util.Base64;

@RestController
public class AuthenticationController {
    private final AuthenticationService authenticationService;
    private final CitizenService citizenService;
    private final Base64.Decoder decoder;

    public AuthenticationController(AuthenticationService authenticationService,CitizenService citizenService) {
        this.authenticationService= authenticationService;
        this.decoder = Base64.getDecoder();
        this.citizenService = citizenService;
    }
    @GetMapping("/login")
    public LoginResponse login(@RequestHeader(HttpHeaders.AUTHORIZATION) String auth) {
        //parsing credentials from Authorization header
        String[] tokens=auth.split(" ");
        byte[] data = tokens[1].getBytes();
        byte[] decodedData = decoder.decode(data);
        String credentials = new String(decodedData);
        tokens = credentials.split(":");
        String username = tokens[0];
        String password = tokens[1];
        // if credentials are valid user data and new jwt will be returned inside of LoginResponse,
        // otherwise 401 will be returned
        return authenticationService.login(username,password);
    }
    @PostMapping("/signup")
    public LoginResponse signup(@RequestBody CitizenDTO citizen){
        System.out.println("Signup hit!");
        String password= citizen.getPassword();
        CitizenDTO cit=citizenService.createCitizen(citizen);
        return authenticationService.login(cit.getUsername(), password);
    }
}