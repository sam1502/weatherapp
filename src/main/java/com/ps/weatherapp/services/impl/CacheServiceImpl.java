package com.ps.weatherapp.services.impl;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.ps.weatherapp.models.externalresponse.CityDateData;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Service
@Log4j2
public class CacheServiceImpl {

    private Cache<String, Map<LocalDate, CityDateData>> weatherInfoCache;

    @PostConstruct
    public void setup() {
        weatherInfoCache = Caffeine.newBuilder().maximumSize(1000)
                .expireAfterWrite(2, TimeUnit.DAYS).build();
    }

    public void putWeatherData(String cityName, Map<LocalDate, CityDateData> cityDateDataMap) {
        log.info("Populating cache with weather data for city : {} and size : {}", cityName, cityDateDataMap.size());
        weatherInfoCache.put(cityName, cityDateDataMap);
    }

    public Map<LocalDate, CityDateData> getCityDateDataMap(String cityName) {
        return weatherInfoCache.getIfPresent(cityName);
    }
}
