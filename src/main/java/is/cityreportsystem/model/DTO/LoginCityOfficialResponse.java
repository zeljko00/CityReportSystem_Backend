package is.cityreportsystem.model.DTO;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class LoginCityOfficialResponse extends CityOfficialDTO implements LoginResponse{
    String token;
}
