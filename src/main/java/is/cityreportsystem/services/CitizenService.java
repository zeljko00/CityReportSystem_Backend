package is.cityreportsystem.services;

import is.cityreportsystem.model.Citizen;
import is.cityreportsystem.model.DTO.CitizenDTO;

public interface CitizenService {
	CitizenDTO findCitizenById(long id);
	CitizenDTO createCitizen(CitizenDTO citizen);
//	CitizenDTO updateCitizen(CitizenDTO citizen);
}
