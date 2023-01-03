package is.cityreportsystem.services;

import is.cityreportsystem.model.DTO.LoginResponse;

public interface AuthenticationService {
    LoginResponse login(String username, String password);
}
