package is.cityreportsystem.services;

import is.cityreportsystem.model.DTO.Stats;
import is.cityreportsystem.model.DTO.YearStats;

public interface StatsService {
    Stats getStats(String startDate, String endDate, String type);
    YearStats getStatsByYear(int year);
}
