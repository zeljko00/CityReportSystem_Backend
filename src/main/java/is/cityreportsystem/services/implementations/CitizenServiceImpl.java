package is.cityreportsystem.services.implementations;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Base64.Encoder;

import is.cityreportsystem.DAO.CitizenDAO;
import is.cityreportsystem.model.Citizen;
import is.cityreportsystem.model.DTO.CitizenDTO;
import is.cityreportsystem.model.enums.UserRole;
import is.cityreportsystem.model.enums.UserStatus;
import is.cityreportsystem.services.CitizenService;
import jakarta.transaction.Transactional;

import org.aspectj.weaver.bcel.BcelRenderer;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class CitizenServiceImpl implements CitizenService {

	private CitizenDAO citizenDAO;
	private ModelMapper modelMapper;
	private BCryptPasswordEncoder encoder;

	public CitizenServiceImpl(CitizenDAO citizenDAO, ModelMapper modelMapper) throws NoSuchAlgorithmException {
		this.citizenDAO = citizenDAO;
		this.modelMapper = modelMapper;
		encoder = new BCryptPasswordEncoder();
	}

	public CitizenDTO findCitizenById(long id) {
		Citizen citizen = citizenDAO.findById(id).get();  //findBy vraca Optional<TYPE>
		return modelMapper.map(citizen, CitizenDTO.class);
	}

	public CitizenDTO createCitizen(CitizenDTO c) {
		System.out.println("Creating citizen!");

		//new citizen will be created if account linked with specified id card number doesn't already exist
		if(!checkIfIdCardIsUsed(c.getIdCard())){
			try {
				String passwordHash = encoder.encode(c.getPassword());
				c.setPassword(passwordHash);
				c.setUsername(c.getFirstName()+c.getIdCard());
				c.setStatus(UserStatus.ACTIVE);
				c.setRole(UserRole.CITIZEN);
				Citizen citizen=modelMapper.map(c,Citizen.class);
				Citizen result=citizenDAO.saveAndFlush(citizen);
				System.out.println("Citizen created");
				c.setId((result.getId()));
				return c;
			} catch (Exception e) {
				e.printStackTrace(); //logovati
			}
		}
		return null;
	}
//	public CitizenDTO updateCitizen(CitizenDTO c) {
//		try {
//			byte[] data = c.getPasswordHash().getBytes("UTF-8");
//			byte[] dataHash = digest.digest(data);
//			byte[] encodedData = base64Encoder.encode(dataHash);
//			String passwordHash = new String(encodedData);
//
//			Citizen citizen=citizenDAO.findById(c.getId()).get();
//			if(citizen!=null){
//				citizen.setPhone(c.getPhone());
//				citizen.setActive(c.getActive());
//				citizen.setPasswordHash(passwordHash);
//
//				citizenDAO.saveAndFlush(citizen);
//				return c;
//			}
//			return null;
//		} catch (Exception e) {
//			return null;
//		}
//	}
	//checks whether account, linked with specifed idCard number, already exists
	public boolean checkIfIdCardIsUsed(String idCard) {
		Citizen citizen=citizenDAO.findByIdCard(idCard);
		return citizen!=null;
	}
}
