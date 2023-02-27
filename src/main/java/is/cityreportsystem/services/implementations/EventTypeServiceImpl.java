package is.cityreportsystem.services.implementations;

import is.cityreportsystem.model.enums.EventType;
import is.cityreportsystem.services.EventTypeService;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class EventTypeServiceImpl implements EventTypeService {
    public List<String> getEventTypes(){
        return Arrays.stream(EventType.values()).map((EventType et)-> {
            return et.name();
        }).collect(Collectors.toList());
    }
}
