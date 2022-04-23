package com.ps.weatherapp.models.externalresponse;


import lombok.Data;

import java.util.List;

@Data
public class OpenWeatherResponse {

    private String cod;
    private int message;
    private int cnt;
    List<CityWeatherData> list;
    private City city;
    private CityRainInfo rain;
}
