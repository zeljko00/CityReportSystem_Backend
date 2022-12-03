package is.cityreportsystem.services;

import is.cityreportsystem.model.Citizen;
import is.cityreportsystem.model.DTO.CitizenDTO;

public interface CitizenService {
	CitizenDTO login(String username, String password);
	CitizenDTO findCitizenById(long id);
	boolean checkIfIdCardIsUsed(String idCard);
	CitizenDTO createCitizen(CitizenDTO citizen);
	CitizenDTO updateCitizen(CitizenDTO citizen);
}
