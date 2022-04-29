package com.ps.weatherapp.utils;

import com.ps.weatherapp.exceptions.UtilityClassException;
import com.ps.weatherapp.models.DayTemperature;
import com.ps.weatherapp.models.ForecastResponse;
import com.ps.weatherapp.models.externalresponse.CityDateData;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class ForecastServiceUtils {

    private ForecastServiceUtils() {
        throw new UtilityClassException("Utility Class!");
    }

    public static Map<LocalDate, ForecastResponse> updateDayWiseData(
            Map<LocalDate, CityDateData> dateCityDateDataMap) {
        Map<LocalDate, ForecastResponse> forecastResponseMap = new HashMap<>();
        for (Map.Entry<LocalDate, CityDateData> entry : dateCityDateDataMap.entrySet()) {
            ForecastResponse fr = new ForecastResponse();
            List<Double> maxMin = entry.getValue().getTemperatureData();
            fr.setDayTemperature(new DayTemperature(maxMin.get(0), maxMin.get(maxMin.size() - 1)));
            fr.setStatus(entry.getValue().getRainData() != null ? "Carry Umbrella" : "Have a nice day");
            fr.setStatus(maxMin.get(maxMin.size() - 1) > 40.0 ? "Use sunscreen lotion" : "Have a nice day");
            forecastResponseMap.put(entry.getKey(), fr);
        }
        return forecastResponseMap;
    }
}
