package com.ps.weatherapp.services.impl;


import com.ps.weatherapp.models.DayTemperature;
import com.ps.weatherapp.models.ForecastResponse;
import com.ps.weatherapp.models.externalresponse.CityDateData;
import com.ps.weatherapp.models.externalresponse.CityWeatherData;
import com.ps.weatherapp.models.externalresponse.OpenWeatherResponse;
import com.ps.weatherapp.services.ForecastService;
import com.ps.weatherapp.services.OpenWeatherMapService;
import com.ps.weatherapp.utils.ForecastServiceUtils;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;


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

    @Value("${forecast.days}")
    private int forecastDays;


    @Override
    public Map<LocalDate, ForecastResponse> getForecastFor(String cityName) {
        log.info("Fetching data for city : {}", cityName);
        //add 3 to current date for forecast
        List<LocalDate> daysRange = Stream.iterate(LocalDate.now(), date -> date.plusDays(1)).limit(forecastDays)
                .collect(Collectors.toList());

        //fetch data from cache
        Map<LocalDate, CityDateData> forecastData = cacheService.getCityDateDataMap(cityName);
        if (!CollectionUtils.isEmpty(forecastData)) {
            log.info("Fetching data from cache for city : {} ", cityName);
            return forecast3Days(forecastData, daysRange);
        }
        //response from open weather api
        OpenWeatherResponse openWeatherResponse = openWeatherMapService.getForecastData(cityName);
        //create day wise data from response
        Map<LocalDate, CityDateData> cityDaysData = createDayWiseData(cityName, openWeatherResponse);
        //return data for 3 days with logic
        return forecast3Days(cityDaysData, daysRange);

    }

    private Map<LocalDate, ForecastResponse> forecast3Days(Map<LocalDate, CityDateData> cityDateDataMap,
                                                           List<LocalDate> days) {
        Map<LocalDate, CityDateData> forecastMap = new HashMap<>();
        for (LocalDate ld : days) {
            if (cityDateDataMap.containsKey(ld)) {
                forecastMap.put(ld, cityDateDataMap.get(ld));
            }
        }
        return ForecastServiceUtils.updateDayWiseData(forecastMap);
    }


    public Map<LocalDate, CityDateData> createDayWiseData(String cityName, OpenWeatherResponse openWeatherResponse) {
        log.info("creating day wise data for city : {} ", cityName);
        Map<LocalDate, CityDateData> dayWiseData = new TreeMap<>();

        for (CityWeatherData cwd : openWeatherResponse.getList()) {
            LocalDate date = LocalDate.parse(cwd.getDt_txt().substring(0, 10));
            if (dayWiseData.containsKey(date)) {
                CityDateData cityDateData = dayWiseData.get(date);
                cityDateData.getTemperatureData().add(cwd.getMain().getTemp());
                cityDateData.getWindData().add(cwd.getWind().getSpeed());
                if (null != cwd.getRain()) {
                    double rainData;
                    rainData = Double.parseDouble(cwd.getRain().getH());
                    if (null == cityDateData.getRainData()) {
                        cityDateData.setRainData(new ArrayList<>());
                    }
                    cityDateData.getRainData().add(rainData);
                }

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

                if(null != cwd.getRain()) {
                    List<Double> rainList = new ArrayList<>();
                    rainList.add(cwd.getRain() != null ? Double.parseDouble(cwd.getRain().getH()) : null);
                    cityDateData.setRainData(rainList);
                }
                dayWiseData.put(date, cityDateData);
            }
        }
        cacheService.putWeatherData(cityName, new TreeMap<>(dayWiseData));
        return dayWiseData;
    }
}
