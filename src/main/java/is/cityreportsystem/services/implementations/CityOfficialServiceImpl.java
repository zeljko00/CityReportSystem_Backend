package is.cityreportsystem.services.implementations;

import is.cityreportsystem.DAO.CityOfficialDAO;
import is.cityreportsystem.model.CityOfficial;
import is.cityreportsystem.model.DTO.CityOfficialDTO;
import is.cityreportsystem.services.CityOfficialService;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class CityOfficialServiceImpl implements CityOfficialService {
    private final CityOfficialDAO cityOfficialDAO;
    private final ModelMapper modelMapper;

    public CityOfficialServiceImpl(CityOfficialDAO cityOfficialDAO, ModelMapper modelMapper) {
        this.cityOfficialDAO = cityOfficialDAO;
        this.modelMapper = modelMapper;
    }

    @Override
    public CityOfficialDTO getCityOfficialById(long id) {
        return modelMapper.map(cityOfficialDAO.findById(id).get(), CityOfficialDTO.class);
    }
}
