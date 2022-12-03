package is.cityreportsystem.services.implementations;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Base64.Encoder;

import is.cityreportsystem.DAO.CitizenDAO;
import is.cityreportsystem.model.Citizen;
import is.cityreportsystem.model.DTO.CitizenDTO;
import is.cityreportsystem.services.CitizenService;
import jakarta.transaction.Transactional;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class CitizenServiceImpl implements CitizenService {

	private CitizenDAO citizenDAO;
	private ModelMapper modelMapper;
	private MessageDigest digest;
	private Encoder base64Encoder;

	public CitizenServiceImpl(CitizenDAO citizenDAO, ModelMapper modelMapper) throws NoSuchAlgorithmException {
		this.citizenDAO = citizenDAO;
		this.modelMapper = modelMapper;
		try {
			digest = MessageDigest.getInstance("MD5");
			base64Encoder = Base64.getEncoder();
		}catch (NoSuchAlgorithmException e){
			e.printStackTrace();
			throw e;
		}
	}

	public CitizenDTO login(String username, String password) {
		try {
			byte[] data = password.getBytes("UTF-8");
			byte[] dataHash = digest.digest(data);
			byte[] encodedData = base64Encoder.encode(dataHash);
			String passwordHash = new String(encodedData);
			System.out.println(passwordHash);
			Citizen citizen=citizenDAO.findCitizenByUsernameAndPasswordHash(username,passwordHash);
			if(citizen!=null)
				return modelMapper.map(citizen,CitizenDTO.class);
			else
				return null;
		} catch (Exception e) {
			e.printStackTrace(); 			//logovati
			return null;
		}
	}

	public CitizenDTO findCitizenById(long id) {
		Citizen citizen = citizenDAO.findById(id).get();  //findBy vraca Optional<TYPE>
		return modelMapper.map(citizen, CitizenDTO.class);
	}

	public CitizenDTO createCitizen(CitizenDTO c) {
		System.out.println(c);
		try {
			byte[] data = c.getPasswordHash().getBytes("UTF-8");
			byte[] dataHash = digest.digest(data);
			byte[] encodedData = base64Encoder.encode(dataHash);
			String passwordHash = new String(encodedData);
			c.setPasswordHash(passwordHash);
			c.setUsername(c.getFirstName()+c.getIdCard());
			c.setActive((byte)1);
			Citizen citizen=modelMapper.map(c,Citizen.class);
			Citizen result=citizenDAO.saveAndFlush(citizen);
			c.setId((result.getId()));
			return c;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	public CitizenDTO updateCitizen(CitizenDTO c) {
		try {
			byte[] data = c.getPasswordHash().getBytes("UTF-8");
			byte[] dataHash = digest.digest(data);
			byte[] encodedData = base64Encoder.encode(dataHash);
			String passwordHash = new String(encodedData);

			Citizen citizen=citizenDAO.findById(c.getId()).get();
			if(citizen!=null){
				citizen.setPhone(c.getPhone());
				citizen.setActive(c.getActive());
				citizen.setPasswordHash(passwordHash);

				citizenDAO.saveAndFlush(citizen);
				return c;
			}
			return null;
		} catch (Exception e) {
			return null;
		}
	}
	public boolean checkIfIdCardIsUsed(String idCard) {
		Citizen citizen=citizenDAO.findByIdCard(idCard);
		return citizen!=null;
	}
}
