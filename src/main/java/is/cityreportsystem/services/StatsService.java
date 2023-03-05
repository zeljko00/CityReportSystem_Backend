package is.cityreportsystem.services;

import is.cityreportsystem.model.DTO.Stats;

public interface StatsService {
    Stats getStats(String startDate, String endDate, String type);
}
