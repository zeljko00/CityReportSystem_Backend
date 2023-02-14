package is.cityreportsystem.model.DTO;

import lombok.Data;

@Data
public class LoginResponse{
    String token;
    CitizenDTO user;
}
