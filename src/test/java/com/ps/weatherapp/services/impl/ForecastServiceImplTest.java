package com.ps.weatherapp.services.impl;

import com.ps.weatherapp.models.externalresponse.CityWeatherData;
import com.ps.weatherapp.models.externalresponse.DayTempInfo;
import com.ps.weatherapp.models.externalresponse.DayWindInfo;
import com.ps.weatherapp.models.externalresponse.OpenWeatherResponse;
import com.ps.weatherapp.services.OpenWeatherMapService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;


public class ForecastServiceImplTest {

    @Spy
    @InjectMocks
    ForecastServiceImpl forecastService;

    @Mock
    OpenWeatherMapService openWeatherMapService;

    @Mock
    CacheServiceImpl cacheService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }


    @Test
    @DisplayName("Fore cast for a city")
    public void testGetForecastFor() {
        when(cacheService.getCityDateDataMap(anyString())).thenReturn(new HashMap<>());

        OpenWeatherResponse openWeatherResponse = new OpenWeatherResponse();
        List<CityWeatherData> cityWeatherDataList = new ArrayList<>();
        CityWeatherData cwd = new CityWeatherData();
        cwd.setMain(new DayTempInfo(34.0));
        cwd.setWind(new DayWindInfo(15.0));
        cwd.setDt(1651066690176L);
        cwd.setDt_txt("2022-04-27 18:00:00");
        cityWeatherDataList.add(cwd);

        openWeatherResponse.setList(cityWeatherDataList);

        when(openWeatherMapService.getForecastData(anyString())).thenReturn(openWeatherResponse);
        forecastService.getForecastFor("Indore");
    }

}