package com.ps.weatherapp.services.impl;


import com.ps.weatherapp.models.DayTemperature;
import com.ps.weatherapp.models.ForecastResponse;
import com.ps.weatherapp.models.externalresponse.CityDateData;
import com.ps.weatherapp.models.externalresponse.CityWeatherData;
import com.ps.weatherapp.models.externalresponse.OpenWeatherResponse;
import com.ps.weatherapp.services.ForecastService;
import com.ps.weatherapp.services.OpenWeatherMapService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;


import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@Log4j2
public class ForecastServiceImpl implements ForecastService {

    @Autowired
    private OpenWeatherMapService openWeatherMapService;

    @Autowired
    private CacheServiceImpl cacheService;


    @Override
    public Map<LocalDate, ForecastResponse> getForecastFor(String cityName) {
        //add 3 to current date for forecast
        List<LocalDate> daysRange = Stream.iterate(LocalDate.now(), date -> date.plusDays(1)).limit(3)
                .collect(Collectors.toList());

        //fetch data from cache
        Map<LocalDate, CityDateData> forecastData = cacheService.getCityDateDataMap(cityName);
        if(null != forecastData) {
            return forecast3Days(forecastData, daysRange);
        }
        //response from open weather api
        OpenWeatherResponse openWeatherResponse = openWeatherMapService.getForecastData(cityName);
        //create day wise data from response
        Map<LocalDate, CityDateData> cityDaysData =  createDayWiseData(cityName, openWeatherResponse);
        //return data for 3 days with logic
        return forecast3Days(cityDaysData, daysRange);

    }

    private Map<LocalDate, ForecastResponse> forecast3Days(Map<LocalDate, CityDateData> cityDateDataMap,
                                                       List<LocalDate> days) {
        Map<LocalDate, CityDateData> forecastMap = new HashMap<>();
        for(LocalDate ld : days) {
            if(cityDateDataMap.containsKey(ld)) {
                forecastMap.put(ld, cityDateDataMap.get(ld));
            }
        }
        return updateDayWiseData(forecastMap);
    }


    public Map<LocalDate, CityDateData> createDayWiseData(String cityName, OpenWeatherResponse openWeatherResponse) {
        Map<LocalDate, CityDateData> dayWiseData = new TreeMap<>();

        for (CityWeatherData cwd : openWeatherResponse.getList()) {
            LocalDate date = LocalDate.parse(cwd.getDt_txt().substring(0, 10));
            if (dayWiseData.containsKey(date)) {
                CityDateData cityDateData = dayWiseData.get(date);
                cityDateData.getTemperatureData().add(cwd.getMain().getTemp());
                cityDateData.getWindData().add(cwd.getWind().getSpeed());
                Collections.sort(cityDateData.getTemperatureData());
                Collections.sort(cityDateData.getWindData());
                dayWiseData.put(date, cityDateData);
            } else {
                List<Double> temperatureList = new ArrayList<>();
                List<Double> windList = new ArrayList<>();
                CityDateData cityDateData = new CityDateData();
                temperatureList.add(cwd.getMain().getTemp());
                windList.add(cwd.getWind().getSpeed());

                cityDateData.setTemperatureData(temperatureList);
                cityDateData.setWindData(windList);
                dayWiseData.put(date, cityDateData);
            }
        }
        cacheService.putWeatherData(cityName, new TreeMap<>(dayWiseData));
        return dayWiseData;
    }

    private Map<LocalDate, ForecastResponse> updateDayWiseData(Map<LocalDate, CityDateData> dateCityDateDataMap) {
        Map<LocalDate, ForecastResponse> forecastResponseMap = new HashMap<>();
        for(Map.Entry<LocalDate, CityDateData> entry : dateCityDateDataMap.entrySet()) {
            ForecastResponse fr = new ForecastResponse();
            List<Double> maxMin  = entry.getValue().getTemperatureData();
            fr.setDayTemperature(new DayTemperature(maxMin.get(0), maxMin.get(maxMin.size()-1)));
            fr.setStatus(maxMin.get(maxMin.size()-1) > 40.0 ? "Use sunscreen lotion" : "Have a nice day");
            forecastResponseMap.put(entry.getKey(), fr);
        }
        return forecastResponseMap;
    }
}
