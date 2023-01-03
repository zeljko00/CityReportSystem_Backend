package is.cityreportsystem.model.DTO;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class LoginCitizenResponse extends CitizenDTO implements LoginResponse{
    String token;
}
