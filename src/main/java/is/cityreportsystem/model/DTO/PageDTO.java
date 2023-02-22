package is.cityreportsystem.model.DTO;

import lombok.Data;
import java.util.List;

@Data
public class PageDTO<T>{
    private List<T> data;
    private long pages;
}
